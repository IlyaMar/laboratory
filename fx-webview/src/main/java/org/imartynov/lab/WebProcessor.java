package org.imartynov.lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;

import static javafx.concurrent.Worker.State.*;


public class WebProcessor {
	public interface LoadCallback {
		void operation(Response state);
	}

	static class Response {
		Throwable e;
		String message;
		
		Response(Throwable e) {
			this.e = e;
		}

		Response(String msg) {
			message = msg;
		}

		void check() {
			if (e != null)
				throw new RuntimeException("exception: " + e.getMessage(), e);
		}
	}
	
	WebEngine webEngine;
	LoadCallback loadCallback;
	
	WebProcessor() {
		webEngine = new WebEngine();
		setUpEngine();
	}
	
	WebEngine getEngine() {
		return webEngine;
	}

	public void setLoadCallback(LoadCallback lc) {
		loadCallback = lc;
	}

	
	void setUpEngine() {
		webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println("state changed: " + oldValue + " -> " + newValue);
			switch (newValue) {
				case SUCCEEDED:
					loadCallback.operation(new Response("load succeeded"));
					break;
				case FAILED:
				case CANCELLED:
					loadCallback.operation(new Response(new RuntimeException("load produced bad page state: " + newValue)));
			}
		});

		webEngine.getLoadWorker().exceptionProperty().addListener((ov, oldException, exception) -> {
            loadCallback.operation(new Response(exception));
        });
	}

}
