package com.github.Sangarru11.ViewController;


import com.github.Sangarru11.Model.DAO.TecnicoDAO;
import com.github.Sangarru11.Model.Entity.Tecnico;
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

public class TecnicosController extends Controller implements Initializable {
    @FXML
    private TableView<Tecnico> tableView;
    @FXML
    private TableColumn<Tecnico, String> IdTecnico;
    @FXML
    private TableColumn<Tecnico, String> NombreTecnico;
    @FXML
    private TableColumn<Tecnico, String> DNITecnico;
    @FXML
    private TableColumn<Tecnico, String> ApellidosTecnico;
    @FXML
    private TableColumn<Tecnico, String> TelefonoTecnico;
    @FXML
    private TableColumn<Tecnico, String> EmailTecnico;

    private ObservableList<Tecnico> tecnicos;
    @Override
    public Object onOpen(Object input) throws IOException {
        List<Tecnico> tecnicoList = TecnicoDAO.build().findAll();
        this.tecnicos = FXCollections.observableArrayList(tecnicoList);
        tableView.setItems(this.tecnicos);
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IdTecnico.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getId() != null ? cellData.getValue().getId().toString() : ""
            )
        );
        NombreTecnico.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre() != null ? cellData.getValue().getNombre() : "")
        );
        ApellidosTecnico.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellidos() != null ? cellData.getValue().getApellidos() : "")
        );
        DNITecnico.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDni() != null ? cellData.getValue().getDni() : "")
        );
        TelefonoTecnico.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefono() != null ? cellData.getValue().getTelefono() : "")
        );
        EmailTecnico.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail() != null ? cellData.getValue().getEmail() : "")
        );
    }
    private void cargarTecnicos() {
        List<Tecnico> tecnicoList = TecnicoDAO.build().findAll();
        tecnicos.clear();
        if (tecnicoList != null) {
            tecnicos.addAll(tecnicoList);
        }
        tableView.refresh();
    }
    @FXML
    private void GoToAddTecnico() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Sangarru11/View/AddTecnico.fxml"));
        Parent root = loader.load();
        AddTecnicosController controller = loader.getController();
        controller.setTecnicosController(this);
        Stage stage = new Stage();
        stage.setTitle("Añadir Tecnico");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/Sangarru11/View/Images/NetStockLogo1.png")));
        stage.initOwner(tableView.getScene().getWindow());
        stage.showAndWait();
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
    public void recargarProveedores() {
        cargarTecnicos();
    }
}
