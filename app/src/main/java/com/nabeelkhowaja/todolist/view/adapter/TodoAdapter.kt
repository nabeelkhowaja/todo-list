package com.nabeelkhowaja.todolist.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.model.Todo
import com.nabeelkhowaja.todolist.view.TodoListener
import com.nabeelkhowaja.todolist.view.activity.EntryActivity

class TodoAdapter(val context: Context) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

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
        holder.delete.setOnClickListener {
            MaterialAlertDialogBuilder(
                context,
                R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle("Alert")
                .setMessage("Do you want to delete this task?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    deleteItem(position)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
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
        var delete: ImageButton

        init {
            task = view.findViewById(R.id.todoCheckBox)
            delete = view.findViewById(R.id.delete)
        }
    }

    /*init {
        this.db = db
        this.activity = activity
    }*/
}
