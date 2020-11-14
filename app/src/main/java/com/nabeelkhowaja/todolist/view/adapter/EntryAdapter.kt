package com.nabeelkhowaja.todolist.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nabeelkhowaja.todolist.view.fragment.LoginFragment
import com.nabeelkhowaja.todolist.view.fragment.SignUpFragment

class EntryAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    //list of fragments to display in viewpager
    private val fragmentList: List<Fragment> = listOf(
        LoginFragment(),
        SignUpFragment()
    )

    override fun getItemCount(): Int = fragmentList.size;

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}