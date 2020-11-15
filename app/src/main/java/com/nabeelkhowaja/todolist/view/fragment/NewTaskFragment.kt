package com.nabeelkhowaja.todolist.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nabeelkhowaja.todolist.R
import com.nabeelkhowaja.todolist.databinding.FragmentNewTaskBinding
import com.nabeelkhowaja.todolist.viewmodel.TodoViewModel
import kotlinx.coroutines.*


class NewTaskFragment : NonCollapsibleBottomSheetDialogFragment() {

    private var mBinding: FragmentNewTaskBinding? = null
    private val binding get() = mBinding!! //overriding getter of mBinding object to avoid handling nullability
    private lateinit var viewModel: TodoViewModel

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNewTaskBinding.inflate(inflater, container, false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.etTask.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim() == "") {
                    binding.btnSave.isEnabled = false
                    binding.btnSave.setTextColor(Color.GRAY)
                } else {
                    binding.btnSave.isEnabled = true
                    binding.btnSave.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.navy_blue
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.btnSave.setOnClickListener {
            uiScope.launch {
                viewModel.insertTask(binding.etTask.text.toString().trim())
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): NewTaskFragment {
            return NewTaskFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Cancelling the job so that all coroutines tight to this gets cancelled
        viewModelJob.cancel()
        uiScope.cancel()
    }
}