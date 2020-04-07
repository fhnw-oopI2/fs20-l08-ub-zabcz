package ch.fhnw.oop2.tasky.part1.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse implementiert eine Task. Eine Task besteht aus der ID und den Daten.
 * 
 * Diese Klasse ist nicht veränderbar (immutable). Alle Felder sind öffentlich und
 * final implementiert.
 * 
 */
public class Task {

    private static final String HOVER_COLOR = "#c4d5c4";
    public final long id;
	public final TaskData data;
	
	/**
	 * Erzeugt eine neue Task.
	 * 
	 * @param id  Die Identität (ID) der Task
	 * @param data  Die Daten der Task
	 */
	public Task(long id, TaskData data) {
	    if(data == null) {
	        throw new IllegalArgumentException("data must not be null!");
	    }
		this.id = id;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Task ["+id+"] " + data.toString();
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + data.hashCode();
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        if (!data.equals(other.data))
            return false;
        if (id != other.id)
            return false;
        return true;
    }

    public static List<Task> reduceList(List<Task> tasks, Status state){
        List<Task> reduced_tasks = new ArrayList<>();

	    for (Task t : tasks) {
            if(t.data.state == state) {
                reduced_tasks.add(t);
            }
        }
	    return reduced_tasks;
    }

    /**
     * Erzeugt eine neue Region (hier ein Label).
     *
     * @param color Color als Hex-String
     * @param label Text, welcher auf die Region geschrieben wird
     * @return  Die neue Region
     */
    public static Region createRegionWithText(String color, String label) {
        final Label labelTask = new Label();
        labelTask.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        labelTask.setPadding(new Insets(10));
        labelTask.setStyle("-fx-background-color: " +color + ";");
        //labelTask.setOpacity(0.9);
        labelTask.setText(label);

        // Effekt: wenn mit Maus drüber fahrt, Button Effekt schaffen
        labelTask.onMouseEnteredProperty().set(event -> labelTask.setStyle("-fx-background-color: " +HOVER_COLOR + ";"));
        labelTask.onMouseExitedProperty().set(event -> labelTask.setStyle("-fx-background-color: " +color + ";"));

        return labelTask;
    }
	
	
}
