package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.Entity.Movimiento;
import com.github.Sangarru11.Model.Entity.MovimientoProducto;
import com.github.Sangarru11.Model.Entity.Producto;
import com.github.Sangarru11.Model.Entity.ProductoZona;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DetalleMovimientoController extends Controller implements Initializable {
    @FXML
    private Label lblNombreMovimiento;
    @FXML
    private Label lblDestinatario;
    @FXML
    private TableView<Producto> tableView;
    @FXML
    private TableColumn<Producto, Image> imageTableColumn;
    @FXML
    private TableColumn<Producto, String> NombreTableColumn;
    @FXML
    private TableColumn<Producto, String> DescripcionTableColumn;
    @FXML
    private TableColumn<Producto, String> CategoriaTableColumn;
    @FXML
    private TableColumn<Producto, String> CantidadTableColumn;
    @FXML
    private TableColumn<Producto, String> ZonaTableColumn;
    @FXML
    private TableColumn<Producto, String> CodigoInternoTableColumn;

    private Movimiento movimiento;

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
        mostrarDatos();
    }

    private void mostrarDatos() {
        if (movimiento != null && lblNombreMovimiento != null && lblDestinatario != null) {
            lblNombreMovimiento.setText(movimiento.getTipo());
            lblDestinatario.setText(movimiento.getDestinatario() != null ? movimiento.getDestinatario() : "");
            ObservableList<Producto> productos = FXCollections.observableArrayList(
                movimiento.getMovimientoProductos().stream()
                    .map(MovimientoProducto::getIdProducto)
                    .collect(Collectors.toList())
            );
            tableView.setItems(productos);
        } else if (movimiento == null && tableView != null) {
            tableView.setItems(FXCollections.observableArrayList());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageTableColumn.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getFoto() != null) {
                Image img = new Image(new ByteArrayInputStream(producto.getFoto()));
                return new SimpleObjectProperty<>(img);
            }
            return new SimpleObjectProperty<>(null);
        });
        imageTableColumn.setCellFactory(col -> new TableCell<Producto, Image>() {
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

        NombreTableColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNombre())
        );
        DescripcionTableColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDescripcion())
        );
        CategoriaTableColumn.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getIdCategoria() != null) {
                return new SimpleStringProperty(producto.getIdCategoria().getNombreCategoria());
            } else {
                return new SimpleStringProperty("");
            }
        });

        CantidadTableColumn.setCellValueFactory(cellData -> {
            Producto productoEnFila = cellData.getValue();
            if (movimiento != null && movimiento.getMovimientoProductos() != null) {
                return movimiento.getMovimientoProductos().stream()
                        .filter(mp -> mp.getIdProducto().equals(productoEnFila))
                        .findFirst()
                        .map(mp -> new SimpleStringProperty(mp.getCantidad() != null ? mp.getCantidad().toString() : ""))
                        .orElse(new SimpleStringProperty(""));
            }
            return new SimpleStringProperty("");
        });

        ZonaTableColumn.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getProductoZonas() != null && !producto.getProductoZonas().isEmpty()) {
                ProductoZona productoZona = producto.getProductoZonas().iterator().next();
                if (productoZona.getIdZona() != null) {
                    return new SimpleStringProperty(productoZona.getIdZona().getNombreZona());
                }
            }
            return new SimpleStringProperty("");
        });
        CodigoInternoTableColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCodigoInterno())
        );
        mostrarDatos();
    }

    @Override
    public Object onOpen(Object input) throws IOException { return null; }
    @Override
    public void onClose(Object output) {}
}