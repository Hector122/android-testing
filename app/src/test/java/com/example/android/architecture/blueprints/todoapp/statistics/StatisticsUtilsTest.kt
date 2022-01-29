package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {
    
    // If there is one completed task and no active tasks, the activeTasks percentage should be 0f, the completed tasks percentage should be 100f.
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        // GIVEN a list of task with a single,  active, task
        val task = listOf(Task("Title", "Dummy description", isCompleted = false))
        
        // WHEN you call getActiveAndCompletedStats
        val result = getActiveAndCompletedStats(task)
        
        // THEN there are 0% completed task and 100% active tsk
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(100f, result.activeTasksPercent)
    }
    
    // If there are two completed tests and three active test, the completed percentage should be 40f and the active percentage should be 60f.
    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        // Create an active tasks (the false makes this active)
        val task = listOf(Task("Title", "Dummy description", isCompleted = true),
                Task("Title", "Dummy description", isCompleted = true),
                Task("Title", "Dummy description", isCompleted = false),
                Task("Title", "Dummy description", isCompleted = false),
                Task("Title", "Dummy description", isCompleted = false))
        
        // Call our function
        val result = getActiveAndCompletedStats(task)
        // Check the result
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }
    
    //If there is an empty list (emptyList()), then both percentages should be 0f.
    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros() {
        // Create an active tasks (the false makes this active)
        val task = emptyList<Task>()
        // Call our function
        val result = getActiveAndCompletedStats(task)
        // Check the result use Hamcrest.
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
    
    //If there was an error loading tasks, the list will be null, then both percentages should be 0f.
    @Test
    fun getActiveAndCompletedStats_error_returnsZeros() {
        // Create an active tasks (the false makes this active)
        val task = null
        // Call our function
        val result = getActiveAndCompletedStats(task)
        // Check the result use Hamcrest.
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
}