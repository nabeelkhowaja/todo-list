package com.nabeelkhowaja.todolist.view.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.model.Todo
import com.nabeelkhowaja.todolist.view.TodoListener

class TodoAdapter(context: Context) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var todoList: MutableList<Todo>? = mutableListOf()
    private var todoListener: TodoListener? = null

    init {
        todoListener = context as TodoListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_todo, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        //db.openDatabase()
        val item: Todo = todoList!![position]
        holder.task.setText(item.task)
        holder.task.isChecked = item.isCompleted
        holder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            /*if (isChecked) {
                db.updateStatus(item.getId(), 1)
            } else {
                db.updateStatus(item.getId(), 0)
            }*/
        }
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    override fun getItemCount(): Int {
        return if(todoList!=null) todoList!!.size else 0
    }

    /*val context: Context get() = activity*/

    fun setTasks(todoList: List<Todo>?) {
        this.todoList = todoList!!.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item: Todo = todoList!![position]
        todoListener?.deleteTodo(item.id!!)
        todoList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    /*fun editItem(position: Int) {
        val item: ToDoModel = todoList!![position]
        val bundle = Bundle()
        bundle.putInt("id", item.getId())
        bundle.putString("task", item.getTask())
        val fragment = AddNewTask()
        fragment.setArguments(bundle)
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG)
    }*/

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox

        init {
            task = view.findViewById(R.id.todoCheckBox)
        }
    }

    /*init {
        this.db = db
        this.activity = activity
    }*/
}
