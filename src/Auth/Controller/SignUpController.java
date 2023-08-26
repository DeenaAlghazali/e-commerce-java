/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auth.Controller;

import Client.Controller.ClientDashboardController;
import Database.DatabaseConnection;
import Entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class SignUpController implements Initializable {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$";

    @FXML
    private TextField nameTF;
    @FXML
    private TextField emailTF;
    @FXML
    private TextField moblieTF;
    @FXML
    private PasswordField passwordTF;
    @FXML
    private Button signUpBtn;
    @FXML
    private Button backBtn;
    Alert alert;
    Statement statement;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection connection = DatabaseConnection.get_connection();
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println("Connection failed");
        }
    }

    @FXML
    private void signUpBtnHandle(ActionEvent event) {
        String name = nameTF.getText();
        String email = emailTF.getText();
        String mobile = moblieTF.getText();
        String password = passwordTF.getText();
        if (isValidInput(name) && isValidEmail(email) && isValidNumericValue(mobile) && isValidPassword(password)) {
            String hashedPassword = Hash.getMd5Hash(password);

            try {
                ResultSet rs = statement.executeQuery("SELECT count(*) FROM users WHERE email ='" + email + "';");
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    alert = new Alert(Alert.AlertType.ERROR, "Email is used try another one", ButtonType.OK);
                    alert.show();
                } else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("Are you sure you want to register in our system?");
                    ButtonType buttonTypeOK = new ButtonType("OK");
                    ButtonType buttonTypeCancel = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
                    alert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == buttonTypeOK) {
                            String sql = "INSERT INTO users (name, email, mobile, password, role) VALUES ('" + name + "', '" + email + "', '" + mobile + "', '" + hashedPassword + "', '1')";
                            try {

                                int rowCount = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

                                ResultSet generatedKeys = statement.getGeneratedKeys();
                                generatedKeys.next();
                                int newuserId = generatedKeys.getInt(1);
                                User user = new User(newuserId, 1, name, email, mobile, hashedPassword);
                                if (rowCount > 0) {
                                    alert = new Alert(Alert.AlertType.CONFIRMATION, "User Added Successfully", ButtonType.OK);
                                    alert.show();

                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Client/views/ClientDashboard.fxml"));
                                        loader.setController(new ClientDashboardController());
                                        Parent root = loader.load();

                                        ClientDashboardController dashboardController = loader.getController();
                                        dashboardController.setUser(user);

                                        Stage stage = new Stage();
                                        stage.setScene(new Scene(root));
                                        stage.show();

                                        ((Stage) signUpBtn.getScene().getWindow()).close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    alert = new Alert(Alert.AlertType.ERROR, "User Added Faild", ButtonType.OK);
                                    alert.show();
                                }
                            } catch (SQLException ex) {
                                alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                                alert.show();
                            }
                        } else if (buttonType == buttonTypeCancel) {
                            Platform.exit();
                        }
                    });
                }

            } catch (SQLException ex) {
                alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.show();
            }
        }
    }

    @FXML
    private void backBtnHandle(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Login.fxml"));
        loader.setController(new LoginController());
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException ex) {
            alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
        newStage.show();
    }

    private boolean isValidInput(String input) {
        if (!input.isEmpty()) {
            return true;
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty value Alert");
            alert.setHeaderText("Empty Failds Values!");
            alert.setContentText("Please enter values in all Failds..\nDont't Keep It Empty!");
            alert.show();
            return false;

        }
    }

    public boolean isValidEmail(String email) {
        if (isValidInput(email)) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Email is not valid", ButtonType.OK);
                alert.show();
                return false;
            }
        }
        return false;
    }

    public boolean isValidPassword(String password) {
        if (isValidInput(password)) {
            if (password.length() >= 8) {
                return true;
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Password is not valid", ButtonType.OK);
                alert.show();
                return false;
            }
        }
        return false;

    }

    private boolean isValidNumericValue(String input) {
        if (isValidInput(input)) {
            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException ex) {
                alert = new Alert(Alert.AlertType.ERROR, "Please enter numeric value in both Age & Salary Failds", ButtonType.OK);
                alert.show();
            }
        }
        return false;

    }
}
