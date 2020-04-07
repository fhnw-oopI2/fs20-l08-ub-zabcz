package ch.fhnw.oop2.tasky.model;

import ch.fhnw.oop2.tasky.gui.screen.Lane;
import ch.fhnw.oop2.tasky.model.impl.InMemoryMapRepository;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.util.List;

public class TaskyPresentationModel {
    // constants
    private static final int MIN_DUMMY_TASKS = 1;
    private static final int MAX_DUMMY_TASKS = 4;

    private Repository repo;
    private final List<Task> tasks;

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

    private Lane todo;
    private Lane doing;
    private Lane done;
    private Lane review;

    public TaskyPresentationModel() {
        // init properties
        stageTitle = new SimpleStringProperty("JavaFX Application Tasky");
        taskSelected = new SimpleLongProperty();
        id = new SimpleLongProperty();
        initListener(taskSelected); // add listener to id
        desc = new SimpleStringProperty();
        title = new SimpleStringProperty();
        date = new SimpleObjectProperty<>(LocalDate.now()); // Localdate
        state = new SimpleObjectProperty<>(Status.Todo); // Status

        // create repo
        repo = new InMemoryMapRepository();
        // fill gui with dummy tasks
        this.initDummyTasks();
        tasks = repo.read();
    }

    /**
     * implement functional interface "ChangeListener"
     */
    private void initListener(LongProperty id) {
        taskSelected.addListener(new ChangeListener(){
            // if id changes, lookup other fileds
            @Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Task temp = getRepo().read(id.get());
                idProperty().set(temp.id);
                title.set(temp.data.title);
                desc.set(temp.data.desc);
                date.set(temp.data.dueDate);
                state.set(temp.data.state);
            }
        });
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
     * refreshes all tasks in repo
     */
    public void refresh() {
        tasks.clear();
        tasks.addAll(repo.read());
    }

    /**
     * fired on click on Button "New"
     * creates new Task in repo with empty TaskData
     */
    public void createTask() {
        System.out.println("createTask entered");
        Task task = this.getRepo().create(new TaskData("", "", LocalDate.now(), Status.Todo));
        this.taskSelectedProperty().set(task.id);
        System.out.println("task_id:" +task.id);
        System.out.println("taskSelectedProperty:" + taskSelectedProperty().get());
        tasks.add(task);
        // cleanup before user enters a task
        cleanUp();
        System.out.println("createTask done");
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

    public void updateForm(LongProperty id){
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
     * Loescht den Task im aktuellen Form und leert das Form
     */
    public void deleteTask(){
        if (this.idProperty().get() != 0 ){
            this.deleteTask(this.idProperty().get());
        }
        // cleanUp();
    }

    /**
     * Handles Mouse click on Region
     */
    public void mouseClickAction(Long id) {
        System.out.println("mouse click fired id:" +id );
        this.taskSelectedProperty().set(id);
        System.out.println("property_id:" +taskSelectedProperty().get() );
        this.updateForm(this.taskSelectedProperty());
        //this.buttonsEnable();
    }

    /**
     * Leert das Form mit eingegebenen Informationen
     */
    public void cleanUp(){
        this.titleProperty().set("");
        this.descProperty().set("");
        this.dateProperty().set(null);
        this.stateProperty().set(null);

        //this.buttonsDisable();
    }

    /**
     * Hook fuer nach dem Klick auf "New".
     * Hier kann init, cleanup, button disable eingefuegt werden.
     * */
    public void setUp() {
        // setup Form
        this.titleProperty().set("");
        this.descProperty().set("");
        this.dateProperty().set(LocalDate.now()); // Localdate
        this.stateProperty().set(Status.Todo); // Status

        // activate buttons
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
