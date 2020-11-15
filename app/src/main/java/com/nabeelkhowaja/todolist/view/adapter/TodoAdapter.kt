package com.nabeelkhowaja.todolist.view.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
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

        val item: Todo = todoList!![position]
        holder.task.setText(item.task)
        holder.task.isChecked = item.isCompleted
        holder.task.setOnCheckedChangeListener { _, isChecked ->
            todoListener?.toggleCompletedStatus(item.id!!, isChecked)
            if (isChecked)
                holder.task.paintFlags = holder.task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                holder.task.paintFlags =
                    holder.task.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.delete.setOnClickListener {
            MaterialAlertDialogBuilder(
                context,
                R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle(R.string.alert)
                .setMessage(R.string.delete_task_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    deleteItem(position)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        if (item.isCompleted)
            holder.task.paintFlags = holder.task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            holder.task.paintFlags = holder.task.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    override fun getItemCount(): Int {
        return if (todoList != null) todoList!!.size else 0
    }

    fun setTasks(todoList: List<Todo>?) {
        this.todoList = todoList!!.toMutableList()
        notifyDataSetChanged()
    }

    private fun deleteItem(position: Int) {
        val item: Todo = todoList!![position]
        todoListener?.deleteTodo(item.id!!)
        todoList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox = view.findViewById(R.id.cbTodo)
        var delete: ImageButton = view.findViewById(R.id.ibDelete)
    }
}
