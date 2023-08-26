/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auth.Controller;

import Admin.Controller.AdminDashboardController;
import Database.DatabaseConnection;
import Entity.User;
import Client.Controller.ClientDashboardController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class LoginController implements Initializable {

    public List<User> userInfo = new ArrayList<>();

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$";
    @FXML
    private TextField emailTF;
    @FXML
    private TextField passwordTF;
    @FXML
    private Button loginBtn;
    @FXML
    private Button signupBtn;
    Alert alert;
    Statement statement;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Connection connection = DatabaseConnection.get_connection();
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println("Connection failed");
        }

    }

    @FXML
    private void loginBtnHandle(ActionEvent event) {
        String email = emailTF.getText();
        String password = passwordTF.getText();

        if (isValidEmail(email) && isValidPassword(password)) {
            String hashedPassword = Hash.getMd5Hash(password);
            String sql = "SELECT * FROM users WHERE email = '" + email + "' And Password = '" + hashedPassword + "' limit 1";
            try {
                ResultSet rs = statement.executeQuery(sql);
                rs.next();
                User user = new User(rs.getInt("id"), rs.getInt("role"), rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("password"));
                if (user.getRole() == 0) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Admin/views/AdminDashboard.fxml"));
                        loader.setController(new AdminDashboardController());
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                        ((Stage) loginBtn.getScene().getWindow()).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    openClientDashboard(user);
                }

            } catch (SQLException ex) {
                alert = new Alert(Alert.AlertType.ERROR, "Invalid user", ButtonType.OK);
                alert.show();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Invalid user", ButtonType.OK);
            alert.show();
        }

    }

    @FXML
    private void signupBtnHandle(ActionEvent event) {
        Stage stage = (Stage) signupBtn.getScene().getWindow();
        stage.close();
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/SignUp.fxml"));
        loader.setController(new SignUpController());
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException ex) {
            alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
        newStage.show();
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

    private void openClientDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Client/views/ClientDashboard.fxml"));
            loader.setController(new ClientDashboardController());
            Parent root = loader.load();

            ClientDashboardController dashboardController = loader.getController();
            dashboardController.setUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) loginBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
