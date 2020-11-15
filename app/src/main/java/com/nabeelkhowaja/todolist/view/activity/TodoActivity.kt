package com.nabeelkhowaja.todolist.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setListeners()

        binding.tasksRecyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter = TodoAdapter(this)
        binding.tasksRecyclerView.setAdapter(adapter)

        viewModel.getAllTodo().observe(this, Observer {
            adapter.setTasks(it)
        })


    }

    private fun setListeners() {
        binding.fabCreateTask.setOnClickListener {
            NewTaskFragment.newInstance().show(supportFragmentManager, NewTaskFragment.TAG)
        }
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
            .setTitle("Alert")
            .setMessage("Do you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                val intent = Intent(this@TodoActivity, EntryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                this@TodoActivity.finish()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
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
            viewModel.deleteTask(id)
        }
    }

    override fun toggleCompletedStatus(id: Int, isCompleted: Boolean) {
        uiScope.launch {
            viewModel.toggleCompletedStatus(id, isCompleted)
        }
    }
}