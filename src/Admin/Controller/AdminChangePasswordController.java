/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Controller;

import Auth.Controller.Hash;
import Auth.Controller.LoginController;
import Database.DatabaseConnection;
import Entity.User;
import Client.Controller.ChangePasswordController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class AdminChangePasswordController implements Initializable {

    @FXML
    private PasswordField oldPassTF;
    @FXML
    private PasswordField newPassTF;
    @FXML
    private PasswordField confirmPassTF;
    @FXML
    private Button backBtn;
    @FXML
    private Button changeBtn;
    @FXML
    private Button manageClientsBtn;
    @FXML
    private Button manageOrdersBtn;
    @FXML
    private Button changePasswordBtn;
    @FXML
    private Button manageProductsBtn;
    @FXML
    private Button manageInvoicesBtn;
    @FXML
    private Button logoutBtn;

    Statement statement;
    private User user;
    Alert alert;

    public void setUser(User user) {
        this.user = user;
    }

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
    private void backBtnHandle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminDashboard.fxml"));
            loader.setController(new AdminDashboardController());
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) backBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeBtnHandle(ActionEvent event) {
        String oldPassword = oldPassTF.getText();
        String newPassword = newPassTF.getText();
        String confirmPassword = confirmPassTF.getText();

        if (isValidPassword(oldPassword) && isValidPassword(newPassword) && isValidPassword(confirmPassword)) {
            try {
                ResultSet rs = statement.executeQuery("SELECT password FROM users WHERE role = 0");
                rs.next();
                String password = rs.getString("password");
                String hashedPassword = Hash.getMd5Hash(oldPassword);

                if (password.equals(hashedPassword)) {

                    if (newPassword.equals(confirmPassword)) {
                        String hashedNewPassword = Hash.getMd5Hash(newPassword);
                        int affectedRows = statement.executeUpdate("UPDATE users SET password ='" + hashedNewPassword + "' WHERE role = 0");
                        if (affectedRows == 1) {
                            alert = new Alert(Alert.AlertType.INFORMATION, "Password Changed successfully", ButtonType.OK);
                            alert.show();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR, "Failed to change password", ButtonType.OK);
                            alert.show();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR, "New password and confirm password do not match", ButtonType.OK);
                        alert.show();;
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Incorrect old password", ButtonType.OK);
                    alert.show();
                }
                clearData();
            } catch (SQLException ex) {
                Logger.getLogger(ChangePasswordController.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private void clearData() {
        oldPassTF.clear();
        newPassTF.clear();
        confirmPassTF.clear();
    }

    @FXML
    void manageProductsBtnHandle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminManageProducts.fxml"));
            loader.setController(new AdminManageProductsController());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) manageProductsBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void manageOrdersBtnHandle(ActionEvent event) {
          try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminManageOrders.fxml"));
            loader.setController(new AdminManageOrdersController());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) manageOrdersBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void manageClientsBtnHandle(ActionEvent event) {
       try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminManageClients.fxml"));
            loader.setController(new AdminManageClientsController());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) manageClientsBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void manageInvoicesBtnHandle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminManageInvoices.fxml"));
            loader.setController(new AdminManageInvoicesController());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) manageInvoicesBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void changePasswordBtnHandle(ActionEvent event) {

    }

    @FXML
    void logoutBtnHandle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Auth/views/Login.fxml"));
            loader.setController(new LoginController());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) logoutBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
