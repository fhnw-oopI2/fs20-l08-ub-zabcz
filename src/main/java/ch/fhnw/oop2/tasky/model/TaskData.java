package ch.fhnw.oop2.tasky.model;

import java.time.LocalDate;

/**
 * Die Daten einer Task. Diese Klasse hat keine Identität und wird immer zusammen
 * mit der Klasse Task gebraucht. Objekte dieser Klasse sind immutable.
 *
 */
public class TaskData {

	public final LocalDate dueDate;
	public final Status state;
	public final String title;
	public final String desc;
	
	/**
	 * Erzeugt ein neues TaskData.
	 * 
	 * @param title  Der Titel
	 * @param desc  Die Beschreibung
	 * @param dueDate  Das Datum an dem die Task erledigt sein soll
	 * @param state  Der Zustand der Task
	 */
	public TaskData(String title, String desc, LocalDate dueDate, Status state) {
		this.dueDate = dueDate;
		this.state = state;
		this.title = title;
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "title= " + title + ", desc= " + desc + ", dueDate= " + dueDate +", state= " + state;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        TaskData other = (TaskData) obj;
        if (desc == null) {
            if (other.desc != null)
                return false;
        } else if (!desc.equals(other.desc))
            return false;
        if (dueDate == null) {
            if (other.dueDate != null)
                return false;
        } else if (!dueDate.equals(other.dueDate))
            return false;
        if (state != other.state)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Status getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
