package org.imartynov;

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
	FXTest test;

	TestScenario(FXTest test) {
		this.test = test;
	}

	@Override
	public void run() {
		try {
			test.doTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

public class FXTest extends Application {

	String loginUrl = "https://portal.fedsfm.ru/Account/login";
	String logoutUrl = "https://portal.fedsfm.ru/account/logoff";
	String loginButtonID = "loginButton";
	String loginInputID = "loginEditor";
	String passwordInputID = "passwordEditor";

	String testLogin = "5011029307501101001";
	String testPassword = "rivendell";

	WebEngine webEngine = new WebEngine();
	State pageLoadState;
	private Scene scene;

	@Override
	public void start(Stage stage) {
		// create the scene
		stage.setTitle("Web View");
		Browser b = new Browser();
		webEngine = b.getEngine();

		setUpEngine();
		webEngine.load(loginUrl);

		scene = new Scene(b, 750, 500, Color.web("#666970"));
		stage.setScene(scene);
		// scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
		stage.show();

		new Thread(new TestScenario(this), "test thread").start();
		// doTest();
	}

	synchronized State waitPage() {
		while (pageLoadState == null) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pageLoadState;
	}

	synchronized void setWaitPage(State newState) {
		pageLoadState = newState;
		this.notify();
	}

	void setUpEngine() {
		webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED || newValue == State.FAILED) {
				System.out.println("finished loading, " + newValue);
				setWaitPage(newValue);
				// org.w3c.dom.Document xmlDom = webEngine.getDocument();
				// System.out.println(xmlDom);
			}
		}); // addListener()

		webEngine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> ov, Throwable oldException, Throwable exception) {
				System.out.println("Load Exception: " + exception);
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	void doTest() throws Exception {
		System.out.println("test started");

		State s = waitPage();
		if (s != State.SUCCEEDED)
			return;
		System.out.println("login page loaded");

		doLogin();
		System.out.println("login submitted");

		setWaitPage(null);
		s = waitPage();
		if (s != State.SUCCEEDED)
			return;
		doCheckLogined();
		System.out.println("checked logged in");		
	}

	void doLogin() throws Exception {
		org.w3c.dom.Document dom = webEngine.getDocument();
		Element root = dom.getDocumentElement();

		XPath xpath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xpath.evaluate("//*[@id='loginEditor']", dom, XPathConstants.NODE);

		/*
		 * Element loginField = dom.getElementById(loginInputID); loginField =
		 * (Element) node; if (loginField != null)
		 * loginField.setNodeValue(testLogin); else throw new Exception(
		 * "no login field: " + loginInputID);
		 * 
		 * 
		 * node = (Node) xpath.evaluate("//*[@id='passwordEditor']", dom,
		 * XPathConstants.NODE); Element passwordField =
		 * webEngine.getDocument().getElementById(passwordInputID);
		 * passwordField = (Element) node; if (passwordField != null)
		 * passwordField.setAttribute("value", testPassword); else throw new
		 * Exception("no password field: " + passwordInputID);
		 */

		// HTMLInputElement button = (HTMLInputElement)
		// xpath.evaluate("//*[@id='loginButton']", dom, XPathConstants.NODE);
		// webEngine.executeScript("$('#buttonId')[0].click()");
		System.out.println("submitting form...");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("filling login");
				JSObject login = (JSObject) webEngine.executeScript("document.getElementsByName('loginEditor')[0]");
				login.setMember("value", testLogin);
				System.out.println("filling password");
				JSObject pass = (JSObject) webEngine.executeScript("document.getElementsByName('passwordEditor')[0]");
				pass.setMember("value", testPassword);

				System.out.println("detecting button");
				JSObject but = (JSObject) webEngine.executeScript("document.getElementsByName('loginButton')[0]");
				if (but != null) {
					System.out.println("clicking...");
					Object o = but.call("click");
				} else
					System.out.println("no button: " + loginButtonID);
			}
		});
	}

	void doCheckLogined() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("checking content...");
				JSObject c = (JSObject) webEngine
						.executeScript("document.getElementsByClassName('portal-page-content')");
				System.out.println("content " + c.getMember("length"));
			}
		});
	}

}
