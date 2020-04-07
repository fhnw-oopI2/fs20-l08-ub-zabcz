package ch.fhnw.oop2.tasky.gui.screen;

import ch.fhnw.oop2.tasky.model.Status;
import ch.fhnw.oop2.tasky.model.Task;
import ch.fhnw.oop2.tasky.model.TaskData;
import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import javafx.beans.property.LongProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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

	private TaskyPresentationModel model;

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
	
	private Button buttonSave;
	private Button buttonDelete;
	
	/**
	 * Erzeugt eine neue Detailansicht.
	 *
	 * @param model Presentation Model
	 */
	Detail(TaskyPresentationModel model) {
		this.model = model;
		System.out.println("detail entered");
		initializeControls();
		layoutControls();
		System.out.println("detail done");
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
		System.out.println("before binding");
		idField.textProperty().bind(model.idProperty().asString());
		System.out.println("after binding");
		titleField = new TextField();
		titleField.textProperty().bindBidirectional(model.titleProperty()); // String to String - no Converter
		descriptionField = new TextArea();
		descriptionField.textProperty().bindBidirectional(model.descProperty()); // String to String - no Converter

		datePicker = new DatePicker();
		datePicker.valueProperty().bindBidirectional(model.dateProperty()); // value property wie bei slider
		stateDropDown = new ComboBox<>();
		stateDropDown.getItems().addAll(Status.getAllStati());
		stateDropDown.valueProperty().bindBidirectional(model.stateProperty()); // value property wie bei slider
		
		buttonSave = new Button("Save");
		buttonDelete = new Button("Delete");

		buttonSave.setOnAction(event -> saveTask());
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
		buttons.getChildren().addAll(buttonSave, buttonDelete);
		add(buttons, 0, 5, 2, 1);
		GridPane.setMargin(buttons, new Insets(20, 0, 0, 0));
	}

	/**
	 * Speichert die eingegeben Infos als Task temporär ab und gibt die weiter ans AppGui
	 */
	public void saveTask(){
		TaskData taskdata = new TaskData(model.titleProperty().get(), model.descProperty().get(), model.dateProperty().get(), model.stateProperty().get());
		Task tmp = new Task(model.idProperty().get(), taskdata);
		System.out.println("detail: " + taskdata.toString());
		model.updateTask(tmp);
		cleanUp();
	}

	/**
	 * Leert das Form mit eingegebenen Informationen
	 */
	public void cleanUp(){
		model.titleProperty().set("");
		model.descProperty().set("");
		model.dateProperty().set(null);
		model.stateProperty().set(null);

		buttonsDisable();
	}

	/**
	 * Hook fuer nach dem Klick auf "New".
	 * Hier kann init, cleanup, button disable eingefuegt werden.
	 * */
	public void setUp() {
		// setup Form
		model.titleProperty().set("");
		model.descProperty().set("");
		model.dateProperty().set(LocalDate.now()); // Localdate
		model.stateProperty().set(Status.Todo); // Status

		// activate buttons
		buttonsEnable();
	}

	/**
	 * Loescht den Task im aktuellen Form und leert das Form
	 */
	public void deleteTask(){
		if (model.idProperty().get() != 0 ){
			model.deleteTask(model.idProperty().get());
		}
		cleanUp();
	}

	public void buttonsEnable(){
		buttonSave.setDisable(false);
		buttonDelete.setDisable(false);
	}

	public void buttonsDisable(){
		buttonSave.setDisable(true);
		buttonDelete.setDisable(true);
	}

	public void updateFrom(LongProperty id){
		Task temp = model.getRepo().read(id.get());
		model.titleProperty().set(temp.data.title);
		model.descProperty().set(temp.data.desc);
		model.dateProperty().set(temp.data.dueDate);
		model.stateProperty().set(temp.data.state);
	}
}
