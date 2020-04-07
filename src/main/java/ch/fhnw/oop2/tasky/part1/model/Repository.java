package ch.fhnw.oop2.tasky.part1.model;

import java.util.List;

/**
 * Das Repository Interface. Alle Implementation eines Repositories müssen dieses Interface
 * implementieren.
 *
 */
public interface Repository {
	
	/**
	 * Erzeugt eine neue Task.
	 * 
	 * @param data Die TaskData, die Daten der Task
	 * @return Eine neue Task
	 */
	Task create(TaskData data);
	
	/**
	 * Liest alle Tasks im Repository und gibt sie zurück. Die Tasks sind nicht sortiert.
	 * 
	 * @return Die Liste mit allen Tasks (unsortiert). Die Liste ist leer, wenn es keine Tasks gibt
	 */
	List<Task> read();
	
	/**
	 * Liest die Task mit der spezifierten ID. Falls die ID nicht exisitert wird null zurück gegeben.
	 * 
	 * @param id  Die ID der Task
	 * @return  Die Task falls sie existiert, null sonst
	 */
	Task read(long id);
	
	/**
	 * Update einer Task. Eine Task kann nur angepasst werden, wenn sie auch
	 * tatsächlich existiert. 
	 * 
	 * Beim Versuch eine Task anzupassen, die nicht existiert, wird eine 
	 * IllegalStateException geworfen.
	 * 
	 * @param updated Die Task, die angepasst werden soll
	 */
	void update(Task updated);
	
	/**
	 * Löscht die Task mit der spezifizierten ID. Der Versuch eine nicht existierende 
	 * Task zu löschen führt zu einer IllegalStateException.
	 * 
	 * @param id  Die ID
	 */
	void delete(long id);
}

