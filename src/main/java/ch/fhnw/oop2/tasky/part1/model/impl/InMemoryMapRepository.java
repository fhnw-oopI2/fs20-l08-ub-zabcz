package ch.fhnw.oop2.tasky.part1.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.oop2.tasky.part1.model.Repository;
import ch.fhnw.oop2.tasky.part1.model.Task;
import ch.fhnw.oop2.tasky.part1.model.TaskData;

/**
 * In Memory Variante eines Repository. Diese Implementation basiert auf 
 * einer HashMap. Schnelle Einfüge- und Lese-Operationen, aber dafür keine
 * Reihenfolge.
 * 
 */
public class InMemoryMapRepository implements Repository {

	private final Map<Long, Task> allTasks = new HashMap<>();
	private long nextId = 1;
	
	@Override
	public List<Task> read() {
		return new ArrayList<>(allTasks.values());
	}
	
	@Override
	public Task read(long id) {
		return allTasks.get(id);
	}

	@Override
	public Task create(TaskData data) {
		Task newTask = new Task(nextId++, data);
		allTasks.put(newTask.id, newTask);
		return newTask;
	}

	@Override
	public void update(Task updated) {
		if(!allTasks.containsKey(updated.id)) {
			throw new IllegalStateException("Update - keine Task mit dieser ID: " + updated.id);
		}
		allTasks.put(updated.id, updated);
	}

	@Override
	public void delete(long id) {
		if(!allTasks.containsKey(id)) {
			throw new IllegalStateException("Delete - keine Task mit dieser ID: " + id);
		}
		allTasks.remove(id);
	}

}
