package com.kmyiu.simpletodoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kmyiu.simpletodoapp.database.TodoTask

class CustomAdapter(
    private val dataSet: Array<TodoTask>,
    private val onClick: (TodoTask) -> Unit
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View, onClick: (TodoTask) -> Unit) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        var todoTask: TodoTask? = null

        init {
            view.setOnClickListener { todoTask?.let { it1 -> onClick(it1) } }
            textView = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.todoTask = dataSet[position]
        viewHolder.textView.text = dataSet[position].title
    }

    override fun getItemCount() = dataSet.size
}
