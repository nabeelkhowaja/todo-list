package com.nabeelkhowaja.todolist.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.databinding.ActivityTodoBinding
import com.nabeelkhowaja.todolist.view.DialogCloseListener
import com.nabeelkhowaja.todolist.view.fragment.NewTaskFragment

class TodoActivity : AppCompatActivity(), DialogCloseListener {

    private var mBinding: ActivityTodoBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);

        setListeners()
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

    override fun handleDialogClose(dialog: DialogInterface?) {

    }
}