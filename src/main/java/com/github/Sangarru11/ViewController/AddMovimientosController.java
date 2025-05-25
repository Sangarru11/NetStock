package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.MovimientoDAO;
import com.github.Sangarru11.Model.Entity.Movimiento;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;

public class AddMovimientosController extends Controller implements Initializable {

    @FXML
    private TextArea txtNombreCategoria;
    @FXML
    private TextArea txtNombreCategoria1;
    @FXML
    private TextArea txtNombreCategoria2;

    private MovimientosController movimientosController;

    public void setMovimientosController(MovimientosController controller) {
        this.movimientosController = controller;
    }

    @Override
    public Object onOpen(Object input) throws IOException {
        return null;
    }

    @Override
    public void onClose(Object output) {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    public void addMovimiento() {
        String tipo = txtNombreCategoria.getText().trim();
        String observaciones = txtNombreCategoria1.getText().trim();
        String destinatario = txtNombreCategoria2.getText().trim();

        if (tipo.isEmpty() || destinatario.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("El tipo y el destinatario no pueden estar vacíos.");
            alert.show();
            return;
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(tipo);
        movimiento.setObservaciones(observaciones);
        movimiento.setDestinatario(destinatario);
        movimiento.setFecha(Instant.now());

        MovimientoDAO.build().insert(movimiento);

        if (movimientosController != null) {
            movimientosController.recargarMovimientos();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Movimiento añadido correctamente.");
        alert.show();

        txtNombreCategoria.clear();
        txtNombreCategoria1.clear();
        txtNombreCategoria2.clear();
    }
}