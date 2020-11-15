package com.nabeelkhowaja.todolist.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nabeelkhowaja.todolist.databinding.ActivityEntryBinding
import com.nabeelkhowaja.todolist.view.HandleEntryPages
import com.nabeelkhowaja.todolist.view.adapter.EntryAdapter

class EntryActivity : AppCompatActivity(), HandleEntryPages {

    private var mBinding: ActivityEntryBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting up login and sign-up fragments in view pager's adapter
        setEntryAdapter()
    }

    private fun setEntryAdapter() {
        val entryAdapter = EntryAdapter(this)
        binding.viewPager.adapter = entryAdapter
        binding.viewPager.isUserInputEnabled = false;
    }

    //call from sign-up fragment to show login fragment
    override fun showLoginFragment() = binding.viewPager.setCurrentItem(0, true)

    //call from login fragment to show sign-up fragment
    override fun showSignUpFragment() = binding.viewPager.setCurrentItem(1, true)

    //reset login and and sign-up fragment after successfully creating user
    override fun resetEntryPages() = setEntryAdapter()

}