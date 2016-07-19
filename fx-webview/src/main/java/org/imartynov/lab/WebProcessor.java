package org.imartynov.lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;




public class WebProcessor {
	public interface LoadCallback {
		void operation(Response state);
	}

	static class Response {
		Throwable e;
		State s;
		
		Response(Throwable e, State s) {
			this.e = e;
			this.s = s;
		}
		
		void check() {
			if (e != null)
				throw new RuntimeException("webview throwed exception!", e);
			if (s!= null && s != State.SUCCEEDED)
				throw new RuntimeException("webview returned bad page state: " + s);
		}
	}
	
	WebEngine webEngine;
	//Response response;
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


	/*synchronized void waitResponse(long timeout) {
		while (response == null) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("waitPage interrupted!");
				return;
			}
		}
		response.check();
		response = null;
	}
	
	synchronized void setResponse(Response r) {
		response = r;
		this.notify();
	}

	void setResponseSuccess() {
		setResponse(new Response(null, null));
	}*/

	
	void setUpEngine() {
		webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println("state changed: " + oldValue + " -> " + newValue);
			if (newValue == State.SUCCEEDED || newValue == State.FAILED || newValue == State.CANCELLED) {
				//setResponse(new Response(null, newValue));
				loadCallback.operation(new Response(null, newValue));
			}
		});

		webEngine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> ov, Throwable oldException, Throwable exception) {
				//setResponse(new Response(exception, null));
				loadCallback.operation(new Response(exception, null));
			}
		});
	}

}
