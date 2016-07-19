package org.imartynov.lab;

import javafx.application.Application;
import javafx.stage.Stage;

import static java.lang.System.out;

/**
 * Created by Ilya on 28.06.2016.
 */
public class App extends Application {
    WebProcessor wp;
    LoginScenario s;

    public App() {

    }

    public void init() {
        out.println("App init");
    }

    @Override
    public void start(Stage stage) {
        out.println("App start");
        wp = new WebProcessor();
        s = new LoginScenario(wp);
        out.println("web engine initialized");
        LoginProcessor.AppParams p = LoginProcessor.appParams;
        synchronized (p) {
            p.wp = wp;
            p.s = s;
            p.notify();
        }
    }

    public void stop() {
        out.println("App stop");
    }

 //   static void main(String[] args) {        launch(args);    }
}
