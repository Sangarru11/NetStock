package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.MovimientoDAO;
import com.github.Sangarru11.Model.DAO.MovimientoProductoDAO;
import com.github.Sangarru11.Model.DAO.ProductoDAO;
import com.github.Sangarru11.Model.Entity.Movimiento;
import com.github.Sangarru11.Model.Entity.MovimientoProducto;
import com.github.Sangarru11.Model.Entity.MovimientoProductoId;
import com.github.Sangarru11.Model.Entity.Producto;
import com.github.Sangarru11.Model.Entity.Usuario;
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

    private MovimientosController movimientosController;

    public void setMovimientosController(MovimientosController controller) {
        this.movimientosController = controller;
    }

    @Override
    public Object onOpen(Object input) throws IOException {
        cargarProductosDisponibles();
        return null;
    }

    @Override
    public void onClose(Object output) {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productosDisponibles = FXCollections.observableArrayList();
        productosAnadidosAlMovimiento = FXCollections.observableArrayList();
        tableViewProductosDisponibles.setItems(productosDisponibles);
        tableViewProductosAnadidos.setItems(productosAnadidosAlMovimiento);

        ObservableList<String> tiposMovimiento = FXCollections.observableArrayList("Salida", "Entrega");
        comboBoxTipoMovimiento.setItems(tiposMovimiento);

        configurarTablaProductosDisponibles();
        configurarTablaProductosAnadidos();
        cargarProductosDisponibles();
    }

    private void cargarProductosDisponibles() {
        System.out.println("[DEBUG] AddMovimientosController.cargarProductosDisponibles() - INICIO");
        ProductoDAO productoDAO = ProductoDAO.build();
        List<Producto> productoList = productoDAO.findAll();

        productosDisponibles.clear();
        if (productoList != null) {
            for (Producto p : productoList) {
                System.out.println("[DEBUG] Cargando producto disponible: " + p.getNombre() + ", Stock BD: " + (p.getCantidadDisponible() != null ? p.getCantidadDisponible() : "N/A"));
                productosDisponibles.add(p);
            }
        }
        tableViewProductosDisponibles.refresh();
        System.out.println("[DEBUG] AddMovimientosController.cargarProductosDisponibles() - FIN. Total productos cargados: " + productosDisponibles.size());
    }


    private void configurarTablaProductosDisponibles() {
        colImagenDisp.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto.getFoto() != null && producto.getFoto().length > 0) {
                return new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(producto.getFoto()), 50, 50, true, true));
            }
            return new SimpleObjectProperty<>(null); // Imagen por defecto o nada
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

    private void abrirDialogoCantidad(Producto productoSeleccionadoOriginal) {
        System.out.println("[DEBUG] abrirDialogoCantidad para: " + productoSeleccionadoOriginal.getNombre() +
                ", Stock visual actual: " + (productoSeleccionadoOriginal.getCantidadDisponible() != null ? productoSeleccionadoOriginal.getCantidadDisponible() : "N/A"));

        Optional<ProductoMovimientoWrapper> optWrapperExistente = productosAnadidosAlMovimiento.stream()
                .filter(pmw -> pmw.getProducto().getId().equals(productoSeleccionadoOriginal.getId()))
                .findFirst();

        int cantidadPreviamenteEnWrapper = 0;
        if (optWrapperExistente.isPresent()) {
            cantidadPreviamenteEnWrapper = optWrapperExistente.get().getCantidadSuministrada();
            System.out.println("[DEBUG] Producto ya existe en la lista de añadidos con cantidad: " + cantidadPreviamenteEnWrapper);
        }

        int stockVisualActual = productoSeleccionadoOriginal.getCantidadDisponible() != null ? productoSeleccionadoOriginal.getCantidadDisponible() : 0;
        int stockBaseParaCalculo = stockVisualActual + cantidadPreviamenteEnWrapper;
        System.out.println("[DEBUG] Stock base para cálculo (visual actual + previa en wrapper): " + stockBaseParaCalculo);


        TextInputDialog dialog = new TextInputDialog(String.valueOf(cantidadPreviamenteEnWrapper > 0 ? cantidadPreviamenteEnWrapper : 1));
        dialog.setTitle("Introducir Cantidad");
        dialog.setHeaderText("Añadir '" + productoSeleccionadoOriginal.getNombre() + "' al movimiento.");
        dialog.setContentText("Cantidad a suministrar (Total disponible original para este movimiento: " + stockBaseParaCalculo + "):");

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
                System.out.println("[DEBUG] Cantidad introducida en diálogo: " + cantidadIntroducida);

                if (cantidadIntroducida <= 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Cantidad Inválida", "La cantidad debe ser mayor que cero.");
                    return;
                }

                if (cantidadIntroducida > stockBaseParaCalculo) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Stock Insuficiente",
                            "La cantidad solicitada (" + cantidadIntroducida +
                                    ") excede el stock disponible original para este movimiento (" + stockBaseParaCalculo + ").");
                    return;
                }

                int stockVisualRestante = stockBaseParaCalculo - cantidadIntroducida;

                if (optWrapperExistente.isPresent()) {
                    ProductoMovimientoWrapper wrapperExistente = optWrapperExistente.get();
                    wrapperExistente.setCantidadSuministrada(cantidadIntroducida);
                    System.out.println("[DEBUG] Wrapper existente actualizado con cantidad: " + cantidadIntroducida);
                } else {
                    productosAnadidosAlMovimiento.add(new ProductoMovimientoWrapper(productoSeleccionadoOriginal, cantidadIntroducida));
                    System.out.println("[DEBUG] Nuevo wrapper añadido con cantidad: " + cantidadIntroducida);
                }

                productoSeleccionadoOriginal.setCantidadDisponible(stockVisualRestante);
                System.out.println("[DEBUG] Stock visual actualizado para '" + productoSeleccionadoOriginal.getNombre() + "' a: " + stockVisualRestante);

                tableViewProductosDisponibles.refresh();
                tableViewProductosAnadidos.refresh();

            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "Por favor, introduce un número válido.");
            }
        });
    }

    @FXML
    public void addMovimiento() {
        System.out.println("[DEBUG] AddMovimientosController.addMovimiento() - INICIO");
        String tipo = comboBoxTipoMovimiento.getValue(); // Obtener valor del ComboBox
        String observaciones = txtObservaciones.getText().trim();
        String destinatario = txtDestinatario.getText().trim();

        if (tipo == null || tipo.isEmpty()) { // Comprobar si se seleccionó un tipo
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
        System.out.println("[DEBUG] Usuario logueado: " + usuarioLogueado.getNombreUsuario());

        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(tipo);
        movimiento.setObservaciones(observaciones);
        movimiento.setDestinatario(destinatario.isEmpty() ? null : destinatario);
        movimiento.setFecha(Instant.now());
        movimiento.setIdUsuario(usuarioLogueado);

        MovimientoDAO movimientoDAO = MovimientoDAO.build();
        System.out.println("[DEBUG] Intentando insertar movimiento: Tipo=" + tipo + ", UsuarioID=" + usuarioLogueado.getId());
        if (!movimientoDAO.insert(movimiento) || movimiento.getId() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el movimiento en la base de datos.");
            System.out.println("[ERROR] Falla al insertar movimiento o ID es null.");
            return;
        }
        System.out.println("[DEBUG] Movimiento insertado con ID: " + movimiento.getId());

        MovimientoProductoDAO mpDAO = MovimientoProductoDAO.build();
        ProductoDAO productoDAO = ProductoDAO.build();
        boolean todosProductosProcesadosCorrectamente = true;

        for (ProductoMovimientoWrapper pmw : productosAnadidosAlMovimiento) {
            Producto productoOriginalDelWrapper = pmw.getProducto();
            System.out.println("[DEBUG] Procesando producto para MovimientoProducto: " + productoOriginalDelWrapper.getNombre() + ", ID: " + productoOriginalDelWrapper.getId() + ", Cantidad en wrapper: " + pmw.getCantidadSuministrada());

            Producto productoParaActualizarBD = productoDAO.findById(productoOriginalDelWrapper.getId());
            if (productoParaActualizarBD == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Producto", "No se encontró el producto " + productoOriginalDelWrapper.getNombre() + " en la base de datos.");
                todosProductosProcesadosCorrectamente = false;
                System.out.println("[ERROR] Producto no encontrado en BD: ID=" + productoOriginalDelWrapper.getId());
                break;
            }
            System.out.println("[DEBUG] Producto '" + productoParaActualizarBD.getNombre() + "' obtenido de BD. Stock actual BD: " + (productoParaActualizarBD.getCantidadDisponible() != null ? productoParaActualizarBD.getCantidadDisponible() : "N/A"));


            MovimientoProducto mp = new MovimientoProducto();
            MovimientoProductoId mpId = new MovimientoProductoId();
            mpId.setIdMovimiento(movimiento.getId());
            mpId.setIdProducto(productoParaActualizarBD.getId());
            mp.setId(mpId);
            mp.setIdMovimiento(movimiento);
            mp.setIdProducto(productoParaActualizarBD);
            mp.setCantidad(pmw.getCantidadSuministrada());

            System.out.println("[DEBUG] Intentando insertar MovimientoProducto: MovID=" + movimiento.getId() + ", ProdID=" + productoParaActualizarBD.getId() + ", Cantidad=" + pmw.getCantidadSuministrada());
            if (!mpDAO.insert(mp)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el detalle del producto " + productoParaActualizarBD.getNombre() + " para el movimiento.");
                todosProductosProcesadosCorrectamente = false;
                System.out.println("[ERROR] Falla al insertar MovimientoProducto para ProdID: " + productoParaActualizarBD.getId());
                break;
            }
            System.out.println("[DEBUG] MovimientoProducto insertado correctamente para ProdID: " + productoParaActualizarBD.getId());

            int cantidadSuministradaEnMovimiento = pmw.getCantidadSuministrada();
            int stockActualEnBD = productoParaActualizarBD.getCantidadDisponible() != null ? productoParaActualizarBD.getCantidadDisponible() : 0;
            String tipoNormalizado = tipo.toLowerCase();
            int nuevoStockCalculadoParaBD = stockActualEnBD;

            if (tipoNormalizado.contains("salida") || tipoNormalizado.contains("transferencia") || tipoNormalizado.contains("entrega")) {
                nuevoStockCalculadoParaBD = stockActualEnBD - cantidadSuministradaEnMovimiento;
                System.out.println("[INFO] Tipo '" + tipo + "' reduce stock. Stock anterior: " + stockActualEnBD + ", Cantidad: " + cantidadSuministradaEnMovimiento + ", Nuevo stock: " + nuevoStockCalculadoParaBD);
            } else if (tipoNormalizado.contains("entrada") || tipoNormalizado.contains("devolución") || tipoNormalizado.contains("recepción") || tipoNormalizado.contains("devolucion")) {
                nuevoStockCalculadoParaBD = stockActualEnBD + cantidadSuministradaEnMovimiento;
                System.out.println("[INFO] Tipo '" + tipo + "' aumenta stock. Stock anterior: " + stockActualEnBD + ", Cantidad: " + cantidadSuministradaEnMovimiento + ", Nuevo stock: " + nuevoStockCalculadoParaBD);
            } else {
                System.out.println("[INFO] Tipo de movimiento '" + tipo + "' no afecta el stock para " + productoParaActualizarBD.getNombre());
            }

            if (nuevoStockCalculadoParaBD != stockActualEnBD ||
                tipoNormalizado.contains("salida") || tipoNormalizado.contains("transferencia") || tipoNormalizado.contains("entrega") ||
                tipoNormalizado.contains("entrada") || tipoNormalizado.contains("devolución") || tipoNormalizado.contains("recepción") || tipoNormalizado.contains("devolucion")) {

                productoParaActualizarBD.setCantidadDisponible(nuevoStockCalculadoParaBD);
                System.out.println("[DEBUG] Intentando actualizar stock en BD para '" + productoParaActualizarBD.getNombre() + "' a " + nuevoStockCalculadoParaBD);
                if (productoDAO.update(productoParaActualizarBD)) {
                    System.out.println("[DEBUG] Stock actualizado en BD para '" + productoParaActualizarBD.getNombre() + "'. Verificando...");
                    Producto productoVerificado = productoDAO.findById(productoParaActualizarBD.getId());
                    if (productoVerificado != null && productoVerificado.getCantidadDisponible() != null && productoVerificado.getCantidadDisponible() == nuevoStockCalculadoParaBD) {
                        System.out.println("[VERIFY] Stock para '" + productoVerificado.getNombre() + "' verificado en BD: " + productoVerificado.getCantidadDisponible());
                    } else {
                        System.out.println("[VERIFY ERROR] Discrepancia en verificación de stock para '" + productoParaActualizarBD.getNombre() + "'. Esperado: " + nuevoStockCalculadoParaBD + ", Encontrado: " + (productoVerificado != null && productoVerificado.getCantidadDisponible() != null ? productoVerificado.getCantidadDisponible() : "N/A"));
                        mostrarAlerta(Alert.AlertType.WARNING, "Verificación de Stock", "El stock para " + productoParaActualizarBD.getNombre() + " podría no haberse actualizado correctamente en la BD.");
                    }
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de Actualización", "No se pudo actualizar el stock para " + productoParaActualizarBD.getNombre() + " en la base de datos.");
                    todosProductosProcesadosCorrectamente = false;
                    System.out.println("[ERROR] Falla al actualizar stock en BD para ProdID: " + productoParaActualizarBD.getId());
                    break;
                }
            } else {
                System.out.println("[DEBUG] No se requiere actualización de stock para '" + productoParaActualizarBD.getNombre() + "' (stock no cambiado o tipo no reconocido para actualización).");
            }
        }

        if (todosProductosProcesadosCorrectamente) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Movimiento y actualización de stock realizados correctamente.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Parcial", "Algunos productos o el stock no pudieron ser procesados/actualizados correctamente. Revise la consola para más detalles.");
        }

        if (movimientosController != null) {
            movimientosController.recargarMovimientos();
            System.out.println("[DEBUG] Llamado a recargarMovimientos() en MovimientosController.");
        }

        limpiarFormulario();
        cargarProductosDisponibles();

        Stage stage = (Stage) comboBoxTipoMovimiento.getScene().getWindow();
        stage.close();
        System.out.println("[DEBUG] AddMovimientosController.addMovimiento() - FIN");
    }

    private void limpiarFormulario() {
        comboBoxTipoMovimiento.setValue(null);
        txtObservaciones.clear();
        txtDestinatario.clear();
        productosAnadidosAlMovimiento.clear();
        tableViewProductosAnadidos.refresh();
        System.out.println("[DEBUG] Formulario limpiado.");
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