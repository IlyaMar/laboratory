package org.imartynov.lab;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.*;

public class LoginScenario {

	public class JavaBridge {
		public void hello() {
			System.out.println("hello from js");
		}

		public void handleEvents(JSObject events) {
            Integer len = (Integer) events.getMember("length");
            List<JSObject> list = new ArrayList<>();
            for (int i =0; i < len; i++) {
                JSObject e = (JSObject) events.getSlot(i);
                list.add(e);
                //System.out.println("event: " + e);
            }
            handleMutationEvents(list);
		}
	}

	static class Parameters {
		String loginUrl = "https://portal.fedsfm.ru/Account/login";
		//String loginUrl = "file:///b:/Project/laboratory/fx-webview/src/test/data/login.html";
		String loginSelector = "document.getElementsByName('loginEditor')[0]";
		String passwordSelector = "document.getElementsByName('passwordEditor')[0]";
		String buttonSelector = "document.getElementsByName('loginButton')[0]";
		String contentSelector = "document.getElementsByClassName('portal-page-content')";

        String loginFailreClass = "validation-summary-errors";

		String testLogin = "5011029307501101001";
		String testPassword = "rivendell";		
	}
	
	private WebProcessor wp;
	private WebEngine e;
	private Parameters p;
	private WebProcessor.Response response;
    private CountDownLatch latchLogin = new CountDownLatch(1);

	LoginScenario(WebProcessor wp) {
		this(wp, new Parameters());
	}
	
	LoginScenario(WebProcessor wp, Parameters p) {
		this.wp = wp;
		this.p = p;
		e = wp.getEngine();
	}

	void saveResponse(WebProcessor.Response r) {
		response = r;
	}

    public Parameters getParameters() {
        return p;
    }


	public void doLoad() throws Exception {

		// loading login form
		CountDownLatch latch = new CountDownLatch(1);
		wp.setLoadCallback(r -> {
				saveResponse(r);
				latch.countDown();
			}
		);
		Platform.runLater(() -> {
			out.println("loading url");
			e.load(p.loginUrl);
		});
		latch.await();
		response.check();
		out.println("login page loaded");

		Platform.runLater(() -> {
			JSObject jsobj = (JSObject) e.executeScript("window");
			jsobj.setMember("java", new JavaBridge());
			out.println("java bridge set");
		});
	}


	void doTest() throws Exception {
		out.println("test started");
		doLoad();

		doLogin();
		out.println("login submitted");

		doCheckLogined();
		out.println("test success!");
	}
	
	
	JSObject findElement(String selector) throws Exception {
		JSObject el = (JSObject) e.executeScript(selector);					
		if (el == null)
			throw new Exception("no element by selector: " + selector);
		return el;
	}


    void handleMutationEvents(List<JSObject> events) {
        for (JSObject e : events) {
            JSObject target = (JSObject) e.getMember("target");
            String cls = (String) target.getMember("className");
            if (cls.equals(p.loginFailreClass)) {
                String s = "mutation event on " + target + ", assume login failure";
                out.println(s);
                response = new WebProcessor.Response(new Exception("login failure, please check login/password"));
                latchLogin.countDown();
				//return;
            }
        }
    }


	void doLogin() throws Exception {
		out.println("logging in");

		wp.setLoadCallback( r -> {
					saveResponse(r);
                    latchLogin.countDown();
				}
		);

		Platform.runLater(() -> {
            try {
				// set up change listener
				e.executeScript("var box = document.querySelector('div.ibox');");
				e.executeScript("var observer = new MutationObserver(function(mutations) {  \n" +
						"    java.handleEvents(mutations);\n" +
						"  });\n");
				e.executeScript("observer.observe(box, {attributes: true, childList: true, characterData: false, subtree: true });");

				out.println("filling login");
                JSObject login = findElement(p.loginSelector);
                login.setMember("value", p.testLogin);

                out.println("filling password");
                JSObject pass = findElement(p.passwordSelector);
                pass.setMember("value", p.testPassword);

                out.println("pressing button");
                JSObject but = findElement(p.buttonSelector);
                but.call("click");
            }
            catch (Exception e) {
                out.println("exception in js!");
                response = new WebProcessor.Response(e);
                latchLogin.countDown();
            }
        });

		out.println("waiting for login...");
        latchLogin.await();
		response.check();
	}

	void doCheckLogined() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			out.println("checking content...");
			JSObject c = (JSObject) e.executeScript(p.contentSelector);
			Integer l = (Integer) c.getMember("length");
			out.println("portal content length: " + l);
			if (l == 1)
				out.println("log in success");
			else
				response = new WebProcessor.Response("login in unsuccess, portal content length: " + l);
			latch.countDown();
		});
		latch.await();
	}

}
