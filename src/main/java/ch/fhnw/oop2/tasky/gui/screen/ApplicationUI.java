package ch.fhnw.oop2.tasky.gui.screen;

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

	private Lane todo;
	private Lane doing;
	private Lane done;
	private Lane review;

	private TaskyPresentationModel model;
	private Detail detailView;

	private LaneGroup laneGroup;

	/**
	 * Erzeugt einen neuen MainScreen.
     * @param model
     */
	public ApplicationUI(TaskyPresentationModel model) {
		this.model = model;
		detailView = new Detail(model);
		initializeControls();
		layoutControls();
	}

	private void initializeControls() {
		refreshTaskLanes();
	}

	/**
	 * Anzeigen aller Tasks, eingeordnet in die Lanes
	 */
	private void refreshTaskLanes() {
		// remove laneGroup from gridpane to prevent dead task regions
		getChildren().remove(laneGroup);

		// create private list of all tasks from repo
		List<Task> all_tasks = model.getRepo().read();

		//todo: for each status in AllStatus() do:

		// create lists for each state
		List<Task> todo_tasks = Task.reduceList(all_tasks, Status.Todo);
		List<Task> doing_tasks = Task.reduceList(all_tasks, Status.Doing);
		List<Task> done_tasks = Task.reduceList(all_tasks, Status.Done);
		List<Task> review_tasks = Task.reduceList(all_tasks, Status.Review);

		// create regions for every task in its specific state
		List<Region> todo_regions = model.createRegionsForTasks(todo_tasks, COLORS[Status.Todo.ordinal()]);
		List<Region> doing_regions = model.createRegionsForTasks(doing_tasks, COLORS[Status.Doing.ordinal()]);
		List<Region> done_regions = model.createRegionsForTasks(done_tasks, COLORS[Status.Done.ordinal()]);
		List<Region> review_regions = model.createRegionsForTasks(review_tasks, COLORS[Status.Review.ordinal()]);

		// create lanes for each list of region
		todo = new Lane (Status.Todo.name(), todo_regions);
		doing = new Lane (Status.Doing.name(), doing_regions);
		done = new Lane (Status.Done.name(), done_regions);
		review = new Lane (Status.Review.name(), review_regions);

		laneGroup = new LaneGroup(this, model, todo, doing, done, review);
		add(laneGroup, 0, 0);

	}

	/**
	 * creates layout
	 */
	private void layoutControls() {
		ConstraintHelper.setRowPercentConstraint(this, 100); // Höhe soll generell voll ausgefüllt werden.
		
		ConstraintHelper.setColumnPercentConstraint(this, TASKLANE_PERCENT);
		add(new LaneGroup(this, model, todo, doing, done, review), 0, 0);
		
		ConstraintHelper.setColumnPercentConstraint(this, DETAILS_PERCENT);

		add(detailView, 1, 0);
	}

	/**
	 * refreshes all task lanes
	 */
	public void showTaskInDetail() {
		refreshTaskLanes();
	}

}
