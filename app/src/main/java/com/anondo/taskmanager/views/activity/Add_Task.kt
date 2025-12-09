package com.anondo.taskmanager.views.activity

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anondo.taskmanager.databinding.ActivityAddTaskBinding
import com.anondo.taskmanager.db.TaskDao
import com.anondo.taskmanager.db.Task_Data_Class
import com.anondo.taskmanager.reducecode.EncryptDecrypt
import com.anondo.taskmanager.views.activity.MainActivity

@Suppress("DEPRECATION")
class Add_Task : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding
    lateinit var dao: TaskDao
    var userId = 0

    val colors = listOf(
        Color.parseColor("#F48FB1"), // Muted Pink
        Color.parseColor("#CE93D8"), // Muted Purple
        Color.parseColor("#BA68C8"), // Rich Purple
        Color.parseColor("#F06292"), // Rose
        Color.parseColor("#7986CB") // Dark Indigo
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = EncryptDecrypt.database(this)

        editTaskOption()

        binding.tvDueDateValue.setOnClickListener {

            binding.tvDueDateValue.visibility = View.GONE
            binding.datePicker.visibility = View.VISIBLE

        }

        binding.btnSaveTask.setOnClickListener {

            var title_normal = binding.etTitle.text.toString()
            var description_normal = binding.etDescription.text.toString()
            val completed = binding.switchCompleted.isChecked

            val calendar = Calendar.getInstance()
            calendar.set(binding.datePicker.year, binding.datePicker.month, binding.datePicker.dayOfMonth)
            val dueMillis = calendar.timeInMillis.toString()

            if (title_normal.isEmpty()) {
                binding.etTitle.error = "Enter task title"
                return@setOnClickListener
            }else if (description_normal.isEmpty()){
                binding.etDescription.error = "Enter task description"
                return@setOnClickListener
            }

            var title_encrypted = EncryptDecrypt.encryptData(title_normal)
            var description_encrypted = EncryptDecrypt.encryptData(description_normal)

            addoredit(userId , title_encrypted , description_encrypted , dueMillis , completed)

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

    fun editTaskOption(){

        if (intent.hasExtra("Edit")){
            var task = intent.getParcelableExtra<Task_Data_Class>("Edit")

            task?.let{

                binding.btnSaveTask.text = "Update Task"
                binding.etTitle.setText(EncryptDecrypt.decryptData(it.title))
                binding.etDescription.setText(EncryptDecrypt.decryptData(it.description))
                binding.switchCompleted.isChecked = it.status
                userId = it.id

                val millTime = it.duedate.toLong()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = millTime

                binding.datePicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

            }

        }else{
            binding.btnSaveTask.text = "Save Task"
        }


    }

    fun addoredit(id: Int, title: String, description: String, dueTimes: String, status: Boolean) {

        if (binding.btnSaveTask.text=="Update Task"){
            val randomColor = colors.random()

            var taskData =
                Task_Data_Class(id, title, description, dueTimes, status, randomColor.toInt())

            dao.Edit_Task(taskData)

        }else{
            val randomColor = colors.random()

            var taskData =
                Task_Data_Class(0, title, description, dueTimes, status, randomColor.toInt())

            dao.Add_Task(taskData)
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}