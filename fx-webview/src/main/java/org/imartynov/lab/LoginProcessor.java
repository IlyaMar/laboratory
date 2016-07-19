package org.imartynov.lab;

import javafx.application.Platform;

import static java.lang.System.out;


public class LoginProcessor {
    static class AppParams {
        public WebProcessor wp;
        public LoginScenario s;
        public Exception ex;
    }

    static AppParams appParams = new AppParams();

    public void prepare() throws InterruptedException {
        synchronized(appParams) {
            new Thread(() -> {
                out.println("starting FX app");
                //synchronized(appParams) {
                    try {
                        App.launch(App.class, "");
                    } catch (Exception e) {
                        appParams.ex = e;
                    }
                //    appParams.notify();
                //}

            }, "FX app init thread").start();
            out.println("waiting for App to start");
            appParams.wait();
            if (appParams.ex != null)
                throw new RuntimeException("failed to launch App", appParams.ex);
        }
        out.println("preparation complete");
    }


    public LoginScenario.Parameters getParameters() {
        return appParams.s.getParameters();
    }

    public void start() throws Exception {
        appParams.s.doTest();
    }

    public void stop() {
        out.println("stopping App");
        Platform.exit();
    }

}







