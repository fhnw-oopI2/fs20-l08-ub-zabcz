package ch.fhnw.oop2.tasky.gui.screen;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.oop2.tasky.model.Status;
import ch.fhnw.oop2.tasky.model.Task;
import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * Diese Klasse implementiert den visuellen Behälter für eine Task-Sorte (Todo, Doing, ...).
 * 
 */
public final class Lane extends GridPane {

	private final static int MAX_TASKS_PER_LANE = 5;

	private TaskyPresentationModel model;
	private Label label;
	private List<Region> allTasks;
	private Status status;
	private String color;
	private int row;
	
	/**
	 * Erzeugt eine neue Lane.
	 * 
	 * @param state Der Labeltext für die Lane
	 * @param color Farbe der einzelnen Tasks in dieser Lane
	 * @param model Presetntation Model
	 */
	public Lane(Status state, String color, TaskyPresentationModel model) {
		this.status = state;
		this.color = color;
		this.model = model;
		initializeControls();
		layoutControls();
	}
	
	private void initializeControls() {
		// setup laben on top of lane
		label = new Label(status.toString());
	}

	/**
	 * creates for each task with specific state new region
	 * and adds region to lane
	 */
	private void layoutControls() {
		// cleanup all elements of lane (old regions for task)
		getChildren().clear();
		getRowConstraints().clear();
		getColumnConstraints().clear();
		row = 1;

		allTasks = createRegionsForTasks(model.getRepo().readByStatus(status), color);

		// Nur eine Spalte für diese Lane.
		ConstraintHelper.setColumnPercentConstraint(this, 100);
		
		// Für das Label.
		add(label, 0, 0);
		ConstraintHelper.setRowPercentConstraint(this, 5);
		GridPane.setMargin(label, new Insets(0, 0, 0, 3));
		
		// Padding für die Lane.
		setPadding(new Insets(5));

		allTasks.stream()
			.forEach(task -> {
				ConstraintHelper.setRowPercentConstraint(this, 95.0 / MAX_TASKS_PER_LANE);
				add(task, 0, row++);
				GridPane.setMargin(task, new Insets(3));
			});
	}

	/**
	 * creates region for every task with color
	 * @param color Color as Hex-String
	 * @param tasks List von Tasks
	 * @return  Liste von neuen Regions
	 */
	public List<Region> createRegionsForTasks(List<Task> tasks, String color) {
		List<Region> tasks_as_region = new ArrayList<>();

		for (Task t : tasks) {
			// create region with color and title
			Region region = Task.createRegionWithText(color, t.data.title);
			// create click handler on every region
			region.onMouseClickedProperty().set(event -> model.mouseClickAction(t.id)); //taskSelected.set(t.id));
			// add region to list of regions
			tasks_as_region.add(region);
		}
		return tasks_as_region;
	}

}
