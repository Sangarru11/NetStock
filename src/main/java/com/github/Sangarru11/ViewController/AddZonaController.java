package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.ZonaDAO;
import com.github.Sangarru11.Model.Entity.Zona;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddZonaController extends Controller implements Initializable {
    @FXML
    private TextArea txtNombreZona;
    @FXML
    private TextArea txtDescripcionZona;

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
    public void addZona() {
        String nombre = txtNombreZona.getText().trim();
        String descripcion = txtDescripcionZona.getText().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("El nombre y la descripción de la zona no pueden estar vacíos.");
            alert.show();
            return;
        }

        Zona zona = new Zona();
        zona.setNombreZona(nombre);
        zona.setDescripcion(descripcion);

        ZonaDAO.build().insert(zona);

        if (zonasController != null) {
            zonasController.recargarZonas();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Zona añadida correctamente.");
        alert.show();

        txtNombreZona.clear();
        txtDescripcionZona.clear();
    }

    private ZonasController zonasController;

    public void setZonasController(ZonasController controller) {
        this.zonasController = controller;
    }
}
