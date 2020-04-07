package ch.fhnw.oop2.tasky.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskyPresentationModel {

    private final StringProperty stageTitle;

    public TaskyPresentationModel() {
        stageTitle = new SimpleStringProperty("JavaFX Application Tasky");
    }

    public String getStageTitle() {
        return stageTitle.get();
    }

    public StringProperty stageTitleProperty() {
        return stageTitle;
    }

    public void setStageTitle(String stageTitle) {
        this.stageTitle.set(stageTitle);
    }


}
