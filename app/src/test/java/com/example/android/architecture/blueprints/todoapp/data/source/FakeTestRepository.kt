package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking
import java.util.LinkedHashMap

class FakeTestRepository : TasksRepository {
    //representing the current list
    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()
    //for your observable tasks
    private val observableTasks = MutableLiveData<Result<List<Task>>>()
    
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return Result.Success(tasksServiceData.values.toList())
    }
    
    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }
    
    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }
    
    override suspend fun refreshTask(taskId: String) {
        observableTasks.value = getTasks()
    }
    
    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }
        return observableTasks.map { tasks ->
            when (tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }
    
    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }
    
    override suspend fun saveTask(task: Task) {
        tasksServiceData[task.id] = task
    }
    
    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(isCompleted = true)
        tasksServiceData[task.id] = completedTask
        refreshTasks()
    }
    
    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
        // Not required for the remote data source.
    }
    
    override suspend fun activateTask(task: Task) {
        val activeTask = task.copy(isCompleted = false)
        tasksServiceData[task.id] = activeTask
        refreshTasks()
    }
    
    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }
    
    override suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }
    
    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
        refreshTasks()
    }
    
    override suspend fun deleteTask(taskId: String) {
        tasksServiceData.remove(taskId)
        refreshTasks()
    }
    
    fun addTask(vararg  tasks: Task){
        for (task in tasks){
            tasksServiceData[task.id] = task
        }
        
        runBlocking { refreshTasks() }
    }
    
}