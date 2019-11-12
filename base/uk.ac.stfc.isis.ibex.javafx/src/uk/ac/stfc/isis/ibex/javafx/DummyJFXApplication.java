package uk.ac.stfc.isis.ibex.javafx;

import javafx.stage.Stage;

public class DummyJFXApplication extends javafx.application.Application {
	@Override
	public void start(Stage window) throws Exception {
		System.out.println("Start JFX");
		JFXBackgroundTask.JFX_INITIALIZATION_LATCH.countDown();
		System.out.println("Counted down");
	}
}
