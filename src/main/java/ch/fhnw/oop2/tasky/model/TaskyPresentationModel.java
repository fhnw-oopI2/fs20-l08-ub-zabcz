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

import java.time.LocalDate;

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

        // sync property of task id
        //taskSelected.bindBidirectional(detailView.getTaskIdProperty());

        repo = new InMemoryMapRepository();
        System.out.println("pm done");


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
