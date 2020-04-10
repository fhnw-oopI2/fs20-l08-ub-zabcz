package ch.fhnw.oop2.tasky.model;

import javafx.beans.property.LongProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TaskyPresentationModelTest {

    private TaskyPresentationModel pm;

    @BeforeEach
    void setUp() {
        pm = new TaskyPresentationModel();
    }

    @AfterEach void tearDown() {
    }

    @DisplayName("TestInitDummyTasks: test if initDummyTasks-Function creates at number of tasks in range of MIN_DUMMY_TASKS and MAX_DUMMY_TASKS ")
    @Test
    public void TestInitDummyTasks() {
        // clear all tasks in repo
        pm.getRepo().deleteAll();

        // init dummy tasks
        pm.initDummyTasks();

        // check if repo contains at least one task
        assertTrue(pm.getRepo().read().size() >= 1);

        // check for each state in ENUM Status
        for (Status state : Status.getAllStati()) {
            // check if at least one task with given state was generated
            assertTrue(pm.getRepo().read().stream().anyMatch(task -> task.data.state.equals(state)));
            int number_of_tasks = (int) pm.getRepo().read().stream().filter(task -> task.data.state.equals(state)).count();
            // check if number of task for each state is in determined range of MIN and MAX
            assertTrue(number_of_tasks >= TaskyPresentationModel.MIN_DUMMY_TASKS);
            assertTrue(number_of_tasks <= TaskyPresentationModel.MAX_DUMMY_TASKS);
        }

    }

    @DisplayName("testCreateTask: check if new task with ID and empty TaskData is generated")
    @Test
    public void testCreateTask() {
        // store task size
        int init_task_size = pm.getRepo().read().size();
        // creates new Task with new ID and empty TaskData
        pm.createTask();
        // new task should be added to repo
        assertEquals(init_task_size + 1 , pm.getRepo().read().size());
        // new task
        Task test_task = pm.getRepo().read().get(init_task_size);
        // new id should not be zero
        assertTrue(test_task.id > 0);
        // title should be empty
        assertSame("", test_task.data.getTitle());
        // description should be empty
        assertSame("", test_task.data.getDesc());
        // new id should not be zero
        assertEquals(LocalDate.now(), test_task.data.getDueDate());
        // init state should be Status.Todo
        assertEquals(Status.Todo, test_task.data.getState());
    }

    @DisplayName("testSaveTask: actual task will be saved in repository if user clicks save in ui")
    @Test
    public void testSaveTask() {
        // store task size
        int init_task_size = pm.getRepo().read().size();
        String title = "TestTitle";
        String desc = "TestDescription";
        LocalDate date = LocalDate.now().plusWeeks(2);
        Status state = Status.Done;
        // creates new Task with new ID and empty TaskData, as user would do
        pm.createTask();
        // change values as the user would do
        pm.titleProperty().set(title);
        pm.descProperty().set(desc);
        pm.dateProperty().set(date);
        pm.stateProperty().set(state);

        // save task, as user would do by clicking on "save" button
        pm.saveTask();

        // store created task in var
        Task test_task = pm.getRepo().read().get(init_task_size);
        // new id should not be zero
        assertTrue(test_task.id > 0);
        // title should be empty
        assertSame(title, test_task.data.getTitle());
        // description should be empty
        assertSame(desc, test_task.data.getDesc());
        // should be same
        assertSame(date, test_task.data.getDueDate());
        // should be same
        assertEquals(state, test_task.data.getState());

    }

    @DisplayName("testTestDeleteTask: if task is deleted, task if deleted from repo ")
    @Test
    public void testTestDeleteTask() {
        // store task size
        int init_task_size = pm.getRepo().read().size();
        // creates new Task with new ID and empty TaskData
        pm.createTask();
        // new task should be added to repo
        assertEquals(init_task_size + 1 , pm.getRepo().read().size());
        // new task
        Task test_task = pm.getRepo().read().get(init_task_size);
        // delete task
        pm.getRepo().delete(test_task.id);
        // repo size should have size before adding new task
        assertEquals(init_task_size, pm.getRepo().read().size());
    }

    @DisplayName("testCleanForm: tests if form is cleaned correctly")
    @Test
    public void testCleanForm() {
        pm.cleanForm();

        String title = "";
        String desc = "";
        LocalDate date = null;
        Status state = null;
        // change values as the user would do
        assertSame(title,pm.titleProperty().get());
        assertSame(desc,pm.descProperty().get());
        assertSame(date,pm.dateProperty().get());
        assertEquals(state,pm.stateProperty().get());

    }

    @DisplayName("testSetupForm: tests if setup is correct")
    @Test
    public void testSetupForm() {
        pm.setupForm();

        String title = "";
        String desc = "";
        LocalDate date = LocalDate.now();
        Status state = Status.Todo;
        // change values as the user would do
        assertSame(title,pm.titleProperty().get());
        assertSame(desc,pm.descProperty().get());
        assertTrue(date.equals(pm.dateProperty().get()));
        assertEquals(state,pm.stateProperty().get());
    }

    @DisplayName("testMouseClickAction: ")
    @Test
    public void testMouseClickAction() {
        // store task sizei
        String title1 = "task01";
        String title2 = "task02";
        String desc1 = "TestDescription";
        String desc2 = "TestDescription";
        LocalDate date1 = LocalDate.now().plusWeeks(2);
        LocalDate date2 = LocalDate.now().plusWeeks(3);
        Status state1 = Status.Done;
        Status state2 = Status.Review;
        // store task size
        int init_task_size = pm.getRepo().read().size();
        // creates two new test tasks
        pm.createTask();
        Task test_task1 = pm.getRepo().read().get(init_task_size);

        // change values of task 2
        pm.titleProperty().set(title1);
        pm.descProperty().set(desc1);
        pm.dateProperty().set(date1);
        pm.stateProperty().set(state1);

        // save values of task 2
        pm.saveTask();

        pm.createTask();
        Task test_task2 = pm.getRepo().read().get(init_task_size+1);

        // change values of task 2
        pm.titleProperty().set(title2);
        pm.descProperty().set(desc2);
        pm.dateProperty().set(date2);
        pm.stateProperty().set(state2);

        // save values of task 2
        pm.saveTask();

        // update form to task1
        pm.mouseClickAction(test_task1.id);

        // check if values of gui have been updated
        assertSame(title1,pm.titleProperty().get());
        assertSame(desc1,pm.descProperty().get());
        assertSame(date1,pm.dateProperty().get());
        assertEquals(state1,pm.stateProperty().get());

        // update form to task2
        pm.mouseClickAction(test_task2.id);

        // check if values of gui have been updated
        assertSame(title2,pm.titleProperty().get());
        assertSame(desc2,pm.descProperty().get());
        assertSame(date2,pm.dateProperty().get());
        assertEquals(state2,pm.stateProperty().get());

    }

}
