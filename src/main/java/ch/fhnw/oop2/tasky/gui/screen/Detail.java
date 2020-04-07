package ch.fhnw.oop2.tasky.gui.screen;

import ch.fhnw.oop2.tasky.model.Status;
import ch.fhnw.oop2.tasky.model.Task;
import ch.fhnw.oop2.tasky.model.TaskData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;

/**
 * Diese Klasse sorgt dafür, dass alle Controls für die Detailansicht 
 * vorhanden sind und richtig platziert werden.
 * 
 * Das erweiterte GridPane wird auf zwei Spalten aufgeteilt:
 * Linke Spalte: Labels
 * Rechte Spalte: Input Controls.
 *
 */
final class Detail extends GridPane {

	private ApplicationUI gui;

	private Label labelId;
	private Label labelTitle;
	private Label labelDescription;
	private Label labelDate;
	private Label labelState;
	
	private TextField idField;
	private TextField titleField;
	private TextArea descriptionField;
	
	private DatePicker datePicker;
	private ComboBox<Status> stateDropDown;
	
	private Button buttonNew;
	private Button buttonDelete;

	private final LongProperty id;
	private final StringProperty desc;
	private final StringProperty title;
	private final ObjectProperty<LocalDate> date;
	private final ObjectProperty<Status> state;

	
	/**
	 * Erzeugt eine neue Detailansicht.
	 */
	Detail(ApplicationUI gui) {
		this.gui = gui;
		id = new SimpleLongProperty();
		initListener(id);
		desc = new SimpleStringProperty();
		title = new SimpleStringProperty();
		date = new SimpleObjectProperty<>(LocalDate.now()); // Localdate
		state = new SimpleObjectProperty<>(Status.Todo); // Status
		initializeControls();
		layoutControls();
	}

	/**
	 * implement functional interface "ChangeListener"
	 */
	private void initListener(LongProperty id) {
		id.addListener(new ChangeListener(){
			// if id changes, lookup other fileds
			@Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Task temp = gui.getRepo().read(id.get());
				title.set(temp.data.title);
				desc.set(temp.data.desc);
				date.set(temp.data.dueDate);
				state.set(temp.data.state);
			}
		});
	}

	/**
	 * Erzeugt alle control elemente
	 */
	private void initializeControls() {
		labelId = new Label("ID");
		labelTitle = new Label("Title");
		labelDescription = new Label("Desc");
		labelDate = new Label("Date");
		labelState = new Label("State");

		idField = new TextField();
		idField.setDisable(true);
		// bind to AppUI
		idField.textProperty().bindBidirectional(id, new NumberStringConverter());
		idField.textProperty().bindBidirectional(id, new NumberStringConverter());

		titleField = new TextField();
		titleField.textProperty().bindBidirectional(title); // String to String - no Converter
		descriptionField = new TextArea();
		descriptionField.textProperty().bindBidirectional(desc); // String to String - no Converter

		datePicker = new DatePicker();
		datePicker.valueProperty().bindBidirectional(date); // value property wie bei slider
		stateDropDown = new ComboBox<>();
		stateDropDown.getItems().addAll(Status.getAllStati());
		stateDropDown.valueProperty().bindBidirectional(state); // value property wie bei slider
		
		buttonNew = new Button("Save");
		buttonDelete = new Button("Delete");

		buttonNew.setOnAction(event -> saveTask());
		buttonDelete.setOnAction(event -> deleteTask());
		buttonsDisable();
	}

	/**
	 * Erzeugt das ganze layout (gegeben)
	 */
	private void layoutControls() {
		setPadding(new Insets(22, 30, 22, 30));
		
		ConstraintHelper.setColumnPercentConstraint(this, 20);
		ConstraintHelper.setColumnPercentConstraint(this, 80);
		
		add(labelId, 0, 0);
		add(idField, 1, 0);
		
		add(labelTitle, 0, 1);
		add(titleField, 1, 1);
		GridPane.setMargin(titleField, new Insets(10, 0, 0, 0));
		
		descriptionField.setMaxHeight(100);
		add(labelDescription, 0, 2);
		add(descriptionField, 1, 2);
		GridPane.setMargin(descriptionField, new Insets(10, 0, 0, 0));
		
		add(labelDate, 0, 3);
		add(datePicker, 1, 3);
		datePicker.setMaxWidth(Double.MAX_VALUE);
		GridPane.setMargin(datePicker, new Insets(10, 0, 0, 0));
		
		add(labelState, 0, 4);
		add(stateDropDown, 1, 4);
		stateDropDown.setMaxWidth(Double.MAX_VALUE);
		GridPane.setMargin(stateDropDown, new Insets(10, 0, 0, 0));
		
		// Buttons werden als HBox mit Colspan hinzugefügt.
		HBox buttons = new HBox();
		buttons.setSpacing(10);
		buttons.getChildren().addAll(buttonNew, buttonDelete);
		add(buttons, 0, 5, 2, 1);
		GridPane.setMargin(buttons, new Insets(20, 0, 0, 0));
	}

	/**
	 * Gibt das ID Property zurueck, wie in AB 04.
	 */
	public LongProperty getTaskIdProperty() {
		return id;
	}

	/**
	 * Speichert die eingegeben Infos als Task temporär ab und gibt die weiter ans AppGui
	 */
	public void saveTask(){
		TaskData taskdata = new TaskData(title.get(), desc.get(), date.get(), state.get());
		Task tmp = new Task(id.get(), taskdata);
		System.out.println("detail: " + taskdata.toString());
		gui.updateTask(tmp);
		cleanUp();
	}

	/**
	 * Leert das Form mit eingegebenen Informationen
	 */
	public void cleanUp(){
		title.set("");
		desc.set("");
		date.set(null);
		state.set(null);

		buttonsDisable();
	}

	/**
	 * Hook fuer nach dem Klick auf "New".
	 * Hier kann init, cleanup, button disable eingefuegt werden.
	 * */
	public void setUp() {
		// setup Form
		title.set("");
		desc.set("");
		date.set(LocalDate.now()); // Localdate
		state.set(Status.Todo); // Status

		// activate buttons
		buttonsEnable();
	}

	/**
	 * Loescht den Task im aktuellen Form und leert das Form
	 */
	public void deleteTask(){
		if (id.get() != 0 ){
			gui.deleteTask(id.get());
		}
		cleanUp();
	}

	public void buttonsEnable(){
		buttonNew.setDisable(false);
		buttonDelete.setDisable(false);
	}

	public void buttonsDisable(){
		buttonNew.setDisable(true);
		buttonDelete.setDisable(true);
	}

	public void updateFrom(LongProperty id){
		Task temp = gui.getRepo().read(id.get());
		title.set(temp.data.title);
		desc.set(temp.data.desc);
		date.set(temp.data.dueDate);
		state.set(temp.data.state);
	}
}
