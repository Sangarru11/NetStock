package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.CategoriaDAO;
import com.github.Sangarru11.Model.DAO.ProductoDAO;
import com.github.Sangarru11.Model.Entity.Categoria;
import com.github.Sangarru11.Model.Entity.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriasController extends Controller implements Initializable {
    @FXML
    private TableView<Categoria> tableView;
    @FXML
    private TableColumn<Categoria, String> IdCategoria;
    @FXML
    private TableColumn<Categoria, String> NombreCategoria;
    private ObservableList<Categoria> categorias;
    @Override
    public Object onOpen(Object input) throws IOException {
        List<Categoria> categoriaList = CategoriaDAO.build().findAll();
        this.categorias = FXCollections.observableArrayList(categoriaList);
        tableView.setItems(this.categorias);
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IdCategoria.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getId() != null ? cellData.getValue().getId().toString() : ""
            )
        );
        NombreCategoria.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreCategoria())
        );
    }
    @FXML
    private void GoToCategorias() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.Categorias, null);
    }
    @FXML
    private void GoToMain() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.MainPage, null);
    }
    @FXML
    private void GoToGStock() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.GStock, null);
    }
    @FXML
    private void GoToAddStock() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.AddStock, null);
    }
    @FXML
    private void GoToMovimientos() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.Movimientos, null);
    }
    @FXML
    private void GoToZonas() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.Zonas, null);
    }
    @FXML
    private void GoToTecnicos() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Tecnicos, null);
    }

    @FXML
    private void GoToProveedores() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Proveedores, null);
    }
    @FXML
    private void GoToAddCategoria() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Sangarru11/View/AddCategoria.fxml"));
        Parent root = loader.load();

        AddCategoriaController addCategoriaController = loader.getController();
        addCategoriaController.setCategoriasController(this); // Pasa la referencia

        Stage stage = new Stage();
        stage.setTitle("Añadir Categoría");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/Sangarru11/View/Images/NetStockLogo1.png")));
        stage.initOwner(tableView.getScene().getWindow());
        stage.showAndWait();
    }

    public void recargarCategorias() {
        List<Categoria> categoriaList = CategoriaDAO.build().findAll();
        this.categorias.setAll(categoriaList);
    }
}
