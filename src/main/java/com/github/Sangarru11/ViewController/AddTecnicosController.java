package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.ProveedorDAO;
import com.github.Sangarru11.Model.DAO.TecnicoDAO;
import com.github.Sangarru11.Model.Entity.Proveedor;
import com.github.Sangarru11.Model.Entity.Tecnico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTecnicosController extends Controller implements Initializable {

    @FXML
    private TextArea txtNombreTecnico;
    @FXML
    private TextArea txtApellidosTecnico;
    @FXML
    private TextArea txtDniTecnico;
    @FXML
    private TextArea txtTelefonoTecnico;
    @FXML
    private TextArea txtEmailTecnico;

    private TecnicosController tecnicosController;

    public void setTecnicosController(TecnicosController tecnicoController) {
        this.tecnicosController = tecnicoController;
    }

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
    private void addTecnico() {
        String nombreTecnico = txtNombreTecnico.getText().trim();
        String apellidosTecnico = txtApellidosTecnico.getText().trim();
        String dniTecnico = txtDniTecnico.getText().trim();
        String telefono = txtTelefonoTecnico.getText().trim();
        String email = txtEmailTecnico.getText().trim();

        if (nombreTecnico.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campo Requerido", "El nombre del tecnico es obligatorio.");
            return;
        }

        if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato Inválido", "El formato del correo electrónico no es válido.");
            return;
        }
        if (!telefono.isEmpty() && !telefono.matches("\\d{9,15}")) {
             mostrarAlerta(Alert.AlertType.ERROR, "Formato Inválido", "El formato del teléfono no es válido (debe tener entre 9 y 15 dígitos).");
             return;
        }


        Tecnico nuevoTecnico = new Tecnico();
        nuevoTecnico.setNombre(nombreTecnico);
        nuevoTecnico.setApellidos(apellidosTecnico.isEmpty() ? null : apellidosTecnico);
        nuevoTecnico.setTelefono(telefono.isEmpty() ? null : telefono);
        nuevoTecnico.setDni(dniTecnico.isEmpty() ? null : dniTecnico);
        nuevoTecnico.setEmail(email.isEmpty() ? null : email);

        TecnicoDAO tecnicoDAO = TecnicoDAO.build();
        if (tecnicoDAO.insert(nuevoTecnico)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Tecnico añadido correctamente.");
            if (tecnicosController != null) {
                tecnicosController.recargarProveedores();
            }
            closeWindow();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo añadir el tecnico a la base de datos.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtNombreTecnico.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}