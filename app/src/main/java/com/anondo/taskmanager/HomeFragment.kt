package com.anondo.taskmanager

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anondo.taskmanager.databinding.FragmentHomeBinding
import com.anondo.taskmanager.db.TaskDao
import com.anondo.taskmanager.db.Task_Data_Class
import com.anondo.taskmanager.reducecode.EncryptDecrypt
import com.anondo.taskmanager.views.Add_Task
import com.anondo.taskmanager.views.TaskAdapter

class HomeFragment : Fragment(), TaskAdapter.handleUserClick {

    lateinit var binding: FragmentHomeBinding
    lateinit var dao: TaskDao
    lateinit var taskList: MutableList<Task_Data_Class>
    private lateinit var taskAdapter: TaskAdapter

    private var titleMenuText = "Sort by Title(A-Z)"
    private var dueDateMenuText = "Due Date(OverDue)"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        dao = EncryptDecrypt.database(requireContext())

        recyclerAdapter(dao.Get_All_Task())

        binding.addTask.setOnClickListener {
            startActivity(Intent(requireContext(), Add_Task::class.java))
        }

        binding.filter.setOnClickListener { view ->
            showMenu(view, R.menu.overflow_menu)
        }

        edSearchEvent()

        return binding.root
    }

    private fun updateMenu() {
        titleMenuText = "Sort by Title(A-Z)"
        dueDateMenuText = "Due Date(OverDue)"
    }

    private fun edSearchEvent() {

        binding.searchByTitle.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {

                val title = binding.searchByTitle.text.toString()

                if (title.isNotEmpty()) {

                    val filteredTasks = dao
                        .Get_All_Task()
                        .filter { task ->
                            EncryptDecrypt.decryptData(task.title)
                                .contains(title, ignoreCase = true)
                        }

                    recyclerAdapter(filteredTasks.toMutableList())

                } else {

                    recyclerAdapter(dao.Get_All_Task())
                }
            }
        })
    }

    private fun recyclerAdapter(tasks: MutableList<Task_Data_Class>) {

        taskList = tasks
        taskList.sortBy { it.duedate.toLong() }

        taskAdapter = TaskAdapter(
            this,
            requireContext(),
            taskList
        )

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = taskAdapter
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int) {

        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.menu.findItem(R.id.titles).title = titleMenuText
        popup.menu.findItem(R.id.dueDates).title = dueDateMenuText

        popup.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {

                R.id.titles -> {

                    if (titleMenuText.contains("A-Z")) {

                        taskList.sortBy {
                            EncryptDecrypt.decryptData(it.title).lowercase()
                        }

                        titleMenuText = "Sort by Title(Z-A)"

                    } else {

                        taskList.sortByDescending {
                            EncryptDecrypt.decryptData(it.title).lowercase()
                        }

                        titleMenuText = "Sort by Title(A-Z)"
                    }

                    taskAdapter.notifyDataSetChanged()
                    true
                }

                R.id.dueDates -> {

                    if (dueDateMenuText.contains("OverDue")) {

                        taskList.sortByDescending {
                            it.duedate.toLong()
                        }

                        dueDateMenuText = "Due Date(Closest)"

                    } else {

                        taskList.sortBy {
                            it.duedate.toLong()
                        }

                        dueDateMenuText = "Due Date(OverDue)"
                    }

                    taskAdapter.notifyDataSetChanged()
                    true
                }

                else -> false
            }
        }

        popup.show()
    }

    override fun onEditClick(task: Task_Data_Class) {

        val intent =
            Intent(requireContext(), Add_Task::class.java)

        intent.putExtra("Edit", task)
        startActivity(intent)
    }

    override fun onDeleteClick(task: Task_Data_Class) {

        dao.Delete_Task(task)

        Toast.makeText(
            requireContext(),
            "Deleted Successful",
            Toast.LENGTH_LONG
        ).show()

        recyclerAdapter(dao.Get_All_Task())
    }

    override fun onStatusChange(task: Task_Data_Class, isChecked: Boolean) {

        val updatedTask =
            task.copy(status = isChecked)

        dao.Edit_Task(updatedTask)

        recyclerAdapter(dao.Get_All_Task())
    }

    override fun onResume() {
        super.onResume()
        recyclerAdapter(dao.Get_All_Task())
        updateMenu()
    }
}