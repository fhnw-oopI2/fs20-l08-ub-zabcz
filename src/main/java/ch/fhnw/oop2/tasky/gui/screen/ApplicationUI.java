package ch.fhnw.oop2.tasky.gui.screen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.oop2.tasky.model.Repository;
import ch.fhnw.oop2.tasky.model.Status;
import ch.fhnw.oop2.tasky.model.Task;
import ch.fhnw.oop2.tasky.model.TaskData;
import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import ch.fhnw.oop2.tasky.model.impl.InMemoryMapRepository;
import javafx.beans.property.LongProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.beans.property.SimpleLongProperty;

/**
 * Diese Klasse teilt den Bildschirm in die zwei Hauptgebiete auf:
 * (a) Lane Group
 * (b) Detail-Ansicht einer ausgewählten Task.
 *
 */
public final class ApplicationUI extends GridPane {
	
	private static final int TASKLANE_PERCENT = 60;
	private static final int DETAILS_PERCENT = 40;
	private static final int MIN_DUMMY_TASKS = 1;
	private static final int MAX_DUMMY_TASKS = 4;
	public final static String[] COLORS = { "#2ecc71", "#3498db", "#e74c3c", "#9b59b6" };

	private Lane todo;
	private Lane doing;
	private Lane done;
	private Lane review;

	private Repository repo;
	private Detail detailView;


	private final LongProperty taskSelected;
	private LaneGroup laneGroup;

	/**
	 * Erzeugt einen neuen MainScreen.
     * @param model
     */
	public ApplicationUI(TaskyPresentationModel model) {
		repo = new InMemoryMapRepository();
		detailView = new Detail(this);
		initDummyTasks();
		// init property
		taskSelected = new SimpleLongProperty();
		// sync property of task id
		taskSelected.bindBidirectional(detailView.getTaskIdProperty());

		initializeControls();
		layoutControls();
	}
	private void initDummyTasks() {
		int counter=1;
		for(Status status : Status.getAllStati()){
			int randomNum = MIN_DUMMY_TASKS + (int)(Math.random()*MAX_DUMMY_TASKS);
			for (int i = 0; i < randomNum; i++) {
				repo.create(new TaskData("Task0"+counter, "Task von Tasky v5.0", LocalDate.now(), status));
				counter++;
			}
		}

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
		List<Task> all_tasks = repo.read();

		//todo: for each status in AllStatus() do:
//		for(Status status : Status.getAllStati()){
//			int randomNum = MIN_DUMMY_TASKS + (int)(Math.random()*MAX_DUMMY_TASKS);
//			for (int i = 0; i < randomNum; i++) {
//				repo.create(new TaskData("Task0"+counter, "Task von Tasky v5.0", LocalDate.now(), status));
//				counter++;
//			}
//		}
		// create lists for each state
		List<Task> todo_tasks = Task.reduceList(all_tasks, Status.Todo);
		List<Task> doing_tasks = Task.reduceList(all_tasks, Status.Doing);
		List<Task> done_tasks = Task.reduceList(all_tasks, Status.Done);
		List<Task> review_tasks = Task.reduceList(all_tasks, Status.Review);

		// create regions for every task in its specific state
		List<Region> todo_regions = createRegionsForTasks(todo_tasks, COLORS[Status.Todo.ordinal()]);
		List<Region> doing_regions = createRegionsForTasks(doing_tasks, COLORS[Status.Doing.ordinal()]);
		List<Region> done_regions = createRegionsForTasks(done_tasks, COLORS[Status.Done.ordinal()]);
		List<Region> review_regions = createRegionsForTasks(review_tasks, COLORS[Status.Review.ordinal()]);

		// create lanes for each list of region
		todo = new Lane (Status.Todo.name(), todo_regions);
		doing = new Lane (Status.Doing.name(), doing_regions);
		done = new Lane (Status.Done.name(), done_regions);
		review = new Lane (Status.Review.name(), review_regions);

		laneGroup = new LaneGroup(this, todo, doing, done, review);
		add(laneGroup, 0, 0);

	}

	/**
	 * creates layout
	 */
	private void layoutControls() {
		ConstraintHelper.setRowPercentConstraint(this, 100); // Höhe soll generell voll ausgefüllt werden.
		
		ConstraintHelper.setColumnPercentConstraint(this, TASKLANE_PERCENT);
		add(new LaneGroup(this, todo, doing, done, review), 0, 0);
		
		ConstraintHelper.setColumnPercentConstraint(this, DETAILS_PERCENT);

		add(detailView, 1, 0);
	}

	/**
	 * creates region for every task with color
	 * @param color Color as Hex-String
	 * @param tasks List von Tasks
	 * @return  Liste von neuen Regions
	 */
	private List<Region> createRegionsForTasks(List<Task> tasks, String color) {
		List<Region> tasks_as_region = new ArrayList<>();

		for (Task t : tasks) {
			// create region with color and title
			Region region = Task.createRegionWithText(color, t.data.title);
			// create click handler on every region
			region.onMouseClickedProperty().set(event -> mouseClickAction(t.id)); //taskSelected.set(t.id));
			// add region to list of regions
			tasks_as_region.add(region);
		}
		return tasks_as_region;
	}

	/**
	 * fired on click on Button "New"
	 * creates new Task in repo with empty TaskData
	 */
	public void newTask() {
		System.out.println(this.getClass() + "newTask()");
		Task task = repo.create(new TaskData("", "", LocalDate.now(), Status.Todo));
		taskSelected.set(task.id);
		System.out.println(taskSelected.toString());
		refreshTaskLanes();
		System.out.println("create");
		detailView.setUp();
	}

	/**
	 * refreshes all task lanes
	 */
	public void showTaskInDetail() {
		refreshTaskLanes();
	}

	/**
	 * Speichert den eingegebenen Task im repository
	 */
	public void updateTask(Task tmp) {
		repo.update(tmp);
		refreshTaskLanes();
	}

	/**
	 * Löscht den neu erstellten Task aus dem repo
	 */
	public void deleteTask(long task_id) {
		repo.delete(task_id);
		refreshTaskLanes();
	}

	/**
	 * Handles Mouse click on Region
	 */
	public void mouseClickAction(Long id) {
		//System.out.println("mouse click fired");
		taskSelected.set(id);
		detailView.updateFrom(taskSelected);
		detailView.buttonsEnable();
	}

	/**
	 * Getter: repo
	 */
	public Repository getRepo() {
		return repo;
	}
}
