package com.kmyiu.simpletodoapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmyiu.simpletodoapp.MyApplication.Companion.database
import com.kmyiu.simpletodoapp.database.TodoTask
import com.kmyiu.simpletodoapp.database.TodoTaskData
import com.kmyiu.simpletodoapp.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mContext = this.requireContext()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this@MainFragment)
        initUi()
    }

    private fun initUi() {
        val todoTaskDao = database.todoTaskDao()
        CoroutineScope(MyApplication.applicationScope.coroutineContext).launch {
            val todoFlow: Flow<List<TodoTask>> = todoTaskDao.getAll()
            todoFlow.collect { todos ->
                CoroutineScope(Dispatchers.Main).launch {
                    val recyclerView = binding.recyclerview
                    val adapter = CustomAdapter(todos.toTypedArray()) { todoTask ->
                        val todoTaskData = TodoTaskData(todoTask.title, todoTask.uid)
                        val bundle = Bundle()
                        bundle.putSerializable("TodoTaskData", todoTaskData)
                        navController.navigate(R.id.action_mainFragment_to_editTaskFragment, bundle)
                    }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                }
            }
        }
        // quote
        // for marquee effect
        binding.tvQuote.isSelected = true
        val quoteViewModelObserver = Observer<String> { quote ->
            binding.tvQuote.text = quote
        }
        MainActivity.quoteViewModel.quoteSentence.observe(
            viewLifecycleOwner, quoteViewModelObserver
        )
        binding.tvQuote.setOnClickListener { MainActivity.quoteViewModel.refreshQuote() }

        // add task fragment
        binding.btnAddTask.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_addTaskFragment)
        }
    }
}