package ch.fhnw.oop2.tasky.gui;

import ch.fhnw.oop2.tasky.gui.screen.ApplicationUI;
import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Diese Klasse ist der Einstiegspunkt in die Tasky-Anwendung.
 *
 */
public final class Tasky extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {

		TaskyPresentationModel model = new TaskyPresentationModel();
		Parent root = new ApplicationUI(model);
		final Scene scene = new Scene(root, 800, 400);
	
		stage.titleProperty().bind(model.stageTitleProperty());
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
