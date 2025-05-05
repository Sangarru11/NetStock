package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.App;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public Object onOpen(Object input) throws IOException {

        return input;
    }

    @Override
    public void onClose(Object output) {

    }

    /**
     * Método para cargar una vista de FXML.
     * @param scenes La escena que se cargará.
     * @return Un objeto View que contiene la escena y su controlador.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public static View loadFXML(Scenes scenes) throws IOException {
        String url = scenes.getURL();
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }

    /**
     * Método para cambiar la escena actual.
     * @param scene La nueva escena a la que se cambiará.
     * @param data Los datos que se pasarán a la nueva escena.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public static void changeScene(Scenes scene, Object data) throws IOException {
        View view = MainController.loadFXML(scene);
        Scene _scene = new Scene(view.scene, 1075, 720);
        App.currentController = view.controller;
        App.currentController.onOpen(data);
        App.stage.setScene(_scene);
        App.stage.show();
    }

}
