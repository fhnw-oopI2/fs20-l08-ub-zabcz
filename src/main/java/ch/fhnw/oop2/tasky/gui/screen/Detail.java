package ch.fhnw.oop2.tasky.gui.screen;

import ch.fhnw.oop2.tasky.model.Status;
import ch.fhnw.oop2.tasky.model.TaskyPresentationModel;
import javafx.util.converter.NumberStringConverter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


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
		initializeControls();
		layoutControls();
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
		idField.textProperty().bindBidirectional(model.idProperty(), new NumberStringConverter());

		titleField = new TextField();
		titleField.textProperty().bindBidirectional(model.titleProperty()); // String to String - no Converter

		descriptionField = new TextArea();
		descriptionField.textProperty().bindBidirectional(model.descProperty()); // String to String - no Converter

		datePicker = new DatePicker();
		datePicker.valueProperty().bindBidirectional(model.dateProperty()); // value property wie bei slider

		stateDropDown = new ComboBox<>();
		stateDropDown.getItems().addAll(Status.getAllStati());
		stateDropDown.valueProperty().bindBidirectional(model.stateProperty()); // value property wie bei slider

		buttonSave = new Button();
		buttonDelete = new Button();
		buttonSave.textProperty().bind(model.buttonSaveTextProperty());
		buttonDelete.textProperty().bind(model.buttonDeleteTextProperty());
		// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#disableProperty
		buttonSave.disableProperty().bindBidirectional(model.buttonSaveDisableProperty());
		buttonDelete.disableProperty().bindBidirectional(model.buttonDeleteDisableProperty());

		buttonSave.setOnAction(event -> model.saveTask());
		buttonDelete.setOnAction(event -> model.deleteTask());

		//buttonsDisable();
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


}
