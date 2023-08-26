/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Controller;

import Auth.Controller.LoginController;
import Database.DatabaseConnection;
import Entity.User;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class AdminDashboardController implements Initializable {

    @FXML
    private Button manageProductsBtn;
    @FXML
    private Button manageOrdersBtn;
    @FXML
    private Button manageClientsBtn;
    @FXML
    private Button manageInvoicesBtn;
    @FXML
    private Button changePasswordBtn;
    Statement statement;
    @FXML
    private Button logoutBtn;
    private User user;

    @FXML
    private Label numOfProducts;

    @FXML
    private Label numOfClients;
    @FXML
    private Label numOfOrders;
    @FXML
    private Label totalInvoices;

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection connection = DatabaseConnection.get_connection();
            try {
                statement = connection.createStatement();
            } catch (SQLException ex) {
                System.out.println("Connection failed");
            }

            ResultSet productRS = statement.executeQuery("SELECT count(*) FROM products");
            if (productRS.next()) {
                int count = productRS.getInt(1);
                numOfProducts.setText(count + "");
            }

            ResultSet orderRS = statement.executeQuery("SELECT count(*) FROM orders");
            if (orderRS.next()) {
                int count = orderRS.getInt(1);
                numOfOrders.setText(count + "");
            }

            ResultSet clientRS = statement.executeQuery("SELECT count(*) FROM users");
            if (clientRS.next()) {
                int count = clientRS.getInt(1);
                numOfClients.setText(count + "");
            }
            ResultSet totalRS = statement.executeQuery("SELECT SUM(total_price) FROM invoices");
            if (totalRS.next()) {
                double count = totalRS.getDouble(1);
                totalInvoices.setText(count + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void manageProductsBtnHandle(ActionEvent event) {
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
    private void manageOrdersBtnHandle(ActionEvent event) {
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
    private void manageClientsBtnHandle(ActionEvent event) {
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
    private void manageInvoicesBtnHandle(ActionEvent event) {
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
    private void changePasswordBtnHandle(ActionEvent event) {
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
    private void logoutBtnHandle(ActionEvent event) {
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
