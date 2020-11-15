package com.nabeelkhowaja.todolist.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.databinding.ActivityTodoBinding
import com.nabeelkhowaja.todolist.view.TodoListener
import com.nabeelkhowaja.todolist.view.adapter.TodoAdapter
import com.nabeelkhowaja.todolist.view.fragment.NewTaskFragment
import com.nabeelkhowaja.todolist.viewmodel.TodoViewModel
import kotlinx.coroutines.*


class TodoActivity : AppCompatActivity(), TodoListener {

    private var mBinding: ActivityTodoBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability
    lateinit var adapter: TodoAdapter
    private lateinit var viewModel: TodoViewModel

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTodoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        setContentView(binding.root)


        setToolbar()
        setListeners()
        setTodoList()
        setObservers()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setListeners() {
        binding.fabCreateTask.setOnClickListener {
            NewTaskFragment.newInstance().show(supportFragmentManager, NewTaskFragment.TAG)
        }
    }

    private fun setTodoList() {
        //setting up adapter for tasks list
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(this)
        binding.tasksRecyclerView.adapter = adapter
    }

    private fun setObservers() {
        viewModel.getAllTodo().observe(this, Observer {
            adapter.setTasks(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logout()
        }
        return false
    }

    private fun logout() {
        MaterialAlertDialogBuilder(
            this,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle(R.string.alert)
            .setMessage(R.string.logout_message)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                val intent = Intent(this@TodoActivity, EntryActivity::class.java)
                //Removing all tasks from back stack
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                this@TodoActivity.finish()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Cancelling the job so that all coroutines tight to this gets cancelled
        viewModelJob.cancel()
        uiScope.cancel()
    }

    override fun deleteTodo(id: Int) {
        uiScope.launch {
            //Deleting task with id
            viewModel.deleteTodo(id)
        }
    }

    override fun toggleCompletedStatus(id: Int, isCompleted: Boolean) {
        uiScope.launch {
            //toggle task status
            viewModel.toggleCompletedStatus(id, isCompleted)
        }
    }
}