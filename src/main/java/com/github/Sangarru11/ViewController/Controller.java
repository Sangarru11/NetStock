package com.github.Sangarru11.ViewController;

import com.github.Sangarru11.App;

import java.io.IOException;

public abstract class Controller {
    App app;
    public void setApp(App app){
        this.app=app;
    }

    public abstract Object onOpen(Object input) throws IOException;

    public abstract void onClose(Object output);
}
