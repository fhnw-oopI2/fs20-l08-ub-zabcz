package ch.fhnw.oop2.tasky.model;

import ch.fhnw.oop2.tasky.model.impl.InMemoryMapRepository;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskyPresentationModel {
    // constants
    private static final int MIN_DUMMY_TASKS = 1;
    private static final int MAX_DUMMY_TASKS = 4;

    private Repository repo;

    // Properties
    private final StringProperty stageTitle;
    // sync property for task id
    private final LongProperty taskSelected;
    // detail properties
    private final LongProperty id;
    private final StringProperty desc;
    private final StringProperty title;
    private final ObjectProperty<LocalDate> date;
    private final ObjectProperty<Status> state;

    public TaskyPresentationModel() {
        // init properties
        stageTitle = new SimpleStringProperty("JavaFX Application Tasky");
        taskSelected = new SimpleLongProperty();
        id = new SimpleLongProperty();
        initListener(id); // add listener to id
        desc = new SimpleStringProperty();
        title = new SimpleStringProperty();
        date = new SimpleObjectProperty<>(LocalDate.now()); // Localdate
        state = new SimpleObjectProperty<>(Status.Todo); // Status

        // create repo
        repo = new InMemoryMapRepository();
        // fill gui with dummy tasks
        this.initDummyTasks();
    }

    /**
     * implement functional interface "ChangeListener"
     */
    private void initListener(LongProperty id) {
        id.addListener(new ChangeListener(){
            // if id changes, lookup other fileds
            @Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Task temp = getRepo().read(id.get());
                title.set(temp.data.title);
                desc.set(temp.data.desc);
                date.set(temp.data.dueDate);
                state.set(temp.data.state);
            }
        });
    }

    /**
     * fired on click on Button "New"
     * creates new Task in repo with empty TaskData
     */
    public void newTask() {
        System.out.println(this.getClass() + "newTask()");
        Task task = this.getRepo().create(new TaskData("", "", LocalDate.now(), Status.Todo));
        this.taskSelectedProperty().set(task.id);
        System.out.println(this.taskSelectedProperty().toString());
        //refreshTaskLanes();
        System.out.println("create");
        //detailView.setUp();
    }

    /**
     * Speichert den eingegebenen Task im repository
     */
    public void updateTask(Task tmp) {
        repo.update(tmp);
    }

    /**
     * Löscht den neu erstellten Task aus dem repo
     */
    public void deleteTask(long task_id) {
        repo.delete(task_id);
    }

    /**
     * generiert pro Status im Enums 'Status' Tasks.
     * Die Anzahl ist zufällig, aber beschränkt durch obere- und untere Schranken:
     * MIN_DUMMY_TASKS und MAX_DUMMY_TASKS
     */
    public void initDummyTasks() {
        int counter=1;
        for(Status status : Status.getAllStati()){
            int randomNum = MIN_DUMMY_TASKS + (int)(Math.random()*MAX_DUMMY_TASKS);
            for (int i = 0; i < randomNum; i++) {
                this.getRepo().create(new TaskData("Task0"+counter, "Task von Tasky v5.0", LocalDate.now(), status));
                counter++;
            }
        }

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
            region.onMouseClickedProperty().set(event -> this.mouseClickAction(t.id)); //taskSelected.set(t.id));
            // add region to list of regions
            tasks_as_region.add(region);
        }
        return tasks_as_region;
    }

    /**
     * Loescht den Task im aktuellen Form und leert das Form
     */
    public void deleteTask(){
        if (this.idProperty().get() != 0 ){
            this.deleteTask(this.idProperty().get());
        }
       // cleanUp();
    }

    public void updateFrom(LongProperty id){
        Task temp = this.getRepo().read(id.get());
        this.titleProperty().set(temp.data.title);
        this.descProperty().set(temp.data.desc);
        this.dateProperty().set(temp.data.dueDate);
        this.stateProperty().set(temp.data.state);
    }

    /**
     * Speichert die eingegeben Infos als Task temporär ab und gibt die weiter ans AppGui
     */
    public void saveTask(){
        TaskData taskdata = new TaskData(this.titleProperty().get(), this.descProperty().get(), this.dateProperty().get(), this.stateProperty().get());
        Task tmp = new Task(this.idProperty().get(), taskdata);
        System.out.println("detail: " + taskdata.toString());
        this.updateTask(tmp);
        //cleanUp();
    }

    /**
     * Handles Mouse click on Region
     */
    public void mouseClickAction(Long id) {
        //System.out.println("mouse click fired");
        this.taskSelectedProperty().set(id);
        this.updateFrom(this.taskSelectedProperty());
        //this.buttonsEnable();
    }


    /**
     * Getter: repo
     */
    public Repository getRepo() {
        return repo;
    }

    // Property-Getter
    public StringProperty stageTitleProperty() {
        return stageTitle;
    }

    public long getTaskSelected() {
        return taskSelected.get();
    }

    public LongProperty taskSelectedProperty() {
        return taskSelected;
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public String getDesc() {
        return desc.get();
    }

    public StringProperty descProperty() {
        return desc;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public Status getState() {
        return state.get();
    }

    public ObjectProperty<Status> stateProperty() {
        return state;
    }
}
