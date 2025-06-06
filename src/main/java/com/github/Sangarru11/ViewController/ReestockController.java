package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.MovimientoDAO;
import com.github.Sangarru11.Model.DAO.MovimientoProductoDAO;
import com.github.Sangarru11.Model.DAO.ProductoDAO;
import com.github.Sangarru11.Model.DAO.ProveedorDAO;
import com.github.Sangarru11.Model.Entity.*;
import com.github.Sangarru11.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import java.util.stream.Collectors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReestockController extends Controller implements Initializable {
    @FXML private ComboBox<Proveedor> comboBoxDestinatario;

    @FXML private TableView<Producto> tableViewProductosDisponibles;
    @FXML private TableColumn<Producto, Image> colImagenDisp;
    @FXML private TableColumn<Producto, String> colNombreDisp;
    @FXML private TableColumn<Producto, String> colDescripcionDisp;
    @FXML private TableColumn<Producto, String> colCantidadDisp;

    @FXML private TableView<ProductoMovimientoWrapper> tableViewProductosAnadidos;
    @FXML private TableColumn<ProductoMovimientoWrapper, Image> colImagenAnad;
    @FXML private TableColumn<ProductoMovimientoWrapper, String> colNombreAnad;
    @FXML private TableColumn<ProductoMovimientoWrapper, String> colDescripcionAnad;
    @FXML private TableColumn<ProductoMovimientoWrapper, String> colCantidadAnad;

    private ObservableList<Producto> productosDisponibles;
    private ObservableList<ProductoMovimientoWrapper> productosAnadidosAlMovimiento;

    private GStockController GstockController;

    public void setGstockController(GStockController controller) {
        this.GstockController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productosDisponibles = FXCollections.observableArrayList();
        productosAnadidosAlMovimiento = FXCollections.observableArrayList();
        tableViewProductosDisponibles.setItems(productosDisponibles);
        tableViewProductosAnadidos.setItems(productosAnadidosAlMovimiento);

        cargarProveedores();
        configurarTablaProductosDisponibles();
        configurarTablaProductosAnadidos();

        comboBoxDestinatario.valueProperty().addListener((obs, oldVal, newVal) -> {
            cargarProductosPorProveedor(newVal);
        });
    }

    private void cargarProductosPorProveedor(Proveedor proveedor) {
        productosDisponibles.clear();
        if (proveedor != null) {
            ProductoDAO productoDAO = ProductoDAO.build();
            List<Producto> productosProveedor = productoDAO.findAll().stream()
                    .filter(p -> p.getIdProveedor() != null && p.getIdProveedor().getId().equals(proveedor.getId()))
                    .collect(Collectors.toList());
            productosDisponibles.addAll(productosProveedor);
        }
        tableViewProductosDisponibles.refresh();
    }

    private void cargarProveedores() {
        ProveedorDAO proveedorDAO = ProveedorDAO.build();
        List<Proveedor> proveedores = proveedorDAO.findAll();
        comboBoxDestinatario.setItems(FXCollections.observableArrayList(proveedores));
        comboBoxDestinatario.setConverter(new StringConverter<Proveedor>() {
            @Override
            public String toString(Proveedor proveedor) {
                return proveedor != null ? proveedor.getNombreEmpresa() : "";
            }
            @Override
            public Proveedor fromString(String s) {
                return null;
            }
        });
    }

    private void cargarProductosDisponibles() {
        ProductoDAO productoDAO = ProductoDAO.build();
        List<Producto> productoList = productoDAO.findAll();

        productosDisponibles.clear();
        if (productoList != null) {
            productosDisponibles.addAll(productoList);
        }
        tableViewProductosDisponibles.refresh();
    }

    private void configurarTablaProductosDisponibles() {
        colImagenDisp.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getFoto() != null && producto.getFoto().length > 0) {
                return new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(producto.getFoto()), 50, 50, true, true));
            }
            return new SimpleObjectProperty<>(null);
        });
        colImagenDisp.setCellFactory(col -> createImageCell());

        colNombreDisp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDescripcionDisp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colCantidadDisp.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCantidadDisponible() != null ? cellData.getValue().getCantidadDisponible().toString() : "0"
                )
        );

        tableViewProductosDisponibles.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Producto productoSeleccionado = row.getItem();
                    abrirDialogoCantidad(productoSeleccionado);
                }
            });
            return row;
        });
    }

    private void configurarTablaProductosAnadidos() {
        colImagenAnad.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue().getProducto();
            if (producto.getFoto() != null && producto.getFoto().length > 0) {
                return new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(producto.getFoto()), 50, 50, true, true));
            }
            return new SimpleObjectProperty<>(null);
        });
        colImagenAnad.setCellFactory(col -> createImageCell());

        colNombreAnad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getNombre()));
        colDescripcionAnad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getDescripcion()));
        colCantidadAnad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCantidadSuministrada())));
    }

    private <T> TableCell<T, Image> createImageCell() {
        return new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setAlignment(javafx.geometry.Pos.CENTER);
            }
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    imageView.setImage(null);
                } else {
                    imageView.setImage(item);
                }
            }
        };
    }

    // Dentro de ReestockController

    private void abrirDialogoCantidad(Producto productoSeleccionadoOriginal) {
        Optional<ProductoMovimientoWrapper> optWrapperExistente = productosAnadidosAlMovimiento.stream()
                .filter(pmw -> pmw.getProducto().getId().equals(productoSeleccionadoOriginal.getId()))
                .findFirst();

        int cantidadPreviamenteEnWrapper = 0;
        if (optWrapperExistente.isPresent()) {
            cantidadPreviamenteEnWrapper = optWrapperExistente.get().getCantidadSuministrada();
        }

        int stockVisualActual = productoSeleccionadoOriginal.getCantidadDisponible() != null ? productoSeleccionadoOriginal.getCantidadDisponible() : 0;
        int stockBaseParaCalculo = stockVisualActual - cantidadPreviamenteEnWrapper;

        TextInputDialog dialog = new TextInputDialog(String.valueOf(cantidadPreviamenteEnWrapper > 0 ? cantidadPreviamenteEnWrapper : 1));
        dialog.setTitle("Introducir Cantidad");
        dialog.setHeaderText("Añadir '" + productoSeleccionadoOriginal.getNombre() + "' al stock.");
        dialog.setContentText("Cantidad a añadir (Stock actual: " + stockVisualActual + "):");

        final Pattern intPattern = Pattern.compile("\\d*");
        dialog.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            if (intPattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        }));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantidadStr -> {
            try {
                int cantidadIntroducida = Integer.parseInt(cantidadStr);

                if (cantidadIntroducida <= 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Cantidad Inválida", "La cantidad debe ser mayor que cero.");
                    return;
                }

                if (optWrapperExistente.isPresent()) {
                    ProductoMovimientoWrapper wrapperExistente = optWrapperExistente.get();
                    wrapperExistente.setCantidadSuministrada(cantidadIntroducida);
                } else {
                    productosAnadidosAlMovimiento.add(new ProductoMovimientoWrapper(productoSeleccionadoOriginal, cantidadIntroducida));
                }

                // No modificar el stock visual aquí, solo refrescar tablas
                tableViewProductosDisponibles.refresh();
                tableViewProductosAnadidos.refresh();

            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "Por favor, introduce un número válido.");
            }
        });
    }

    @FXML
    public void addMovimiento() {
        Proveedor destinatario = comboBoxDestinatario.getValue();

        if (destinatario == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campo Requerido", "Por favor, seleccione un proveedor destinatario.");
            return;
        }
        if (productosAnadidosAlMovimiento.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Productos", "Debe añadir al menos un producto al Reestock.");
            return;
        }

        Usuario usuarioLogueado = SessionManager.getUsuarioLogueado();
        if (usuarioLogueado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No se pudo obtener el usuario logueado. Por favor, inicie sesión de nuevo.");
            return;
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("Entrada");
        movimiento.setFecha(Instant.now());
        movimiento.setIdUsuario(usuarioLogueado);

        MovimientoDAO movimientoDAO = MovimientoDAO.build();
        if (!movimientoDAO.insert(movimiento) || movimiento.getId() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el reestock en la base de datos.");
            return;
        }

        MovimientoProductoDAO mpDAO = MovimientoProductoDAO.build();
        ProductoDAO productoDAO = ProductoDAO.build();
        boolean todosProductosProcesadosCorrectamente = true;

        for (ProductoMovimientoWrapper pmw : productosAnadidosAlMovimiento) {
            Producto productoOriginalDelWrapper = pmw.getProducto();
            Producto productoParaActualizarBD = productoDAO.findById(productoOriginalDelWrapper.getId());
            if (productoParaActualizarBD == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Producto", "No se encontró el producto " + productoOriginalDelWrapper.getNombre() + " en la base de datos.");
                todosProductosProcesadosCorrectamente = false;
                break;
            }

            MovimientoProducto mp = new MovimientoProducto();
            MovimientoProductoId mpId = new MovimientoProductoId();
            mpId.setIdMovimiento(movimiento.getId());
            mpId.setIdProducto(productoParaActualizarBD.getId());
            mp.setId(mpId);
            mp.setIdMovimiento(movimiento);
            mp.setIdProducto(productoParaActualizarBD);
            mp.setCantidad(pmw.getCantidadSuministrada());

            if (!mpDAO.insert(mp)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el detalle del producto " + productoParaActualizarBD.getNombre() + " para el movimiento.");
                todosProductosProcesadosCorrectamente = false;
                break;
            }

            int cantidadSuministradaEnMovimiento = pmw.getCantidadSuministrada();
            int stockActualEnBD = productoParaActualizarBD.getCantidadDisponible() != null ? productoParaActualizarBD.getCantidadDisponible() : 0;
            int nuevoStockCalculadoParaBD = stockActualEnBD + cantidadSuministradaEnMovimiento;

            productoParaActualizarBD.setCantidadDisponible(nuevoStockCalculadoParaBD);
            if (!productoDAO.update(productoParaActualizarBD)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Actualización", "No se pudo actualizar el stock para " + productoParaActualizarBD.getNombre() + " en la base de datos.");
                todosProductosProcesadosCorrectamente = false;
                break;
            }
        }

        if (todosProductosProcesadosCorrectamente) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Reestock y actualización de stock realizados correctamente.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Parcial", "Algunos productos o el stock no pudieron ser procesados/actualizados correctamente.");
        }

        if (GstockController != null) {
            GstockController.recargarStock();
        }

        limpiarFormulario();
        cargarProductosDisponibles();

        Stage stage = (Stage) comboBoxDestinatario.getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        comboBoxDestinatario.getSelectionModel().clearSelection();
        productosAnadidosAlMovimiento.clear();
        tableViewProductosAnadidos.refresh();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @Override
    public Object onOpen(Object input) throws IOException {
        return null;
    }

    @Override
    public void onClose(Object output) {}

    public static class ProductoMovimientoWrapper {
        private final Producto producto;
        private int cantidadSuministrada;

        public ProductoMovimientoWrapper(Producto producto, int cantidadSuministrada) {
            this.producto = producto;
            this.cantidadSuministrada = cantidadSuministrada;
        }

        public Producto getProducto() {
            return producto;
        }

        public int getCantidadSuministrada() {
            return cantidadSuministrada;
        }

        public void setCantidadSuministrada(int cantidadSuministrada) {
            this.cantidadSuministrada = cantidadSuministrada;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductoMovimientoWrapper that = (ProductoMovimientoWrapper) o;
            return Objects.equals(producto.getId(), that.producto.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(producto.getId());
        }
    }
}