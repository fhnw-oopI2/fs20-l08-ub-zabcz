package ch.fhnw.oop2.tasky.gui.screen;

import java.util.stream.IntStream;

import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Diese Klasse gruppiert die Lanes auf dem Bildschirm. Sie sorgt dafür, dass die Lanes
 * resizable sind.
 *
 */
public final class LaneGroup extends GridPane {

	private TaskyPresentationModel model;

	private static double ONE_HUNDRED_PERCENT = 100.0;
	private static double BOTTOM_HEIGHT_PERCENT = 10.0;
	private static double LANE_HEIGHT_PERCENT = ONE_HUNDRED_PERCENT - BOTTOM_HEIGHT_PERCENT;
	
	private Button btnCreate;
	private Button btnRefresh;

	/**
	 * Erzeugt eine neue LaneGroup.
	 *
	 * @param lanes Die Lanes in der Gruppe
	 */
	public LaneGroup(TaskyPresentationModel model, Lane... lanes) {
		this.model = model;
		initializeControls();
		layoutControls(lanes);
	}
	
	private void initializeControls() {
		btnCreate = new Button("New");
		btnRefresh = new Button("Refresh");

		// set actoin on button click
		btnCreate.setOnAction(event -> model.createTask());
		btnRefresh.setOnAction(event -> model.refresh());
	}
	
	private void layoutControls(Lane... lanes) {
		ConstraintHelper.setRowPercentConstraint(this, LANE_HEIGHT_PERCENT);
		
		IntStream.range(0, lanes.length)
			.forEach(index -> {				
				ConstraintHelper.setColumnPercentConstraint(this, ONE_HUNDRED_PERCENT / lanes.length);				
				add(lanes[index], index, 0);
			});
		
		ConstraintHelper.setRowPercentConstraint(this, BOTTOM_HEIGHT_PERCENT);
		
		HBox buttons = new HBox();
		buttons.setSpacing(10);
		buttons.getChildren().addAll(btnCreate, btnRefresh);
		add(buttons, 0, 1, lanes.length, 1);
		GridPane.setMargin(buttons, new Insets(7, 0, 5, 10));
	}
}
