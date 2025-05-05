package com.github.Sangarru11;

import com.github.Sangarru11.ViewController.Controller;
import com.github.Sangarru11.ViewController.MainController;
import com.github.Sangarru11.ViewController.Scenes;
import com.github.Sangarru11.ViewController.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static Controller currentController;

    @Override
    public void start(Stage stage) throws IOException {
        View view = MainController.loadFXML(Scenes.WelcomePage);
        scene = new Scene(view.scene, 600, 617);
        currentController = view.controller;
        currentController.onOpen(null);
        App.stage = stage;
        stage.setTitle("Huella Carbono");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/HuellaCarbono/view/images/HuellaCarbonoLogo.PNG")));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}