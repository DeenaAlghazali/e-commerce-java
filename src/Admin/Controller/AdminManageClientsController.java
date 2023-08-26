/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Controller;

import Auth.Controller.LoginController;
import Database.DatabaseConnection;
import Entity.User;
import Client.Controller.ManageOrdersController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class AdminManageClientsController implements Initializable {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> tcId;
    @FXML
    private TableColumn<User, String> tcName;
    @FXML
    private TableColumn<User, String> tcMobile;
    @FXML
    private TableColumn<User, String> tcPassword;
    @FXML
    private Button searchByNameBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button showBtn;
    @FXML
    private Button deleteBtn;
    Statement statement;
    @FXML
    private TableColumn<User, String> tcEmail;
    @FXML
    private TextField searchTF;

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

    Alert alert;

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
        binding();
    }

    private void binding() {
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcMobile.setCellValueFactory(new PropertyValueFactory("mobile"));
        tcPassword.setCellValueFactory(new PropertyValueFactory("password"));
        tcEmail.setCellValueFactory(new PropertyValueFactory("email"));
    }

    @FXML
    private void searchByNameBtnHandle(ActionEvent event) {
        String search = searchTF.getText();
        List<User> users = new ArrayList<>();

        if (isValidInput(search)) {
            try {
                ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE name ='" + search + "'");
                while (rs.next()) {
                    User order = new User(rs.getInt("id"), rs.getInt("role"), rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("password"));
                    users.add(order);
                }
                usersTable.getItems().setAll(users);
            } catch (SQLException ex) {
                Logger.getLogger(AdminManageClientsController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    private void showBtnHandle(ActionEvent event) {
        List<User> users = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE role = 1");
            while (rs.next()) {
                User order = new User(rs.getInt("id"), rs.getInt("role"), rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("password"));
                users.add(order);
            }

            usersTable.getItems().setAll(users);

        } catch (SQLException ex) {
            Logger.getLogger(ManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void deleteBtnHandle(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to delete the user and all of their Orders?");
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        alert.showAndWait().ifPresent(buttonType -> {

            if (buttonType == buttonTypeOK) {
                try {
                    User selectedRow = usersTable.getSelectionModel().getSelectedItem();
                    statement.executeUpdate("DELETE FROM orders WHERE user_id = " + selectedRow.getId());
                    int affected = statement.executeUpdate("DELETE FROM users WHERE id = " + selectedRow.getId());
                    if (affected == 1) {
                        alert = new Alert(Alert.AlertType.INFORMATION, "User Deleted Successfully", ButtonType.OK);
                        alert.show();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminManageClientsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        showBtnHandle(event);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminChangePassword.fxml"));
            loader.setController(new AdminChangePasswordController());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) changePasswordBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
