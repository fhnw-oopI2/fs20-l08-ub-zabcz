package ch.fhnw.oop2.tasky.model;

import ch.fhnw.oop2.tasky.model.impl.InMemoryMapRepository;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class TaskyPresentationModel {
    // constants
    public static final int MIN_DUMMY_TASKS = 1;
    public static final int MAX_DUMMY_TASKS = 4;

    private Repository repo;
    private ObservableList<Task> tasks;

    // Properties
    private final StringProperty stageTitle;
    // sync property for task id
    private final LongProperty taskSelected;
    // detail form properties
    private final LongProperty id;
    private final StringProperty desc;
    private final StringProperty title;
    private final ObjectProperty<LocalDate> date;
    private final ObjectProperty<Status> state;
    private final StringProperty buttonSaveText;
    private final StringProperty buttonDeleteText;
    private final BooleanProperty buttonSaveDisable;
    private final BooleanProperty buttonDeleteDisable;

    // public ctor
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
        // set text of each button and set each button to false, when starting application
        buttonSaveText = new SimpleStringProperty("Safe");
        buttonDeleteText = new SimpleStringProperty("Delete");
        buttonSaveDisable = new SimpleBooleanProperty(true);
        buttonDeleteDisable = new SimpleBooleanProperty(true);

        // create repo
        repo = new InMemoryMapRepository();
        tasks = FXCollections.observableArrayList();
        // fill gui with dummy tasks
        this.initDummyTasks();
        tasks.addAll(repo.read());
    }

    /*************************************************************
     *                                                           *
     *                       INIT Section                        *
     *                                                           *
     *************************************************************/
    /**
     * implement functional interface "ChangeListener"
     */
    private void initListener(LongProperty id) {
        taskSelected.addListener(new ChangeListener(){
            // if id changes, lookup other fileds
            @Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (id.get() != 0) { // id = 0 means, task has been deleted
                    Task temp = getRepo().read(id.get());
                    idProperty().set(temp.id);
                    title.set(temp.data.title);
                    desc.set(temp.data.desc);
                    date.set(temp.data.dueDate);
                    state.set(temp.data.state);
                } else{
                    // if task deleted (new id = 0), cleanup form completely
                    cleanForm();
                }
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

    /*************************************************************
     *                                                           *
     *                       CRUD                                *
     *                                                           *
     *************************************************************/
    /**
     * fired on click on Button "New"
     * creates new Task in repo with empty TaskData
     */
    public void createTask() {
        Task task = this.getRepo().create(new TaskData("", "", LocalDate.now(), Status.Todo));
        this.taskSelectedProperty().set(task.id);
        tasks.add(task);
        // cleanup before user enters a task
        setupForm();
    }

    /**
     * Speichert die eingegeben Infos als Task temporär ab und gibt die weiter ans AppGui
     */
    public void saveTask(){
        TaskData taskdata = new TaskData(this.titleProperty().get(), this.descProperty().get(), this.dateProperty().get(), this.stateProperty().get());
        Task tmp = new Task(this.idProperty().get(), taskdata);
        repo.update(tmp);
        refresh();
    }

    /**
     * delete Task
     * delete Task in repo and delete task from observable list
     */
    public void deleteTask(){
        if (this.idProperty().get() != 0 ){
            // delete from repo
            this.repo.delete(this.idProperty().get());
            // set 0, means no task selected in gui
            taskSelected.set(0);
            // remove deleted task from observable list
            tasks.remove(getRepo().read(this.idProperty().get()));
            // refresh gui, to prevent dead tasks displayed in gui
            refresh();
        }
    }
    /*************************************************************
     *                                                           *
     *                  FORM Actions                             *
     *                                                           *
     *************************************************************/
    /**
     * Leert das Form mit eingegebenen Informationen
     */
    public void cleanForm(){
        this.titleProperty().set("");
        this.descProperty().set("");
        this.dateProperty().set(null);
        this.stateProperty().set(null);

        buttonsDisable();
    }

    /**
     * Hook fuer nach dem Klick auf "New".
     * Hier kann init, cleanup, button disable eingefuegt werden.
     * */
    public void setupForm() {
        // setup Form
        this.titleProperty().set("");
        this.descProperty().set("");
        this.dateProperty().set(LocalDate.now()); // Localdate
        this.stateProperty().set(Status.Todo); // Status

        // activate buttons
        buttonsEnable();
    }

    /**
     * sets button "Safe" and "delete" to visible
     * */
    public void buttonsEnable(){
        setButtonSaveDisable(false);
        setButtonDeleteDisable(false);
    }

    /**
     * sets button "Safe" and "delete" to disabled
     * */
    public void buttonsDisable(){
        setButtonSaveDisable(true);
        setButtonDeleteDisable(true);
    }

    /*************************************************************
     *                                                           *
     *                  EVENT Actions                            *
     *                                                           *
     *************************************************************/
    /**
     * Handles Mouse click on Region
     */
    public void mouseClickAction(Long id) {
        this.taskSelectedProperty().set(id);
        this.updateForm(this.taskSelectedProperty());
        buttonsEnable();
    }

    /**
     * Handles MouseClick-Action on a task to update Form
     * */
    public void updateForm(LongProperty id){
        Task temp = this.getRepo().read(id.get());
        this.titleProperty().set(temp.data.title);
        this.descProperty().set(temp.data.desc);
        this.dateProperty().set(temp.data.dueDate);
        this.stateProperty().set(temp.data.state);
    }

    /*************************************************************
     *                                                           *
     *                 GETTER and SETTER                         *
     *                                                           *
     *************************************************************/
    /**
     * Getter: repo
     */
    public Repository getRepo() {
        return repo;
    }

    public ObservableList<Task> tasksList() {
        return tasks;
    }

    // Property-Getter and Setter
    public void setButtonSaveDisable(boolean buttonSaveDisable) {
        this.buttonSaveDisable.set(buttonSaveDisable);
    }

    public void setButtonDeleteDisable(boolean buttonDeleteDisable) {
        this.buttonDeleteDisable.set(buttonDeleteDisable);
    }

    public StringProperty buttonSaveTextProperty() {
        return buttonSaveText;
    }


    public StringProperty buttonDeleteTextProperty() {
        return buttonDeleteText;
    }


    public BooleanProperty buttonSaveDisableProperty() {
        return buttonSaveDisable;
    }


    public BooleanProperty buttonDeleteDisableProperty() {
        return buttonDeleteDisable;
    }

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
