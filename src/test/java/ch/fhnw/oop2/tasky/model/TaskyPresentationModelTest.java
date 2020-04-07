package ch.fhnw.oop2.tasky.model;

import ch.fhnw.oop2.tasky.model.impl.InMemoryMapRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TaskyPresentationModelTest {

    private Repository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryMapRepository();
    }

    @AfterEach void tearDown() {
    }

    @Test public void stageTitleProperty() {

    }

    @Test public void getRepo() {
    }

    @Test public void updateTask() {
    }

    @Test public void deleteTask() {
    }

    @Test public void initDummyTasks() {
    }
}
