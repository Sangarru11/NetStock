package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.ProductoDAO;
import com.github.Sangarru11.Model.Entity.Producto;
import com.github.Sangarru11.Model.Entity.ProductoZona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.scene.control.TableCell;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController extends Controller implements Initializable {
    @FXML
    private TableView<Producto> tableView;
    @FXML
    private TableColumn<Producto, Image> IdProducto;
    @FXML
    private TableColumn<Producto, String> NombreProducto;
    @FXML
    private TableColumn<Producto, String> DescripcionProducto;
    @FXML
    private TableColumn<Producto, String> CategoriaProducto;
    @FXML
    private TableColumn<Producto, String> CantidadProducto;
    @FXML
    private TableColumn<Producto, String> ZonaProducto;
    @FXML
    private TableColumn<Producto, String> CodigoInternoProducto;

    private ObservableList<Producto> productos;

    @Override
    public Object onOpen(Object input) throws IOException {
        List<Producto> productoList = ProductoDAO.build().findAll();
        this.productos = FXCollections.observableArrayList(productoList);
        tableView.setItems(this.productos);
        return null;
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        IdProducto.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getFoto() != null) {
                Image img = new Image(new java.io.ByteArrayInputStream(producto.getFoto()));
                return new javafx.beans.property.SimpleObjectProperty<>(img);
            }
            return new javafx.beans.property.SimpleObjectProperty<>(null);
        });
        IdProducto.setCellFactory(col -> new TableCell<Producto, Image>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });

        NombreProducto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre())
        );
        NombreProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        NombreProducto.setOnEditCommit(event -> {
            Producto producto = event.getRowValue();
            producto.setNombre(event.getNewValue());
        });

        DescripcionProducto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion())
        );
        DescripcionProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        DescripcionProducto.setOnEditCommit(event -> {
            Producto producto = event.getRowValue();
            producto.setDescripcion(event.getNewValue());
        });

        CategoriaProducto.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getIdCategoria() != null) {
                return new javafx.beans.property.SimpleStringProperty(producto.getIdCategoria().getNombreCategoria());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        CategoriaProducto.setCellFactory(TextFieldTableCell.forTableColumn());

        CantidadProducto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCantidadDisponible() != null ? cellData.getValue().getCantidadDisponible().toString() : ""
                )
        );
        CantidadProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        CantidadProducto.setOnEditCommit(event -> {
            Producto producto = event.getRowValue();
            try {
                producto.setCantidadDisponible(Integer.parseInt(event.getNewValue()));
            } catch (NumberFormatException e) {
                // Ignorar error de conversión
            }
        });

        ZonaProducto.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getProductoZonas() != null && !producto.getProductoZonas().isEmpty()) {
                ProductoZona productoZona = producto.getProductoZonas().iterator().next();
                if (productoZona.getIdZona() != null) {
                    return new javafx.beans.property.SimpleStringProperty(productoZona.getIdZona().getNombreZona());
                }
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        ZonaProducto.setCellFactory(TextFieldTableCell.forTableColumn());

        CodigoInternoProducto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCodigoInterno())
        );
        CodigoInternoProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        CodigoInternoProducto.setOnEditCommit(event -> {
            Producto producto = event.getRowValue();
            producto.setCodigoInterno(event.getNewValue());
        });
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
    private void GoToCategorias() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.Categorias, null);
    }
    @FXML
    private void GoToZonas() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.Zonas, null);
    }
    @FXML
    private void GoToLogin() throws IOException{
        WelcomePageController.changeSceneToMain(Scenes.WelcomePage, null);
    }
    @FXML
    private void GoToTecnicos() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Tecnicos, null);
    }

    @FXML
    private void GoToProveedores() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Proveedores, null);
    }



}