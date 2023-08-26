/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Auth.Controller.LoginController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Auth/views/Login.fxml"));
            loader.setController(new LoginController());
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Login");
            primaryStage.show();

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
