package com.kmyiu.simpletodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kmyiu.simpletodoapp.MyApplication.Companion.applicationScope
import com.kmyiu.simpletodoapp.database.TodoTaskData
import com.kmyiu.simpletodoapp.databinding.FragmentEditTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EditTaskFragment : Fragment() {
    private lateinit var binding: FragmentEditTaskBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this@EditTaskFragment)
        initUi()
    }

    private fun initUi() {
        val todoTaskData: TodoTaskData =
            requireArguments().getSerializable("TodoTaskData") as TodoTaskData
        binding.etNewTask.setText(todoTaskData.title)
        binding.btnSave.setOnClickListener {
            val newTitle = binding.etNewTask.text.toString()
            if (newTitle.isEmpty()) {
                CoroutineScope(applicationScope.coroutineContext).launch {
                    MyApplication.database.todoTaskDao().deleteByUid(todoTaskData.uid)
                }
            } else {
                CoroutineScope(applicationScope.coroutineContext).launch {
                    MyApplication.database.todoTaskDao().editByUid(todoTaskData.uid, newTitle)
                }
            }
            navController.popBackStack()
        }
        binding.btnDelete.setOnClickListener {
            CoroutineScope(applicationScope.coroutineContext).launch {
                MyApplication.database.todoTaskDao().deleteByUid(todoTaskData.uid)
            }
            navController.popBackStack()
        }
    }
}