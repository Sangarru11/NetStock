package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.App;
import com.github.Sangarru11.Model.DAO.UsuarioDAO;
import com.github.Sangarru11.Model.Entity.Usuario;
import com.github.Sangarru11.Utils.HashPassword;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomePageController extends Controller implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @Override
    public Object onOpen(Object input) throws IOException {
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void Login() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Completa todos los campos.");
            alert.show();
            return;
        }

        Usuario user = UsuarioDAO.build().findByName(username);
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Usuario no encontrado.");
            alert.show();
            return;
        }

        String hashedInput = HashPassword.hash(password);
        if (!hashedInput.equals(user.getPasswordHash())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Contraseña incorrecta.");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("¡Bienvenido, " + user.getNombreUsuario() + "!");
        alert.show();

        changeSceneToMain(Scenes.MainPage, user);
    }

    @FXML
    private void goToRegister() throws IOException {
        changeScene(Scenes.RegisterPage, null);
    }

    public static void changeScene(Scenes scene, Object data) throws IOException {
        View view = MainController.loadFXML(scene);
        Scene _scene = new Scene(view.scene, 600, 617);
        App.currentController = view.controller;
        App.currentController.onOpen(data);
        App.stage.setScene(_scene);
        App.stage.show();
    }

    public static void changeSceneToMain(Scenes scene, Object data) throws IOException {
        View view = MainController.loadFXML(scene);
        Scene _scene = new Scene(view.scene);
        App.currentController = view.controller;
        App.currentController.onOpen(data);
        App.stage.setScene(_scene);
        App.stage.setMaximized(true);
        App.stage.show();
    }
}
