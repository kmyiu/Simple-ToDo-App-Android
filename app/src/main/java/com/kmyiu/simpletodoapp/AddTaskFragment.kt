package com.kmyiu.simpletodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kmyiu.simpletodoapp.database.TodoTask
import com.kmyiu.simpletodoapp.databinding.FragmentAddTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this@AddTaskFragment)
        initUi()
    }

    private fun initUi() {
        binding.btnCancel.setOnClickListener {
            navController.popBackStack()
        }
        binding.btnSave.setOnClickListener {
            val newTaskTitle = binding.etNewTask.text.toString()
            if (newTaskTitle.isNotEmpty()) {
                val todoTaskDao = MyApplication.database.todoTaskDao()
                CoroutineScope(MyApplication.applicationScope.coroutineContext).launch {
                    todoTaskDao.insert(TodoTask(newTaskTitle))
                }
                Toast.makeText(this.context, "Saved", Toast.LENGTH_LONG).show()
                navController.popBackStack()
            }
        }
    }
}