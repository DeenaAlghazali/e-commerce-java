/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import Client.Controller.ClientDashboardController;
import Database.DatabaseConnection;
import Entity.Order;
import Entity.Product;
import Entity.User;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class ManageOrdersController implements Initializable {

    @FXML
    private TextField productIdTF;
    @FXML
    private TextField quantityTF;
    @FXML
    private DatePicker dateTF;
    @FXML
    private TableView<Product> productsTable;
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
    private Button backBtn;
    @FXML
    private Button showBtn;
    @FXML
    private Button addOrderBtn;
    @FXML
    private Button resetBtn;
    Statement statement;
    Alert alert;

    private User user;
    @FXML
    private TableView<Order> OrderTable;
    @FXML
    private TableColumn<Order, Integer> tcOrderId;
    @FXML
    private TableColumn<Order, Integer> tcOrderQuantity;
    @FXML
    private TableColumn<Order, String> tcOrderDate;
    @FXML
    private Button viewOrderBtn;
    @FXML
    private Button editOrderBtn;
    @FXML
    private Button searchByIdBtn;
    @FXML
    private TextField searchTF;
    @FXML
    private TableColumn<Order, Integer> tcOrderProductId;
    @FXML
    private Button deleteOrderBtn;
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

        bindingProductTable();
        bindingOrderTable();
        productsTable.getSelectionModel().selectedIndexProperty().addListener(e -> {
            showSelectedRow();
        });
        editAndCommitFromTableView();
    }

    public void editAndCommitFromTableView() {
        tcOrderProductId.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcOrderQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcOrderDate.setCellFactory(TextFieldTableCell.<Order>forTableColumn());
        tcOrderProductId.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setProduct_id(event.getNewValue());
        });

        tcOrderQuantity.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
        });

        tcOrderDate.setOnEditCommit(event -> {
            Order editedOrder = event.getTableView().getItems().get(event.getTablePosition().getRow());
            editedOrder.setDate(event.getNewValue());
        });

    }

    public void bindingProductTable() {
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcCategory.setCellValueFactory(new PropertyValueFactory("category"));
        tcPrice.setCellValueFactory(new PropertyValueFactory("price"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));

    }

    public void bindingOrderTable() {
        tcOrderId.setCellValueFactory(new PropertyValueFactory("id"));
        tcOrderProductId.setCellValueFactory(new PropertyValueFactory("Product_id"));
        tcOrderQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        tcOrderDate.setCellValueFactory(new PropertyValueFactory("date"));
    }

    public void showSelectedRow() {
        Product productSelected = productsTable.getSelectionModel().getSelectedItem();
        if (productSelected != null) {
            productIdTF.setText(productSelected.getId() + "");
        }
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
        List<Product> products = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM products");
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getInt("quantity"), rs.getString("name"), rs.getString("category"), rs.getString("description"), rs.getDouble("price"));
                products.add(product);
            }
            productsTable.getItems().setAll(products);

        } catch (SQLException ex) {
            Logger.getLogger(ManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addOrderBtnHandle(ActionEvent event) {
        Product productSelected = productsTable.getSelectionModel().getSelectedItem();
        String quantity = quantityTF.getText();
        LocalDate selectedDate = dateTF.getValue();
        if (productSelected != null && isValidNumericValue(quantity) && (selectedDate != null)) {
            int storedQuantity = productSelected.getQuantity();
            int quantityNum = Integer.parseInt(quantity);
            if (quantityNum < storedQuantity) {
                try {
                    int newQuantity = storedQuantity - quantityNum;
                    if (newQuantity > 0) {
                        int affectedRow = statement.executeUpdate("INSERT INTO orders (product_id, user_id, quantity, date) VALUES (" + productSelected.getId() + "," + user.getId() + "," + quantityNum + ",'" + selectedDate + "')", Statement.RETURN_GENERATED_KEYS);

                        if (affectedRow == 1) {
                            ResultSet generatedKeys = statement.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                int newOrderId = generatedKeys.getInt(1);

                                alert = new Alert(Alert.AlertType.INFORMATION, "Order added successfully", ButtonType.OK);
                                alert.show();

                                statement.executeUpdate("UPDATE products SET quantity = " + newQuantity + " WHERE id =" + productSelected.getId());
                                Double totalPrice = (quantityNum * productSelected.getPrice());
                                statement.executeUpdate("INSERT INTO invoices (order_id, total_price, date) VALUES (" + newOrderId + "," + totalPrice + ",'" + selectedDate + "')");
                            }
                            showBtnHandle(event);
                            viewOrderBtnHandle(event);
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR, "Failed to add order", ButtonType.OK);
                        alert.show();
                    }
                } catch (SQLException ex) {
                    alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                    alert.show();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "You must add available quantity", ButtonType.OK);
                alert.show();
            }
        }
    }

    @FXML
    private void resetBtnHandle(ActionEvent event) {
        clearData();
        productsTable.getItems().clear();
    }

    private void clearData() {
        productIdTF.clear();
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

    @FXML
    private void viewOrderBtnHandle(ActionEvent event) {
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE user_id=" + user.getId());
            while (rs.next()) {
                Order order = new Order(rs.getInt("id"), rs.getInt("product_id"), user.getId(), rs.getInt("quantity"), rs.getString("date"));
                orders.add(order);
            }

            OrderTable.getItems().setAll(orders);

        } catch (SQLException ex) {
            Logger.getLogger(ManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void editOrderBtnHandle(ActionEvent event) {
        try {
            Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();
            Integer orderId = selectedOrder.getId();
            Integer productId = selectedOrder.getProduct_id();
            Integer quantity = selectedOrder.getQuantity();
            String selectedDate = selectedOrder.getDate();
            ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE id =" + orderId + " And user_id = " + user.getId());
            rs.next();
            int oldQuantity = rs.getInt("quantity");

            if (isValidInput(selectedDate) && isValidNumericValue(orderId + "") && isValidNumericValue(productId + "") && isValidNumericValue(quantity + "")) {
//                ResultSet idExistRow = statement.executeQuery("SELECT * FROM products WHERE id IN (SELECT id FROM products) ");
//                if (idExistRow.next()) {
//                    alert = new Alert(Alert.AlertType.ERROR, "Product ID dosen't exist", ButtonType.OK);
//                    alert.show();
//                    viewOrderBtnHandle(event);
//                } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Are you sure you want to Update your information");
                ButtonType buttonTypeOK = new ButtonType("OK");
                ButtonType buttonTypeCancel = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
                alert.showAndWait().ifPresent(buttonType -> {
                    try {
                        int newQuantity = oldQuantity - quantity;
                        if (newQuantity > 0) {
                            String sql = "UPDATE orders SET product_id =" + productId + ", quantity =" + quantity
                                    + ", date ='" + selectedDate + "'" + " WHERE id = " + orderId;
                            int executeUpdate = statement.executeUpdate(sql);

                            int updateProductsQuantity = statement.executeUpdate("UPDATE products SET quantity = quantity + " + newQuantity + " WHERE id = " + productId);
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR, "Invalid Quantity", ButtonType.OK);
                            alert.show();
                        }
                        showBtnHandle(event);
                        viewOrderBtnHandle(event);

                    } catch (SQLException ex) {
                        viewOrderBtnHandle(event);
                        alert = new Alert(Alert.AlertType.ERROR, "Edited Failed", ButtonType.OK);
                        alert.show();
                    }
                });
//                }

            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Edited Failed", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException ex) {
            alert = new Alert(Alert.AlertType.ERROR, "Edited Failed", ButtonType.OK);
            alert.show();
        }

    }

    @FXML
    private void searchByIdBtnHandle(ActionEvent event) {
        String id = searchTF.getText();
        if (isValidNumericValue(id)) {

            try {
                ResultSet rs = statement.executeQuery("SELECT id, product_id, quantity, date FROM orders WHERE user_id = " + user.getId() + " And id = " + id);
                rs.next();
                Order order = new Order(rs.getInt("id"), rs.getInt("product_id"), user.getId(), rs.getInt("quantity"), rs.getString("date"));
                OrderTable.getItems().setAll(order);
            } catch (SQLException ex) {
                alert = new Alert(Alert.AlertType.ERROR, "ID dosen't exist", ButtonType.OK);
                alert.show();
            }
        }
    }

    @FXML
    private void deleteOrderBtnHandle(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to Delete Row");
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        alert.showAndWait().ifPresent(buttonType -> {

            if (buttonType == buttonTypeOK) {
                Order selectedRow = OrderTable.getSelectionModel().getSelectedItem();
                try {
                    ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE id =" + selectedRow.getId() + " And user_id = " + user.getId());
                    rs.next();
                    int oldQuantity = rs.getInt("quantity");

                    statement.executeUpdate("DELETE FROM invoices WHERE order_id = " + selectedRow.getId());
                    int affectedRow = statement.executeUpdate("DELETE FROM orders WHERE user_id = " + user.getId() + " And id = " + selectedRow.getId());
                    if (affectedRow == 1) {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Order deleted successfully", ButtonType.OK);
                        alert.show();

                        int updateProductsQuantity = statement.executeUpdate("UPDATE products SET quantity = quantity + " + oldQuantity + " WHERE id = " + selectedRow.getProduct_id());
                        viewOrderBtnHandle(event);
                        showBtnHandle(event);
                    } else if (buttonType == buttonTypeCancel) {
                        alert.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ManageOrdersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
