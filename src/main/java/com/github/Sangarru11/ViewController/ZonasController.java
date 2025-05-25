package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.CategoriaDAO;
import com.github.Sangarru11.Model.DAO.ZonaDAO;
import com.github.Sangarru11.Model.Entity.Categoria;
import com.github.Sangarru11.Model.Entity.Zona;
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

public class ZonasController extends Controller implements Initializable {
    @FXML
    private TableView<Zona> tableView;
    @FXML
    private TableColumn<Zona, String> IdZona;
    @FXML
    private TableColumn<Zona, String> NombreZona;
    @FXML
    private TableColumn<Zona, String> DescripcionZona;
    private ObservableList<Zona> Zonas;
    @Override
    public Object onOpen(Object input) throws IOException {
        List<Zona> zonaList = ZonaDAO.build().findAll();
        this.Zonas = FXCollections.observableArrayList(zonaList);
        tableView.setItems(this.Zonas);
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IdZona.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getId() != null ? cellData.getValue().getId().toString() : ""
            )
        );
        NombreZona.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreZona())
        );
        DescripcionZona.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion())
        );
    }
    @FXML
    private void GoToAddZona() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Sangarru11/View/AddZona.fxml"));
        Parent root = loader.load();

        AddZonaController addZonaController = loader.getController();
        addZonaController.setZonasController(this);

        Stage stage = new Stage();
        stage.setTitle("Añadir Zona");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/Sangarru11/View/Images/NetStockLogo1.png")));
        stage.initOwner(tableView.getScene().getWindow());
        stage.showAndWait();
    }

    public void recargarZonas() {
        List<Zona> zonaList = ZonaDAO.build().findAll();
        this.Zonas.setAll(zonaList);
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
}
