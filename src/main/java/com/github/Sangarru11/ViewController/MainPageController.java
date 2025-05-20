package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.ProductoDAO;
import com.github.Sangarru11.Model.Entity.Producto;
import com.github.Sangarru11.Model.Entity.ProductoZona;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController extends Controller implements Initializable {
    @FXML
    private TableView<Producto> tableView;
    @FXML
    private TableColumn<Producto, String> IdProducto;
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

    private javafx.collections.ObservableList<Producto> productos;

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
        tableView.setEditable(true);

        tableView.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Producto producto = tableView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Acción al hacer doble clic, por ejemplo mostrar detalles
                    // showProductoDetails(producto);
                }
            });
            return row;
        });

        IdProducto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getId() != null ? cellData.getValue().getId().toString() : ""
                )
        );
        IdProducto.setCellFactory(TextFieldTableCell.forTableColumn());

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
            }
        });

        ZonaProducto.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            // Obtener la primera zona asociada (puedes ajustar esto según tu lógica)
            if (producto.getProductoZonas() != null && !producto.getProductoZonas().isEmpty()) {
                // Tomar la primera zona
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
}