package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.MovimientoDAO;
import com.github.Sangarru11.Model.DAO.MovimientoProductoDAO;
import com.github.Sangarru11.Model.DAO.ProductoDAO;
import com.github.Sangarru11.Model.DAO.TecnicoDAO;
import com.github.Sangarru11.Model.DAO.ProveedorDAO;
import com.github.Sangarru11.Model.DAO.ZonaDAO;
import com.github.Sangarru11.Model.Entity.*;
import com.github.Sangarru11.Utils.SessionManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddMovimientosController extends Controller implements Initializable {

    @FXML private ComboBox<String> comboBoxTipoMovimiento;
    @FXML private TextArea txtObservaciones;
    @FXML private ComboBox<String> cmbTipoDestinatario;
    @FXML private ComboBox<Object> cmbDestinatarioEntidad;
    @FXML private TextArea txtDestinatario;

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
    private ObservableList<Tecnico> tecnicosDisponibles;
    private ObservableList<Proveedor> proveedoresDisponibles;
    private ObservableList<Zona> zonasDisponibles;

    private MovimientosController movimientosController;

    public void setMovimientosController(MovimientosController controller) {
        this.movimientosController = controller;
    }

    @Override
    public Object onOpen(Object input) throws IOException {
        return null;
    }

    @Override
    public void onClose(Object output) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productosDisponibles = FXCollections.observableArrayList();
        productosAnadidosAlMovimiento = FXCollections.observableArrayList();
        tecnicosDisponibles = FXCollections.observableArrayList();
        proveedoresDisponibles = FXCollections.observableArrayList();
        zonasDisponibles = FXCollections.observableArrayList();

        tableViewProductosDisponibles.setItems(productosDisponibles);
        tableViewProductosAnadidos.setItems(productosAnadidosAlMovimiento);

        cmbDestinatarioEntidad.setConverter(new StringConverter<Object>() {
            @Override
            public String toString(Object object) {
                if (object instanceof Tecnico) {
                    return ((Tecnico) object).getNombre() + " " + (((Tecnico) object).getApellidos() != null ? ((Tecnico) object).getApellidos() : "");
                } else if (object instanceof Proveedor) {
                    return ((Proveedor) object).getNombreEmpresa();
                }
                return null;
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });

        ObservableList<String> tiposMovimiento = FXCollections.observableArrayList("Entrega");
        comboBoxTipoMovimiento.setItems(tiposMovimiento);
        if (!tiposMovimiento.isEmpty()) {
            comboBoxTipoMovimiento.setValue(tiposMovimiento.get(0));
        }


        ObservableList<String> tiposDestinatario = FXCollections.observableArrayList("Técnico", "Proveedor");
        cmbTipoDestinatario.setItems(tiposDestinatario);

        comboBoxTipoMovimiento.valueProperty().addListener((obs, oldVal, newVal) -> actualizarVisibilidadYContenidoDestinatario());
        cmbTipoDestinatario.valueProperty().addListener((obs, oldVal, newVal) -> actualizarVisibilidadYContenidoDestinatario());

        configurarTablaProductosDisponibles();
        configurarTablaProductosAnadidos();
        cargarDatosIniciales();

        actualizarVisibilidadYContenidoDestinatario();
    }

    private void cargarDatosIniciales() {
        cargarProductosDisponibles();
        cargarTecnicos();
        cargarProveedores();
        cargarZonas();
    }

    private void actualizarVisibilidadYContenidoDestinatario() {
        String tipoMovimiento = comboBoxTipoMovimiento.getValue();
        boolean esModoEntrega = "Entrega".equals(tipoMovimiento);

        txtDestinatario.setVisible(false);
        txtDestinatario.setManaged(false);
        txtDestinatario.clear();

        cmbTipoDestinatario.setVisible(esModoEntrega);
        cmbTipoDestinatario.setManaged(esModoEntrega);

        cmbDestinatarioEntidad.setVisible(esModoEntrega);
        cmbDestinatarioEntidad.setManaged(esModoEntrega);

        if (esModoEntrega) {
            String tipoDestinatarioSeleccionado = cmbTipoDestinatario.getValue();
            if (tipoDestinatarioSeleccionado != null && !tipoDestinatarioSeleccionado.isEmpty()) {
                poblarCmbDestinatarioEntidadParaEntrega(tipoDestinatarioSeleccionado);
            } else {
                cmbDestinatarioEntidad.getItems().clear();
            }
        } else {
            cmbTipoDestinatario.getSelectionModel().clearSelection();
            cmbDestinatarioEntidad.getItems().clear();
            cmbDestinatarioEntidad.getSelectionModel().clearSelection();
        }
    }

    private void poblarCmbDestinatarioEntidadParaEntrega(String tipoDestinatario) {
        cmbDestinatarioEntidad.getItems().clear();
        if ("Técnico".equals(tipoDestinatario)) {
            cmbDestinatarioEntidad.setItems(FXCollections.observableArrayList(tecnicosDisponibles));
        } else if ("Proveedor".equals(tipoDestinatario)) {
            cmbDestinatarioEntidad.setItems(FXCollections.observableArrayList(proveedoresDisponibles));
        }
        cmbDestinatarioEntidad.getSelectionModel().clearSelection();
    }

    private void cargarProductosDisponibles() {
        List<Producto> productoList = ProductoDAO.build().findAll();
        productosDisponibles.clear();
        if (productoList != null) {
            productosDisponibles.addAll(productoList);
        }
        tableViewProductosDisponibles.refresh();
    }

    private void cargarTecnicos() {
        List<Tecnico> listaTecnicos = TecnicoDAO.build().findAll();
        tecnicosDisponibles.clear();
        if (listaTecnicos != null) {
            tecnicosDisponibles.addAll(listaTecnicos);
        }
        if ("Entrega".equals(comboBoxTipoMovimiento.getValue()) && "Técnico".equals(cmbTipoDestinatario.getValue())) {
            poblarCmbDestinatarioEntidadParaEntrega("Técnico");
        }
    }

    private void cargarProveedores() {
        List<Proveedor> listaProveedores = ProveedorDAO.build().findAll();
        proveedoresDisponibles.clear();
        if (listaProveedores != null) {
            proveedoresDisponibles.addAll(listaProveedores);
        }
        if ("Entrega".equals(comboBoxTipoMovimiento.getValue()) && "Proveedor".equals(cmbTipoDestinatario.getValue())) {
            poblarCmbDestinatarioEntidadParaEntrega("Proveedor");
        }
    }

    private void cargarZonas() {
        List<Zona> listaZonas = ZonaDAO.build().findAll();
        zonasDisponibles.clear();
        if (listaZonas != null) {
            zonasDisponibles.addAll(listaZonas);
        }
    }

    private void configurarTablaProductosDisponibles() {
        colImagenDisp.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getFoto() != null && producto.getFoto().length > 0) {
                try {
                    return new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(producto.getFoto()), 50, 50, true, true));
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen del producto disponible: " + producto.getNombre() + " - " + e.getMessage());
                }
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
                try {
                    return new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(producto.getFoto()), 50, 50, true, true));
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen del producto añadido: " + producto.getNombre() + " - " + e.getMessage());
                }
            }
            return new SimpleObjectProperty<>(null);
        });
        colImagenAnad.setCellFactory(col -> createImageCell());
        colNombreAnad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getNombre()));
        colDescripcionAnad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getDescripcion()));
        colCantidadAnad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCantidadSuministrada())));

        tableViewProductosAnadidos.setRowFactory(tv -> {
            TableRow<ProductoMovimientoWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ProductoMovimientoWrapper wrapperSeleccionado = row.getItem();
                    abrirDialogoCantidad(wrapperSeleccionado.getProducto());
                }
            });
            return row;
        });
    }

    private <T> TableCell<T, Image> createImageCell() {
        return new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
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
        };
    }

    private void abrirDialogoCantidad(Producto productoSeleccionadoOriginal) {
        Optional<ProductoMovimientoWrapper> optWrapperExistente = productosAnadidosAlMovimiento.stream()
                .filter(pmw -> pmw.getProducto().getId().equals(productoSeleccionadoOriginal.getId()))
                .findFirst();

        int cantidadPreviamenteEnWrapper = optWrapperExistente.map(ProductoMovimientoWrapper::getCantidadSuministrada).orElse(0);

        Producto productoEnTablaDisponibles = productosDisponibles.stream()
            .filter(p -> p.getId().equals(productoSeleccionadoOriginal.getId()))
            .findFirst()
            .orElse(productoSeleccionadoOriginal);

        int stockVisualActual = productoEnTablaDisponibles.getCantidadDisponible() != null ? productoEnTablaDisponibles.getCantidadDisponible() : 0;

        TextInputDialog dialog = new TextInputDialog(String.valueOf(cantidadPreviamenteEnWrapper > 0 ? cantidadPreviamenteEnWrapper : 1));
        dialog.setTitle("Introducir Cantidad");
        dialog.setHeaderText("Añadir '" + productoSeleccionadoOriginal.getNombre() + "' al movimiento.");
        dialog.setContentText("Cantidad a suministrar (Disponible actual: " + stockVisualActual + "):");

        final Pattern intPattern = Pattern.compile("\\d+");
        dialog.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || (intPattern.matcher(newText).matches() && Integer.parseInt(newText) >= 0)) {
                return change;
            }
            return null;
        }));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantidadStr -> {
            if (cantidadStr.isEmpty()) return;
            try {
                int cantidadValida = Integer.parseInt(cantidadStr);
                if (cantidadValida < 0) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Cantidad Inválida", "La cantidad no puede ser negativa.");
                    return;
                }

                int maxCantidadPermitida = stockVisualActual + cantidadPreviamenteEnWrapper;

                if (cantidadValida > maxCantidadPermitida) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Cantidad Excesiva",
                            "No puede suministrar más de " + maxCantidadPermitida + " unidades (Stock actual: " + stockVisualActual +
                            (cantidadPreviamenteEnWrapper > 0 ? ", ya añadido: " + cantidadPreviamenteEnWrapper : "") + ").");
                    return;
                }

                int diferenciaTransferida = cantidadValida - cantidadPreviamenteEnWrapper;

                if (optWrapperExistente.isPresent()) {
                    if (cantidadValida == 0) {
                        productosAnadidosAlMovimiento.remove(optWrapperExistente.get());
                    } else {
                        optWrapperExistente.get().setCantidadSuministrada(cantidadValida);
                    }
                } else {
                    if (cantidadValida > 0) {
                        productosAnadidosAlMovimiento.add(new ProductoMovimientoWrapper(productoEnTablaDisponibles, cantidadValida));
                    } else {
                        tableViewProductosAnadidos.refresh();
                        tableViewProductosDisponibles.refresh();
                        return;
                    }
                }
                tableViewProductosAnadidos.refresh();

                productoEnTablaDisponibles.setCantidadDisponible(stockVisualActual - diferenciaTransferida);
                tableViewProductosDisponibles.refresh(); // Actualiza la tabla de disponibles

            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Entrada Inválida", "Por favor, introduzca un número válido.");
            }
        });
    }

    @FXML
    public void addMovimiento() {
        String tipo = comboBoxTipoMovimiento.getValue();
        String observacionesInput = txtObservaciones.getText().trim();

        if (tipo == null || tipo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campo Requerido", "Por favor, seleccione un tipo de movimiento.");
            return;
        }
        if (productosAnadidosAlMovimiento.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin Productos", "Debe añadir al menos un producto al movimiento.");
            return;
        }
        Usuario usuarioLogueado = SessionManager.getUsuarioLogueado();
        if (usuarioLogueado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No se pudo obtener el usuario logueado. Por favor, inicie sesión de nuevo.");
            return;
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(tipo);
        movimiento.setFecha(Instant.now());
        movimiento.setIdUsuario(usuarioLogueado);
        String observacionesCompletas = observacionesInput;
        Object destinatarioObj = cmbDestinatarioEntidad.getValue();

        if ("Entrega".equals(tipo)) {
            String tipoDestinatario = cmbTipoDestinatario.getValue();
            if (tipoDestinatario == null || tipoDestinatario.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campo Requerido", "Por favor, seleccione un tipo de destinatario.");
                return;
            }
            if (destinatarioObj == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campo Requerido", "Por favor, seleccione el nombre del destinatario.");
                return;
            }

            String nombreDestinatarioParaObs = "";
            if (destinatarioObj instanceof Tecnico) {
                movimiento.setIdTecnico((Tecnico) destinatarioObj);
                nombreDestinatarioParaObs = ((Tecnico) destinatarioObj).getNombre() + " " + (((Tecnico) destinatarioObj).getApellidos() != null ? ((Tecnico) destinatarioObj).getApellidos() : "");
            } else if (destinatarioObj instanceof Proveedor) {
                // La entidad Movimiento no tiene campo para Proveedor directamente.
                // Se incluye en observaciones.
                nombreDestinatarioParaObs = ((Proveedor) destinatarioObj).getNombreEmpresa();
            }
            observacionesCompletas = "Destino: " + tipoDestinatario + " - " + nombreDestinatarioParaObs +
                                    (observacionesInput.isEmpty() ? "" : ". Observaciones: " + observacionesInput);
        }
        movimiento.setObservaciones(observacionesCompletas.isEmpty() ? null : observacionesCompletas);

        if (!MovimientoDAO.build().insert(movimiento) || movimiento.getId() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el movimiento principal en la base de datos.");
            return;
        }

        MovimientoProductoDAO mpDAO = MovimientoProductoDAO.build();
        ProductoDAO productoDAO = ProductoDAO.build();
        boolean todosProductosProcesadosCorrectamente = true;

        for (ProductoMovimientoWrapper pmw : productosAnadidosAlMovimiento) {
            Producto productoOriginalDelWrapper = pmw.getProducto();
            Producto productoParaActualizarBD = productoDAO.findById(productoOriginalDelWrapper.getId());

            if (productoParaActualizarBD == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Datos", "No se encontró el producto " + productoOriginalDelWrapper.getNombre() + " en la base de datos para actualizar stock.");
                todosProductosProcesadosCorrectamente = false;
                continue;
            }

            int cantidadSuministradaEnMovimiento = pmw.getCantidadSuministrada();
            int stockActualEnBD = productoParaActualizarBD.getCantidadDisponible() != null ? productoParaActualizarBD.getCantidadDisponible() : 0;

            MovimientoProducto mp = new MovimientoProducto();
            MovimientoProductoId mpId = new MovimientoProductoId();
            mpId.setIdMovimiento(movimiento.getId());
            mpId.setIdProducto(productoParaActualizarBD.getId());
            mp.setId(mpId);
            mp.setIdMovimiento(movimiento);
            mp.setIdProducto(productoParaActualizarBD);
            mp.setCantidad(cantidadSuministradaEnMovimiento);

            if (!mpDAO.insert(mp)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el detalle del producto " + productoParaActualizarBD.getNombre() + " en el movimiento.");
                todosProductosProcesadosCorrectamente = false;
                continue;
            }

            int nuevoStockCalculadoParaBD = stockActualEnBD;
            if ("Entrega".equalsIgnoreCase(tipo)) {
                nuevoStockCalculadoParaBD = stockActualEnBD - cantidadSuministradaEnMovimiento;
            }

            productoParaActualizarBD.setCantidadDisponible(nuevoStockCalculadoParaBD);
            if (!productoDAO.update(productoParaActualizarBD)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Actualizar", "No se pudo actualizar el stock del producto " + productoParaActualizarBD.getNombre() + ".");
                todosProductosProcesadosCorrectamente = false;
            }
        }

        if (todosProductosProcesadosCorrectamente) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Movimiento registrado y stock actualizado correctamente.");
            limpiarFormulario();
            if (movimientosController != null) {
                movimientosController.recargarMovimientos();
            }
            Stage stage = (Stage) comboBoxTipoMovimiento.getScene().getWindow();
            stage.close();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Proceso Incompleto", "Algunos productos o el stock no se procesaron correctamente. Revise los movimientos y el stock.");
        }
    }

    private void limpiarFormulario() {
        if (!comboBoxTipoMovimiento.getItems().isEmpty()) {
            comboBoxTipoMovimiento.setValue(comboBoxTipoMovimiento.getItems().get(0));
        } else {
            comboBoxTipoMovimiento.getSelectionModel().clearSelection();
        }
        cmbTipoDestinatario.getSelectionModel().clearSelection();
        cmbDestinatarioEntidad.getSelectionModel().clearSelection();
        txtObservaciones.clear();
        productosAnadidosAlMovimiento.clear();
        tableViewProductosAnadidos.refresh();
        actualizarVisibilidadYContenidoDestinatario();
        cargarProductosDisponibles();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public static class ProductoMovimientoWrapper {
        private final Producto producto;
        private int cantidadSuministrada;

        public ProductoMovimientoWrapper(Producto producto, int cantidadSuministrada) {
            this.producto = producto;
            this.cantidadSuministrada = cantidadSuministrada;
        }
        public Producto getProducto() { return producto; }
        public int getCantidadSuministrada() { return cantidadSuministrada; }
        public void setCantidadSuministrada(int cantidadSuministrada) { this.cantidadSuministrada = cantidadSuministrada; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductoMovimientoWrapper that = (ProductoMovimientoWrapper) o;
            return Objects.equals(producto.getId(), that.producto.getId());
        }
        @Override
        public int hashCode() { return Objects.hash(producto.getId()); }
    }
}