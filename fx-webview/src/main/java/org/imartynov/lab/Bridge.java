package org.imartynov.lab;

import javafx.application.Platform;
import netscape.javascript.JSObject;

public class Bridge {
    public void exit() {
        Platform.exit();
    }

    public void onchange(JSObject event) {
        System.out.println("event: " + event);
    }


}

