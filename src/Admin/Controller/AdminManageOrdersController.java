/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Controller;

import Auth.Controller.LoginController;
import Database.DatabaseConnection;
import Entity.Order;
import Client.Controller.ManageOrdersController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
public class AdminManageOrdersController implements Initializable {

    @FXML
    private ComboBox<Integer> productIdCombo;
    @FXML
    private ComboBox<Integer> userIdCombo;
    @FXML
    private TextField quantityTF;
    @FXML
    private DatePicker dateTF;
    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> tcId;
    @FXML
    private TableColumn<Order, Integer> tcProductId;
    @FXML
    private TableColumn<Order, Integer> tcUserId;
    @FXML
    private TableColumn<Order, Integer> tcQuantity;
    @FXML
    private TableColumn<Order, String> tcDate;
    private TextField searchTF;
    @FXML
    private Button backBtn;
    @FXML
    private Button showBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button searchByUserIdBtn;
    Statement statement;
    @FXML
    private ComboBox<Integer> searchCombo;

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
        try {
            Connection connection = DatabaseConnection.get_connection();
            try {
                statement = connection.createStatement();
            } catch (SQLException ex) {
                System.out.println("Connection failed");
            }
            Set<Integer> usersId = new TreeSet<>();
            ResultSet rs = statement.executeQuery("SELECT id FROM users");
            while (rs.next()) {
                usersId.add(rs.getInt("id"));
            }
            userIdCombo.getItems().setAll(usersId);
            searchCombo.getItems().setAll(usersId);
            Set<Integer> productsId = new TreeSet<>();
            ResultSet productsRs = statement.executeQuery("SELECT id FROM products");
            while (productsRs.next()) {
                productsId.add(productsRs.getInt("id"));
            }
            productIdCombo.getItems().setAll(productsId);
            binding();
        } catch (SQLException ex) {
            Logger.getLogger(AdminManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void binding() {
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcProductId.setCellValueFactory(new PropertyValueFactory("product_id"));
        tcUserId.setCellValueFactory(new PropertyValueFactory("user_id"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        tcDate.setCellValueFactory(new PropertyValueFactory("date"));

    }

    @FXML
    private void searchByUserIdBtnHandle(ActionEvent event) {
        List<Order> orders = new ArrayList<>();
        Integer userId = searchCombo.getValue();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE user_id = '" + userId + "'");
            while (rs.next()) {
                Order order = new Order(rs.getInt("id"), rs.getInt("product_id"), rs.getInt("user_id"), rs.getInt("quantity"), rs.getString("date"));
                orders.add(order);
            }
            ordersTable.getItems().setAll(orders);
        } catch (SQLException ex) {
            Logger.getLogger(AdminManageProductsController.class.getName()).log(Level.SEVERE, null, ex);
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
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM orders");
            while (rs.next()) {
                Order order = new Order(rs.getInt("id"), rs.getInt("product_id"), rs.getInt("user_id"), rs.getInt("quantity"), rs.getString("date"));
                orders.add(order);
            }

            ordersTable.getItems().setAll(orders);

        } catch (SQLException ex) {
            Logger.getLogger(ManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addBtnHandle(ActionEvent event) {
        Integer productId = productIdCombo.getValue();
        Integer userId = userIdCombo.getValue();
        String quantity = quantityTF.getText();
        LocalDate selectedDate = dateTF.getValue();

        if (isValidInput(productId + "") && isValidInput(userId + "") && isValidInput(quantity) && isValidInput(selectedDate + "")) {

            try {
                ResultSet rs = statement.executeQuery("SELECT quantity FROM products WHERE id = " + productId);
                int storedQuantity = 0;
                while (rs.next()) {
                    storedQuantity = rs.getInt("quantity");
                }
                int quantityNum = Integer.parseInt(quantity);
                if (quantityNum < storedQuantity) {
                    int affectedRow = statement.executeUpdate("INSERT INTO orders (product_id, user_id, quantity, date) VALUES (" + productId + "," + userId + "," + quantity + ",'" + selectedDate + "')");
                    if (affectedRow == 1) {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Order added successfully", ButtonType.OK);
                        alert.show();
                        int newQuantity = storedQuantity - quantityNum;
                        if (newQuantity > 0) {

                            statement.executeUpdate("UPDATE products SET quantity = " + newQuantity + " WHERE id = " + productId);
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR, "You must add available quantity", ButtonType.OK);
                            alert.show();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR, "Order added Failed", ButtonType.OK);
                        alert.show();
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "You must add available quantity", ButtonType.OK);
                    alert.show();
                }

            } catch (SQLException ex) {
                alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.show();
            }
        }
        showBtnHandle(event);
        clear_data();
    }

    private void clear_data() {
        userIdCombo.setValue(null);
        productIdCombo.setValue(null);
        quantityTF.clear();
        dateTF.setValue(null);
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
