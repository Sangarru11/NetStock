package com.github.Sangarru11.ViewController;

public enum Scenes {
    WelcomePage("View/WelcomePage.fxml"),
    RegisterPage("View/RegisterPage.fxml"),
    GStock("View/GStock.fxml"),
    MainPage("View/MainPage.fxml"),
    AddStock("View/AddStock.fxml"),
    Movimientos("View/Movimientos.fxml"),
    Categorias("View/Categorias.fxml"),
    Zonas("View/Zonas.fxml"),
    AddMovimiento("View/AddMovimiento.fxml"),
    Tecnicos("View/Tecnicos.fxml"),
    Proveedores("View/Proveedores.fxml"),
    Reestock("View/Reestock.fxml"),
    AddCategoria("View/AddCategoria.fxml");

    private String url;
    Scenes(String url) {
        this.url = url;
    }
    public String getURL() {
        return url;
    }
}
