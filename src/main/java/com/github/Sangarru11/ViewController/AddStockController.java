package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.Model.DAO.*;
import com.github.Sangarru11.Model.Entity.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddStockController extends Controller implements Initializable {

    @FXML
    private TextArea txtNombreProducto;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox<Categoria> ComboBoxCategoria;
    @FXML
    private TextArea idCantidadDisponible;
    @FXML
    private TextArea txtCodigoInterno;
    @FXML
    private ImageView ImageViewProducto;
    @FXML
    private ComboBox<Zona> ComboBoxZona;
    @FXML
    private ComboBox<Proveedor> ProveedorComboBox;


    private byte[] fotoProducto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var categorias = CategoriaDAO.build().findAll();
        ComboBoxCategoria.getItems().addAll(categorias);
        ComboBoxCategoria.setConverter(new javafx.util.StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria != null ? categoria.getNombreCategoria() : "";
            }
            @Override
            public Categoria fromString(String string) {
                return ComboBoxCategoria.getItems().stream()
                        .filter(cat -> cat.getNombreCategoria().equals(string))
                        .findFirst().orElse(null);
            }
        });

        var proveedores = ProveedorDAO.build().findAll();
        ProveedorComboBox.getItems().addAll(proveedores);
        ProveedorComboBox.setConverter(new javafx.util.StringConverter<Proveedor>() {
            @Override
            public String toString(Proveedor proveedor) {
                return proveedor != null ? proveedor.getNombreEmpresa() : "";
            }
            @Override
            public Proveedor fromString(String string) {
                return ProveedorComboBox.getItems().stream()
                        .filter(p -> p.getNombreEmpresa().equals(string))
                        .findFirst().orElse(null);
            }
        });

        var zonas = ZonaDAO.build().findAll();
        ComboBoxZona.getItems().addAll(zonas);
        ComboBoxZona.setConverter(new javafx.util.StringConverter<Zona>() {
            @Override
            public String toString(Zona zona) {
                return zona != null ? zona.getNombreZona() : "";
            }
            @Override
            public Zona fromString(String string) {
                return ComboBoxZona.getItems().stream()
                        .filter(z -> z.getNombreZona().equals(string))
                        .findFirst().orElse(null);
            }
        });
    }

    @FXML
    private void AddImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.wbmp")
        );

        File file = fileChooser.showOpenDialog(txtNombreProducto.getScene().getWindow());
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                ImageViewProducto.setImage(image);

                javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);
                java.awt.image.BufferedImage bImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                javax.imageio.ImageIO.write(bImage, "png", baos);
                baos.flush();
                fotoProducto = baos.toByteArray();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addProducto() {
        if (txtNombreProducto.getText().trim().isEmpty() ||
            txtDescripcion.getText().trim().isEmpty() ||
            idCantidadDisponible.getText().trim().isEmpty() ||
            txtCodigoInterno.getText().trim().isEmpty() ||
            fotoProducto == null ||
            ComboBoxCategoria.getValue() == null ||
            ComboBoxZona.getValue() == null ||
            ProveedorComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Todos los campos son obligatorios, incluyendo la imagen, la categoría, la zona y el proveedor.");
            alert.show();
            return;
        }

        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(idCantidadDisponible.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("La cantidad debe ser un número válido.");
            alert.show();
            return;
        }

        Producto producto = new Producto();
        producto.setNombre(txtNombreProducto.getText());
        producto.setDescripcion(txtDescripcion.getText());
        producto.setIdCategoria(ComboBoxCategoria.getValue());
        producto.setCantidadDisponible(cantidad);
        producto.setCodigoInterno(txtCodigoInterno.getText());
        producto.setFoto(fotoProducto);
        producto.setIdProveedor(ProveedorComboBox.getValue());

        ProductoDAO.build().insert(producto);

        Zona zonaSeleccionada = ComboBoxZona.getValue();
        if (zonaSeleccionada != null) {
            ProductoZona productoZona = new ProductoZona();
            productoZona.setIdProducto(producto);
            productoZona.setIdZona(zonaSeleccionada);
            ProductoZonaDAO.build().insert(productoZona);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Producto añadido correctamente.");
        alert.show();

        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNombreProducto.clear();
        txtDescripcion.clear();
        idCantidadDisponible.clear();
        txtCodigoInterno.clear();
        ComboBoxCategoria.getSelectionModel().clearSelection();
        ComboBoxZona.getSelectionModel().clearSelection();
        ProveedorComboBox.getSelectionModel().clearSelection();
        ImageViewProducto.setImage(null);
        fotoProducto = null;
    }

    @Override
    public Object onOpen(Object input) throws IOException {
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
    private void GoToTecnicos() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Tecnicos, null);
    }

    @FXML
    private void GoToProveedores() throws IOException {
        WelcomePageController.changeSceneToMain(Scenes.Proveedores, null);
    }
}
