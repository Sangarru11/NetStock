package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.CategoriaDAO;
import com.github.Sangarru11.Model.Entity.Categoria;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoriaController extends Controller implements Initializable {

    @FXML
    private TextArea txtNombreCategoria;
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
    public void addCategoria() {
        String nombre = txtNombreCategoria.getText();
        if (nombre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("El nombre de la categoría no puede estar vacío.");
            alert.show();
            return;
        }
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombre);
        CategoriaDAO.build().insert(categoria);

        if (categoriasController != null) {
            categoriasController.recargarCategorias();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Categoría añadida correctamente.");
        alert.show();

        txtNombreCategoria.clear();
    }

    private CategoriasController categoriasController;

    public void setCategoriasController(CategoriasController controller) {
        this.categoriasController = controller;
    }
}
