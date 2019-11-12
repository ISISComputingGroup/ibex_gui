package uk.ac.stfc.isis.ibex.javafx;

import javafx.application.Platform;
import javafx.stage.Stage;

public class DummyJFXApplication extends javafx.application.Application {
	@Override
	public void start(Stage window) throws Exception {
		System.out.println("Start JFX");
		
		window.setTitle("IBEX JavaFX background worker");
		window.hide();
		
		
		
		System.out.println("Set window properties");
		Platform.runLater(JFXBackgroundTask.JFX_INITIALIZATION_LATCH::countDown);
		System.out.println("Queued task");
	}
}
