/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Controller;

import Auth.Controller.LoginController;
import Database.DatabaseConnection;
import Entity.Product;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class AdminManageProductsController implements Initializable {

    @FXML
    private TextField nameTF;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private TextField priceTF;
    @FXML
    private TextField quantityTF;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> tcId;
    @FXML
    private TableColumn<Product, String> tcName;
    @FXML
    private TableColumn<Product, String> tcCategory;
    @FXML
    private TableColumn<Product, Double> tcPrice;
    @FXML
    private TableColumn<Product, Integer> tcQuantity;
    @FXML
    private TableColumn<Product, String> tcDescription;
    @FXML
    private Button searchByCategoryBtn;
    @FXML
    private Button BackBtn;
    @FXML
    private Button showBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    Statement statement;
    Alert alert;
    @FXML
    private TextArea descriptionTF;
    @FXML
    private ComboBox<String> searchCategoryCombo;
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
            binding();
            editAndCommitFromTableView();
            Set<String> categories = new TreeSet<>();
            ResultSet rs = statement.executeQuery("SELECT category FROM products");
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            categoryCombo.getItems().setAll(categories);
            searchCategoryCombo.getItems().setAll(categories);
        } catch (SQLException ex) {
            Logger.getLogger(AdminManageProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void binding() {
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcCategory.setCellValueFactory(new PropertyValueFactory("category"));
        tcPrice.setCellValueFactory(new PropertyValueFactory("price"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        tcDescription.setCellValueFactory(new PropertyValueFactory("description"));

    }

    public void editAndCommitFromTableView() {
        tcName.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        tcCategory.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        tcPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tcQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcDescription.setCellFactory(TextFieldTableCell.<Product>forTableColumn());

        tcName.setOnEditCommit(event -> {
            Product editedOrder = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedOrder.setName(event.getNewValue());
        });

        tcCategory.setOnEditCommit(event -> {
            Product editedOrder = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedOrder.setCategory(event.getNewValue());
        });

        tcPrice.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPrice(event.getNewValue());
        });
        tcQuantity.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
        });

        tcDescription.setOnEditCommit(event -> {
            Product editedOrder = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedOrder.setDescription(event.getNewValue());
        });

    }

    @FXML
    private void searchByCategoryBtnHandle(ActionEvent event) {
        List<Product> products = new ArrayList<>();
        String category = searchCategoryCombo.getValue();
        if (category != null) {
            try {
                ResultSet rs = statement.executeQuery("SELECT * FROM products WHERE category = '" + category + "'");
                while (rs.next()) {
                    Product product = new Product(rs.getInt("id"), rs.getInt("quantity"), rs.getString("name"), rs.getString("category"), rs.getString("description"), rs.getDouble("price"));
                    products.add(product);
                }
                productTable.getItems().setAll(products);
            } catch (SQLException ex) {
                Logger.getLogger(AdminManageProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        List<Product> products = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM products");
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getInt("quantity"), rs.getString("name"), rs.getString("category"), rs.getString("description"), rs.getDouble("price"));
                products.add(product);
            }
            productTable.getItems().setAll(products);
        } catch (SQLException ex) {
            Logger.getLogger(AdminManageProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addBtnHandle(ActionEvent event) {
        String name = nameTF.getText();
        String category = categoryCombo.getValue();
        String price = priceTF.getText();
        String quantity = quantityTF.getText();
        String description = descriptionTF.getText();

        if (isValidInput(name) && isValidInput(category) && isValidNumericValue(price) && isValidNumericValue(quantity) && isValidInput(description)) {

            try {

                int affectedRow = statement.executeUpdate("INSERT INTO products (name, category, price, quantity, description) VALUES ('" + name + "','" + category + "'," + price + "," + quantity + ",'" + description + "')");
                if (affectedRow == 1) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Product added successfully", ButtonType.OK);
                    alert.show();
                    clearData();
                    showBtnHandle(event);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Failed to add Product", ButtonType.OK);
                    alert.show();
                }
            } catch (SQLException ex) {
                alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.show();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Failed to add Product", ButtonType.OK);
            alert.show();
        }

    }

    @FXML
    private void editBtnHandle(ActionEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        String name = selectedProduct.getName();
        String category = selectedProduct.getCategory();
        Double price = selectedProduct.getPrice();
        Integer quantity = selectedProduct.getQuantity();
        String description = selectedProduct.getDescription();

        if (isValidInput(name) && isValidInput(category) && isValidInput(price + "") && isValidInput(quantity + "") && isValidInput(description)) {
            try {
                int executeUpdate = statement.executeUpdate("UPDATE products SET name ='" + name + "', category ='" + category + "', price = " + price + ", quantity = " + quantity + ", description ='" + description + "' WHERE id = " + selectedProduct.getId());
                if (executeUpdate == 1) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Product Updated Successfully", ButtonType.OK);
                    alert.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Product Updated Failed", ButtonType.OK);
                    alert.show();
                }

            } catch (SQLException ex) {
                alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.show();
            }
        }
        showBtnHandle(event);

    }

    @FXML
    private void deleteBtnHandle(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to Delete Row");
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        alert.showAndWait().ifPresent(buttonType -> {
            Product selectedRow = productTable.getSelectionModel().getSelectedItem();
            if (buttonType == buttonTypeOK) {
                try {
                    int affectedRow = statement.executeUpdate("DELETE FROM products WHERE id = " + selectedRow.getId());
                    if (affectedRow == 1) {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully", ButtonType.OK);
                        alert.show();
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR, "Product deleted Faild", ButtonType.OK);
                        alert.show();
                    }
                } catch (SQLException ex) {
                    alert = new Alert(Alert.AlertType.ERROR, "You Can't delete this product", ButtonType.OK);
                    alert.show();
                }
            }
            showBtnHandle(event);

        });

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
                alert = new Alert(Alert.AlertType.ERROR, "Please enter numeric value", ButtonType.OK);
                alert.show();
            }
        }
        return false;

    }

    private void clearData() {
        nameTF.clear();
        categoryCombo.getItems().clear();
        priceTF.clear();
        quantityTF.clear();
        descriptionTF.clear();
    }

    @FXML
    void manageProductsBtnHandle(ActionEvent event) {

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
