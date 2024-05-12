package com.example.todolist4

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todolist4.databinding.FragmentNewTaskSheetBinding
import com.example.todolist4.databinding.TaskItemCellBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.sql.Date
import java.time.LocalTime
import java.util.Calendar

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment()
{
    private  lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (taskItem != null) {
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.desc)

            if (taskItem!!.dueTime() != null){
                dueTime = taskItem!!.dueTime()!!

            }
        } else {
            binding.taskTitle.text = "New Task"
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        binding.saveButton.setOnClickListener{
            saveAction()
        }

//        binding.timePickerButton.setOnClickListener{
//            openTimePicker()
//        }
    }

    private fun saveAction()
    {
        val intent = Intent(context?.applicationContext, Notification::class.java)
        val title = binding.name.text.toString()
        val message = binding.desc.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context?.applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        showAlert(time, title, message)

        val dueTimeString = if(dueTime == null) null
        else TaskItem.timeFormatter.format(dueTime)

        if (taskItem == null) {
            val newTask = TaskItem(title, message, dueTimeString, null)
            taskViewModel.addTaskItem(newTask)
        }

        else {
            taskItem!!.name = title
            taskItem!!.desc = message
            taskItem!!.dueTimeString = dueTimeString
            taskViewModel.updateTaskItem(taskItem!!)
        }
        binding.name.setText("")
        binding.desc.setText("")
        dismiss()
    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context?.applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context?.applicationContext)

        AlertDialog.Builder(context)
            .setTitle("Запись уведомления")
            .setMessage("Название: " + title +
                    "\nКомментарий: " + message +
                    "\nВремя: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Окей"){_,_ ->}
            .show()
    }

    private fun getTime(): Long
    {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

//    private fun openTimePicker() {
//        if (dueTime == null)
//            dueTime = LocalTime.now()
//        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
//            dueTime = LocalTime.of(selectedHour, selectedMinute)
//            updateTimeButtonText()
//        }
//        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
//        dialog.setTitle("Task Due")
//        dialog.show()
//    }
//
//    private fun updateTimeButtonText() {
//        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

//    private fun saveAction()
//    {
//        val name = binding.name.text.toString()
//        val desc = binding.desc.text.toString()
//        val dueTimeString = if(dueTime == null) null
//            else TaskItem.timeFormatter.format(dueTime)
//        if (taskItem == null) {
//            val newTask = TaskItem(name, desc, dueTimeString, null)
//            taskViewModel.addTaskItem(newTask)
//        }
//
//        else {
//            taskItem!!.name = name
//            taskItem!!.desc = desc
//            taskItem!!.dueTimeString = dueTimeString
//            taskViewModel.updateTaskItem(taskItem!!)
//        }
//        binding.name.setText("")
//        binding.desc.setText("")
//        dismiss()
//    }
}