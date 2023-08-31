package com.orwa.gatherin.present.student.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateTeacherFragment
import com.orwa.gatherin.databinding.FragmentSendResponseToMasterBinding
import com.orwa.gatherin.model.group.SendResponseToMasterReq
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty


class SendResponseToMasterFragment : BaseValidateTeacherFragment() {

    private val TAG = SendResponseToMasterFragment::class.java.simpleName

    lateinit var binding: FragmentSendResponseToMasterBinding

    private val viewModel: SendResponseToMasterViewModel by viewModels()

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var responseET: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSendResponseToMasterBinding.inflate(inflater, container, false)

        responseET = binding.etWriteAnswer

        binding.btnSend.setOnClickListener {
            validator.validate()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaderTitle(getString(R.string.sendAnswerToMasterEnabled))

        activityViewModel.groupLeader.observe(viewLifecycleOwner, Observer {

        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.response_to_master_sent_success)
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.sendResToMasterRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //popup to previous screen
                findNavController().popBackStack()
            }
        })
    }

    override fun onValidationSucceeded() {

        hideKeyboardFrom(binding.root)
        val res = responseET.text.trim().toString()
        activityViewModel.currentGroup.value?.let {
            arguments?.let { bundle->
                val req = SendResponseToMasterReq(it.id,bundle.getInt(QUESTION_ID_KEY),res)
                viewModel.sendResponseToMaster(req)
            }

        }

    }

    companion object{
        const val QUESTION_ID_KEY="question_id"
    }

}