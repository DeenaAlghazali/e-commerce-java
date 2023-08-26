/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Controller;

import Auth.Controller.LoginController;
import Database.DatabaseConnection;
import Entity.Invoices;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class AdminManageInvoicesController implements Initializable {

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
    @FXML
    private Button logoutBtn;
    @FXML
    private TableView<Invoices> invoicesTable;
    @FXML
    private TableColumn<Invoices, Integer> tcId;
    @FXML
    private TableColumn<Invoices, Integer> tcOrderId;
    @FXML
    private TableColumn<Invoices, Double> tcTotalPrice;
    @FXML
    private TableColumn<Invoices, String> tcData;
    @FXML
    private Button BackBtn;
    @FXML
    private Button showBtn;
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
        binding();
    }

    private void binding() {
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcOrderId.setCellValueFactory(new PropertyValueFactory("order_id"));
        tcTotalPrice.setCellValueFactory(new PropertyValueFactory("total_price"));
        tcData.setCellValueFactory(new PropertyValueFactory("date"));
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

    @FXML
    private void BackBtnHandle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminDashboard.fxml"));
            loader.setController(new AdminDashboardController());
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) BackBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBtnHandle(ActionEvent event) {

        List<Invoices> invoices = new ArrayList<>();

        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM invoices");

            while (rs.next()) {
                Invoices order = new Invoices(rs.getInt("id"), rs.getInt("order_id"), rs.getDouble("total_price"), rs.getString("date"));
                invoices.add(order);
            }
            invoicesTable.getItems().setAll(invoices);

        } catch (SQLException ex) {
            Logger.getLogger(ManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
