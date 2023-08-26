/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import Database.DatabaseConnection;
import Entity.Invoices;
import Entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author VICTUS
 */
public class ViewInvicesController implements Initializable {

    @FXML
    private TableView<Invoices> invoicesTable;
    @FXML
    private TableColumn<Invoices, Integer> tcId;
    @FXML
    private TableColumn<Invoices, Integer> tcOrderId;
    @FXML
    private TableColumn<Invoices, Double> tcTotalPrice;
    @FXML
    private TableColumn<Invoices, String> tcDate;
    @FXML
    private Button backBtn;

    @FXML
    private PasswordField oldPassTF;
    @FXML
    private PasswordField newPassTF;
    @FXML
    private PasswordField confirmPassTF;

    @FXML
    private Button changeBtn;
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

    @FXML
    private Button showBtn;
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
    }

    private void binding() {
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcOrderId.setCellValueFactory(new PropertyValueFactory("order_id"));
        tcTotalPrice.setCellValueFactory(new PropertyValueFactory("total_price"));
        tcDate.setCellValueFactory(new PropertyValueFactory("date"));
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
        List<Invoices> invoices = new ArrayList<>();

        try {
            ResultSet rs = statement.executeQuery("SELECT invoices.id, invoices.order_id, invoices.total_price, invoices.date FROM users join orders on users.id = orders.user_id join invoices on invoices.order_id = orders.id WHERE users.id =" + user.getId());
            while (rs.next()) {
                Invoices invoice = new Invoices(rs.getInt("id"), rs.getInt("order_id"), rs.getDouble("total_price"), rs.getString("date"));
                invoices.add(invoice);
            }
            invoicesTable.getItems().setAll(invoices);

        } catch (SQLException ex) {
            alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }

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
