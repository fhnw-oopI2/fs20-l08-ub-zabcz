package ch.fhnw.oop2.tasky.gui.screen;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.oop2.tasky.model.Status;
import ch.fhnw.oop2.tasky.model.Task;
import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * Diese Klasse teilt den Bildschirm in die zwei Hauptgebiete auf:
 * (a) Lane Group
 * (b) Detail-Ansicht einer ausgewählten Task.
 *
 */
public final class ApplicationUI extends GridPane {
	
	private static final int TASKLANE_PERCENT = 60;
	private static final int DETAILS_PERCENT = 40;
	public final static String[] COLORS = { "#2ecc71", "#3498db", "#e74c3c", "#9b59b6" };

	private TaskyPresentationModel model;
	private Detail detailView;

	private LaneGroup laneGroup;
	private Lane todo;
	private Lane doing;
	private Lane done;
	private Lane review;

	/**
	 * Erzeugt einen neuen MainScreen.
     * @param model
     */
	public ApplicationUI(TaskyPresentationModel model) {
		this.model = model;
		detailView = new Detail(model);
		buildLanes();
		layoutControls();
	}

	/**
	 * Anzeigen aller Tasks, eingeordnet in die Lanes
	 */
	public void buildLanes() {
		// remove laneGroup from gridpane to prevent dead task regions
		getChildren().remove(laneGroup);

		List<Lane> lanesList = new ArrayList<>();

		//for each status in AllStatus() do:
		int index = 0;
		for(Status state : Status.getAllStati()) {
			lanesList.add(new Lane(state, (String) Array.get(COLORS, index++), model));
		}
		// prep
		Lane[] lanesArray = new Lane[lanesList.size()] ;
		lanesArray = lanesList.toArray(lanesArray);

		// create lanes for each list of region
		laneGroup = new LaneGroup(model, lanesArray);
		add(laneGroup, 0, 0);

	}

	/**
	 * creates layout
	 */
	private void layoutControls() {
		ConstraintHelper.setRowPercentConstraint(this, 100); // Höhe soll generell voll ausgefüllt werden.
		
		ConstraintHelper.setColumnPercentConstraint(this, TASKLANE_PERCENT);
		//add(new LaneGroup(model, todo, doing, done, review), 0, 0);

		ConstraintHelper.setColumnPercentConstraint(this, DETAILS_PERCENT);

		add(detailView, 1, 0);
	}

	/**
	 * refreshes all task lanes
	 */


}
