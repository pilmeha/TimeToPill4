package com.example.todolist4

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist4.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root)
{
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    fun bindTaskItem(taskItem: TaskItem){
        binding.name.text = taskItem.name
        binding.dueTime.text = taskItem.dueTimeString.toString()

        if (taskItem.isCompleted){
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        else{
            binding.name.paintFlags.inv()
            binding.dueTime.paintFlags.inv()
        }

        binding.completeButton.setImageResource(taskItem.imageResource())
        binding.completeButton.setColorFilter(taskItem.imageColor(context))

        binding.completeButton.setOnClickListener{
            clickListener.completeTaskItem(taskItem)
        }

        binding.taskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }

        binding.deleteBottom.setOnClickListener{
            clickListener.deleteTaskItem(taskItem)
        }

        if (taskItem.dueTime() != null) {
            binding.dueTime.text = timeFormat.format(taskItem.dueTime())
        }
        else {
            binding.dueTime.text = "time"
        }
    }
}