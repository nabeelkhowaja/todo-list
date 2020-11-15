package com.nabeelkhowaja.todolist.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.databinding.FragmentLoginBinding
import com.nabeelkhowaja.todolist.model.EntryResponse
import com.nabeelkhowaja.todolist.utils.SecurityUtils
import com.nabeelkhowaja.todolist.view.HandleEntryPages
import com.nabeelkhowaja.todolist.view.activity.TodoActivity
import com.nabeelkhowaja.todolist.viewmodel.LoginViewModel
import com.nabeelkhowaja.todolist.viewmodel.SignUpViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.ClassCastException

class LoginFragment() : Fragment() {

    private val TAG = "LoginFragment"

    private var mBinding: FragmentLoginBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability
    private var handleEntryPages: HandleEntryPages? = null
    private lateinit var viewModel: LoginViewModel

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        //Auto-populating last user if "Remember me" was checked
        viewModel.getLastSavedUser().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.etUserName.setText(it.username)
                binding.etPassword.setText(SecurityUtils.decrypt(it.password, it.username))
                binding.cbRememberMe.isChecked = true
            }
        })
    }

    private fun setListeners() {
        binding.tvSignUp.setOnClickListener {
            handleEntryPages?.showSignUpFragment()
        }
        binding.btnSignIn.setOnClickListener {
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            signIn(username, password)
        }
    }

    //Launching new coroutine
    private fun signIn(username: String, password: String) =
        uiScope.launch {
            viewModel.doSignIn(username, password).collect { response ->
                if (isValidInputs(response)) {
                    //Checking if user exists in db
                    val user = viewModel.getUser(response.username)
                    if (user != null) {
                        //decrypting the encrypted password stored in db
                        val decryptedPassword = SecurityUtils.decrypt(user.password, user.username)
                        if (decryptedPassword == response.password) {
                            if (binding.cbRememberMe.isChecked)
                                viewModel.saveUser(user.username, user.password)
                            else
                                viewModel.removeUser()
                            //Starting TodoActivity
                            startActivity(Intent(requireContext(), TodoActivity::class.java))
                            requireActivity().finish()
                        } else
                            showInvalidLoginMessage()
                    } else showInvalidLoginMessage()
                }
            }
        }

    private fun showInvalidLoginMessage() = Toast.makeText(
        requireContext(),
        R.string.invalid_login_message,
        Toast.LENGTH_SHORT
    ).show()

    //Validating inputs
    private fun isValidInputs(response: EntryResponse): Boolean {

        var result = response.isUsernameValid()
        binding.tiUsername.error = result.message

        if (result.isValid) {
            result = response.isPasswordLengthValid()
            binding.tiPassword.error = result.message
        } else binding.tiPassword.error = null

        return result.isValid
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