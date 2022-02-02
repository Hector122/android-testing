package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.IsNull.nullValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class) Because you are no longer using the AndroidX Test

class TasksViewModelTest {
    //When you have repeated setup code like this shared between all tests,
    // you can use the @Before annotation to create a setup method and remove repeated code.
    // Since all of these tests are going to test the TasksViewModel and will need a view model,
    // it's safe to move this code to a @Before block.
    
    //Use this because andriod main looper is not available
// REPLACE
//    @ExperimentalCoroutinesApi
//    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
//
//    @ExperimentalCoroutinesApi
//    @Before
//    fun setupDispatcher() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @ExperimentalCoroutinesApi
//    @After
//    fun tearDownDispatcher() {
//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
//    }
    
    
    // WITH
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    
    
    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeTestRepository
    
    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @Before
    fun setUpViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        
        tasksRepository.addTask(task1, task2, task3)
        
        tasksViewModel = TasksViewModel(tasksRepository)
    }
    
    @Test
    fun addNewTask_setsNewTaskEvent() {
        // GIVEN a fresh task view model
        //val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        
        //WHEN adding a new task
        tasksViewModel.addNewTask()
        
        // THEN the new task event in trigger
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
        
    }
    
    //check that if you've set your filter type to show all tasks, that the Add task button is visible.
    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // Given a fresh ViewModel
        //val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        
        // Then the "Add task" action is visible
        val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, `is`(true))
    }
    
    //test for code that uses viewModelScope
    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description")
        tasksRepository.addTask(task)
        
        // Mark the task as complete task.
        tasksViewModel.completeTask(task, true)
        
        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))
        
        val snackbarText: Event<Int> = tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
        
    }
}