package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.UsuarioDAO;
import com.github.Sangarru11.Model.Entity.Usuario;
import com.github.Sangarru11.Utils.HashPassword;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterPageController extends Controller implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
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

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'|,.<>/?]).{8,}$");
    }

    @FXML
    public void Register() throws IOException {
        Usuario newUser = new Usuario();

        String username = txtUsername.getText();
        newUser.setNombreUsuario(username);
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Completa todos los campos.");
            alert.show();
            return;
        }

        if (!isPasswordValid(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("La contraseña debe tener al menos 8 caracteres, una mayúscula y un carácter especial.");
            alert.show();
            return;
        }

        newUser.setPasswordHash(HashPassword.hash(password));
        newUser.setRol("Técnico");

        Usuario user = UsuarioDAO.build().findByName(username);
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("El usuario ya existe.");
            alert.show();
        } else {
            UsuarioDAO.build().save(newUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("El empleado se ha añadido correctamente");
            alert.show();
            WelcomePageController.changeScene(Scenes.WelcomePage, null);
        }
    }

    @FXML
    private void goToWelcome() throws IOException {
        WelcomePageController.changeScene(Scenes.WelcomePage, null);
    }
}
