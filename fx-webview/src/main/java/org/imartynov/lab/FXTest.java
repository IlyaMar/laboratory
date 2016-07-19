package org.imartynov.lab;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

class TestScenario implements Runnable {
	WebProcessor wp;
	LoginScenario s;			
	
	void init() {
		wp = new WebProcessor();
		s = new LoginScenario(wp);			
	}

	@Override
	public void run() {
		try {
			s.doTest();
		} catch (Exception e) {
			System.out.println("exception during test!");
			e.printStackTrace();
		}
		finally {
		}
	}
}

public class FXTest extends Application {
	//private Scene scene;

	@Override
	public void start(Stage stage) {
		// create the scene
		//stage.setTitle("Web View");
		//Browser b = new Browser();
		//webEngine = b.getEngine();


		//scene = new Scene(b, 750, 500, Color.web("#666970"));
		//stage.setScene(scene);
		// scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
		//stage.show();

		TestScenario ts = new TestScenario();
		ts.init();
		new Thread(ts, "scenario thread").start();
	}


	public static void main(String[] args) throws Exception {
		launch(args);
	}


}
