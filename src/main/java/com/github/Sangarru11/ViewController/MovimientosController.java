package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.CategoriaDAO;
import com.github.Sangarru11.Model.DAO.MovimientoDAO;
import com.github.Sangarru11.Model.Entity.Categoria;
import com.github.Sangarru11.Model.Entity.Movimiento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovimientosController extends Controller implements Initializable {
    @FXML
    private TableView<Movimiento> tableView;
    @FXML
    private TableColumn<Movimiento, String> IdMovimiento;
    @FXML
    private TableColumn<Movimiento, String> txtUsuarioResponsable;
    @FXML
    private TableColumn<Movimiento, String> txtTipoMovimiento;
    @FXML
    private TableColumn<Movimiento, String> txtFechaMovimiento;
    @FXML
    private TableColumn<Movimiento, String> txtObservaciones;
    private ObservableList<Movimiento> movimientos;
    @Override
    public Object onOpen(Object input) throws IOException {
        List<Movimiento> movimientoList = MovimientoDAO.build().findAll();
        this.movimientos = FXCollections.observableArrayList(movimientoList);
        tableView.setItems(this.movimientos);
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IdMovimiento.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getId() != null ? cellData.getValue().getId().toString() : ""
            )
        );
        txtUsuarioResponsable.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getIdUsuario() != null ? cellData.getValue().getIdUsuario().getNombreUsuario() : ""
            )
        );
        txtTipoMovimiento.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTipo() != null ? cellData.getValue().getTipo() : ""
            )
        );
        txtFechaMovimiento.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFecha() != null ? cellData.getValue().getFecha().toString() : ""
            )
        );
        txtObservaciones.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getObservaciones() != null ? cellData.getValue().getObservaciones() : ""
            )
        );
        tableView.setRowFactory(tv -> {
            TableRow<Movimiento> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Movimiento movimiento = row.getItem();
                    mostrarDetallesMovimiento(movimiento);
                }
            });
            return row;
        });
    }
    private void mostrarDetallesMovimiento(Movimiento movimiento) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/github/Sangarru11/View/DetalleMovimientos.fxml"));
            javafx.scene.Parent root = loader.load();

            DetalleMovimientoController controller = loader.getController();
            controller.setMovimiento(movimiento);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Detalle del Movimiento");
            stage.setScene(new javafx.scene.Scene(root));
            stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/github/Sangarru11/View/Images/NetStockLogo1.png")));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void GoToCategorias() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.Categorias, null);
    }
    @FXML
    private void GoToCreateMovimiento() throws IOException {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/github/Sangarru11/View/AddMovimiento.fxml"));
        javafx.scene.Parent root = loader.load();

        AddMovimientosController controller = loader.getController();
        controller.setMovimientosController(this);

        javafx.stage.Stage stage = new javafx.stage.Stage();
        stage.setTitle("Nuevo Movimiento");
        stage.setScene(new javafx.scene.Scene(root));
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/github/Sangarru11/View/Images/NetStockLogo1.png")));
        stage.initOwner(tableView.getScene().getWindow());
        stage.showAndWait();
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

    public void recargarMovimientos() {
        List<Movimiento> movimientoList = MovimientoDAO.build().findAll();
        this.movimientos.setAll(movimientoList);
    }
}
