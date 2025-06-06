package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.ProveedorDAO;
import com.github.Sangarru11.Model.Entity.Proveedor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProveedorController extends Controller implements Initializable {

    @FXML
    private TextArea txtNombreEmpresa;
    @FXML
    private TextArea txtNombreContacto;
    @FXML
    private TextArea txtTelefono;
    @FXML
    private TextArea txtEmail;

    private ProveedoresController proveedoresController;

    public void setProveedoresController(ProveedoresController proveedoresController) {
        this.proveedoresController = proveedoresController;
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
    private void addProveedor() {
        String nombreEmpresa = txtNombreEmpresa.getText().trim();
        String nombreContacto = txtNombreContacto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombreEmpresa.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campo Requerido", "El nombre de la empresa es obligatorio.");
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


        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setNombreEmpresa(nombreEmpresa);
        nuevoProveedor.setContactoNombre(nombreContacto.isEmpty() ? null : nombreContacto);
        nuevoProveedor.setTelefono(telefono.isEmpty() ? null : telefono);
        nuevoProveedor.setEmail(email.isEmpty() ? null : email);

        ProveedorDAO proveedorDAO = ProveedorDAO.build();
        if (proveedorDAO.insert(nuevoProveedor)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Proveedor añadido correctamente.");
            if (proveedoresController != null) {
                proveedoresController.recargarProveedores();
            }
            closeWindow();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo añadir el proveedor a la base de datos.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtNombreEmpresa.getScene().getWindow();
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