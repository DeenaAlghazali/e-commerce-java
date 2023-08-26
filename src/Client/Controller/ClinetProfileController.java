/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import Database.DatabaseConnection;
import Entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class ClinetProfileController implements Initializable {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$";

    @FXML
    private TableView<User> userInfoTable;
    @FXML
    private TableColumn<User, String> tcName;
    @FXML
    private TableColumn<User, String> tcEmail;
    @FXML
    private TableColumn<User, String> tcMobile;
    @FXML
    private Button backBtn;
    Statement statement;
    private User user;
    @FXML
    private Button showBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private MenuItem pink;

    @FXML
    private MenuItem green;

    @FXML
    private Menu fontSiza;

    @FXML
    private Menu format;

    @FXML
    private MenuItem about;

    @FXML
    private MenuItem cursive;

    @FXML
    private MenuItem sans_serif;

    @FXML
    private MenuItem font15;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem font10;

    @FXML
    private MenuItem red;

    @FXML
    private Menu help;

    @FXML
    private Menu file;

    @FXML
    private MenuItem font20;

    @FXML
    private Menu fontFamily;

    @FXML
    private MenuItem serif;

    @FXML
    private MenuItem close;

    @FXML
    private Menu BackgroundColor;
    Alert alert;

    public void setUser(User user) {
        this.user = user;
    }

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

        close.setOnAction(e -> {
            Platform.exit();
        });

        font10.setOnAction(e -> {
            changeFontSize(10);
        });
        font15.setOnAction(e -> {
            changeFontSize(15);
        });
        font20.setOnAction(e -> {
            changeFontSize(20);
        });

        red.setOnAction(e -> {
            changeBackgroundColor(Color.RED);
        });

        green.setOnAction(e -> {
            changeBackgroundColor(Color.GREEN);
        });

        pink.setOnAction(e -> {
            changeBackgroundColor(Color.PINK);
        });

        cursive.setOnAction(e -> {
            changeFontFamily("Cursive");
        });

        sans_serif.setOnAction(e -> {
            changeFontFamily("SansSerif");
        });

        serif.setOnAction(e -> {
            changeFontFamily("Serif");
        });

        about.setOnAction(e -> {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About us");
            alert.setHeaderText("Order Desktop System");
            alert.setContentText("is a comprehensive desktop application designed to streamline and simplify order management processes. With its user-friendly interface and robust features, the system caters to both clients and administrators, empowering them to effortlessly handle orders, products, and invoices.");
            alert.show();
        });

        binding();
        editAndCommitFromTableView();
    }

    private void binding() {
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tcMobile.setCellValueFactory(new PropertyValueFactory("mobile"));

    }

    @FXML
    private void backBtnHandle(ActionEvent event) {
        goBackBtn(user);
    }

    private void goBackBtn(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ClientDashboard.fxml"));
            loader.setController(new ClientDashboardController());
            Parent root = loader.load();

            ClientDashboardController dashboardController = loader.getController();
            dashboardController.setUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) backBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBtnHandle(ActionEvent event) {
        ArrayList<User> user_info = new ArrayList<>();
        user_info.add(user);

        userInfoTable.getItems().setAll(user_info);
    }

    @FXML
    private void resetBtnHandle(ActionEvent event) {
        userInfoTable.getItems().clear();
    }

    public void editAndCommitFromTableView() {
        tcName.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        tcEmail.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        tcMobile.setCellFactory(TextFieldTableCell.<User>forTableColumn());

        tcName.setOnEditCommit(event -> {
            User editedUser = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedUser.setName(event.getNewValue());
        });
        tcEmail.setOnEditCommit(event -> {
            User editedUser = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedUser.setEmail(event.getNewValue());
        });
        tcMobile.setOnEditCommit(event -> {
            User editedUser = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedUser.setMobile(event.getNewValue());
        });
    }

    @FXML
    private void updateBtnHandle(ActionEvent event) {
        String name = user.getName();
        String email = user.getEmail();
        String mobile = user.getMobile();
        if (isValidInput(name) && isValidEmail(email) && isValidNumericValue(mobile)) {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Are you sure you want to Update your information");
            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            alert.showAndWait().ifPresent(buttonType -> {
                try {
                    String sql = "UPDATE users SET name ='" + name + "', email ='" + email
                            + "', mobile ='" + mobile + "'" + " WHERE id = " + user.getId();
                    int executeUpdate = statement.executeUpdate(sql);
                    showBtnHandle(event);
                } catch (SQLException ex) {
                    Logger.getLogger(ClinetProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

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

    private void changeFontSize(double fontSize) {
        String css = "-fx-font-size: " + fontSize + "px;";
        Node rootNode = menuBar.getScene().getRoot();
        rootNode.setStyle(css);
        rootNode.applyCss();
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.sizeToScene();
    }

    private void changeFontFamily(String fontFamily) {
        String css = String.format("-fx-font-family: '%s';", fontFamily);

        Node rootNode = menuBar.getScene().getRoot();
        rootNode.setStyle(css);
        rootNode.applyCss();

        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.sizeToScene();
    }

    private void changeBackgroundColor(Color color) {
        String css = String.format("-fx-background-color: %s;", toRgbString(color));

        Node rootNode = menuBar.getScene().getRoot();
        rootNode.setStyle(css);
        rootNode.applyCss();
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.sizeToScene();
    }

    private String toRgbString(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return String.format("rgb(%d, %d, %d)", red, green, blue);
    }

}
