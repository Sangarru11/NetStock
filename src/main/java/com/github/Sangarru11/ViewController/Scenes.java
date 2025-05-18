package com.github.Sangarru11.ViewController;

public enum Scenes {
    WelcomePage("View/WelcomePage.fxml"),
    RegisterPage("View/RegisterPage.fxml"),
    sHuellaHabito("View/sHuellaHabito.fxml"),
    MainPage("View/MainPage.fxml"),
    RegistrarHuella("View/RegistrarHuella.fxml"),
    RegistrarHabito("View/RegistrarHabito.fxml"),
    Comparativa("View/Comparativa.fxml"),
    MainPageHabito("View/MainPageHabito.fxml");

    private String url;
    Scenes(String url) {
        this.url = url;
    }
    public String getURL() {
        return url;
    }
}
