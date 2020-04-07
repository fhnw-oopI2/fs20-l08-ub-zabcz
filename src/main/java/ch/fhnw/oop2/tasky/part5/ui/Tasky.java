package ch.fhnw.oop2.tasky.part5.ui;

import ch.fhnw.oop2.tasky.part5.ui.screen.ApplicationUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Diese Klasse ist der Einstiegspunkt in die Tasky-Anwendung.
 *
 */
public final class Tasky extends Application {

	private static final String STAGE_TITLE = "Tasky";
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = new ApplicationUI();
		
		final Scene scene = new Scene(root, 800, 400);
	
		stage.setTitle(STAGE_TITLE);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}

	/**
	 * Main-Entry-Point der Anwendung.
	 * 
	 * @param args Der String Array mit den Programmargumenten
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

}
