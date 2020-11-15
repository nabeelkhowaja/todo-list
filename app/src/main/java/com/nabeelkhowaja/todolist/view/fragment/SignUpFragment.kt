package com.nabeelkhowaja.todolist.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.databinding.FragmentSignupBinding
import com.nabeelkhowaja.todolist.model.EntryResponse
import com.nabeelkhowaja.todolist.view.HandleEntryPages
import com.nabeelkhowaja.todolist.viewmodel.SignUpViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.ClassCastException

class SignUpFragment : Fragment() {

    private val TAG = "SignUpFragment"

    private var mBinding: FragmentSignupBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability
    private var handleEntryPages: HandleEntryPages? = null
    private lateinit var viewModel: SignUpViewModel

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        //setObservers()
    }

    /*private fun setObservers() {
        viewModel.signUpResponse.observe(viewLifecycleOwner, Observer {
            if (isValidForSignUp(it)) {
                uiScope.launch {
                    val user = viewModel.getUser(it.username)
                    if(user!=null)
                        Toast.makeText(requireContext(), "User already exist", Toast.LENGTH_SHORT).show()
                    else{
                        viewModel.insertUser(it.username, it.password)
                        resetViews()
                        Toast.makeText(requireContext(), "User created!", Toast.LENGTH_SHORT).show()
                        handleEntryPages?.showLoginFragment()
                    }
                }
            }
        })
    }*/

    //Validating inputs
    private fun isValidInputs(response: EntryResponse): Boolean {
        var result = response.isUsernameValid()
        binding.tiUsername.error = result.message

        if (result.isValid) {
            result = response.isPasswordLengthValid()
            binding.tiPassword.error = result.message
        } else binding.tiPassword.error = null

        if (result.isValid) {
            result = response.validatePasswords()
            binding.tiConfirmPassword.error = result.message
        } else binding.tiConfirmPassword.error = null

        return result.isValid
    }

    private fun setListeners() {
        binding.tvSignIn.setOnClickListener {
            handleEntryPages?.showLoginFragment()
        }
        binding.btnSignUp.setOnClickListener {
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            //viewModel.doSignUp(username, password, confirmPassword)
            signUp(username, password, confirmPassword)
        }
    }

    //Launching new coroutine
    private fun signUp(username: String, password: String, confirmPassword: String) =
        uiScope.launch {
            viewModel.doSignUp(username, password, confirmPassword).collect { response ->
                if (isValidInputs(response)) {
                    val user = viewModel.getUser(response.username)
                    //Checking if username already exists in db
                    if (user != null)
                        Toast.makeText(
                            requireContext(),
                            R.string.username_already_exist,
                            Toast.LENGTH_SHORT
                        ).show()
                    else {
                        //inserting new user in db.
                        viewModel.insertUser(response.username, response.password).let {
                            Toast.makeText(
                                requireContext(),
                                R.string.user_created,
                                Toast.LENGTH_SHORT
                            ).show()
                            handleEntryPages?.resetEntryPages()
                        }
                        //handleEntryPages?.showLoginFragment()
                    }
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            handleEntryPages = context as HandleEntryPages
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. **/
            Log.d(TAG, castException.message.toString())
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

    override fun onDestroy() {
        super.onDestroy()
        //Cancelling the job so that all coroutines tight to this gets cancelled
        viewModelJob.cancel()
        uiScope.cancel()
    }
}