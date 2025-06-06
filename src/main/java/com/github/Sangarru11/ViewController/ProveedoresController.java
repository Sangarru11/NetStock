package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.ProveedorDAO;
import com.github.Sangarru11.Model.Entity.Proveedor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProveedoresController extends Controller implements Initializable {
    @FXML
    private TableView<Proveedor> tableView;
    @FXML
    private TableColumn<Proveedor, String> Id_Proveedor;
    @FXML
    private TableColumn<Proveedor, String> NombreEmpresa;
    @FXML
    private TableColumn<Proveedor, String> NombreContactoEmpresa;
    @FXML
    private TableColumn<Proveedor, String> TelefonoEmpresa;
    @FXML
    private TableColumn<Proveedor, String> CorreoEmpresa;

    private ObservableList<Proveedor> proveedores;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        proveedores = FXCollections.observableArrayList();
        tableView.setItems(proveedores);

        Id_Proveedor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        NombreEmpresa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreEmpresa()));
        NombreContactoEmpresa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContactoNombre()));
        TelefonoEmpresa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        CorreoEmpresa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        cargarProveedores();
    }

    private void cargarProveedores() {
        List<Proveedor> proveedorList = ProveedorDAO.build().findAll();
        proveedores.clear();
        if (proveedorList != null) {
            proveedores.addAll(proveedorList);
        }
        tableView.refresh();
    }

    @Override
    public Object onOpen(Object input) throws IOException {
        cargarProveedores();
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @FXML
    private void GoToMain() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.MainPage, null);
    }

    @FXML
    private void GoToGStock() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.GStock, null);
    }

    @FXML
    private void GoToAddStock() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.AddStock, null);
    }

    @FXML
    private void GoToMovimientos() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Movimientos, null);
    }

    @FXML
    private void GoToCategorias() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Categorias, null);
    }

    @FXML
    private void GoToZonas() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Zonas, null);
    }

    @FXML
    private void GoToTecnicos() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Tecnicos, null);
    }

    @FXML
    private void GoToProveedores() throws IOException {
        cargarProveedores();
    }

    @FXML
    private void GoToAddProveedor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Sangarru11/View/AddProveedor.fxml"));
        Parent root = loader.load();

        AddProveedorController addProveedorController = loader.getController();
        addProveedorController.setProveedoresController(this);

        Stage stage = new Stage();
        stage.setTitle("Añadir Proveedor");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/Sangarru11/View/Images/NetStockLogo1.png")));
        stage.initOwner(tableView.getScene().getWindow());
        stage.showAndWait();
    }

    public void recargarProveedores() {
        cargarProveedores();
    }
}