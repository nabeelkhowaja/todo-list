package com.nabeelkhowaja.todolist.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nabeelkhowaja.todolist.databinding.FragmentLoginBinding
import com.nabeelkhowaja.todolist.view.HandleEntryPages
import java.lang.ClassCastException

class LoginFragment() : Fragment() {

    private var mBinding: FragmentLoginBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability
    private var handleEntryPages: HandleEntryPages? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.tvSignUp.setOnClickListener {
            handleEntryPages?.showSignUpFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            handleEntryPages = context as HandleEntryPages
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. **/
        }
    }

    override fun onDetach() {
        super.onDetach()
        handleEntryPages = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}