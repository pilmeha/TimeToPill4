package com.example.todolist4

interface TaskItemClickListener
{
    fun editTaskItem(taskItem: TaskItem)
    fun completeTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
}