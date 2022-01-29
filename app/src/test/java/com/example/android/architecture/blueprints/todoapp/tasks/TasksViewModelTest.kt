package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.IsNull.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {
    //When you have repeated setup code like this shared between all tests,
    // you can use the @Before annotation to create a setup method and remove repeated code.
    // Since all of these tests are going to test the TasksViewModel and will need a view model,
    // it's safe to move this code to a @Before block.
    
    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @Before
    fun setUpViewModel() {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
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
}