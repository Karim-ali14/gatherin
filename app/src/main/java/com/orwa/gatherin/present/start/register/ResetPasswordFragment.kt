package com.orwa.gatherin.present.start.register

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateDataFragment
import com.orwa.gatherin.databinding.FragmentRegisterBinding
import com.orwa.gatherin.model.auth.SignUpReq
import com.orwa.gatherin.present.start.login.LoginViewModel
import com.orwa.gatherin.utils.*
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.orwa.gatherin.databinding.FragmentResetPasswordBinding
import com.orwa.gatherin.model.auth.ResetPasswordReq


class ResetPasswordFragment : BaseValidateDataFragment() {

    private val TAG = ResetPasswordFragment::class.java.simpleName

    private val viewModel: ResetViewModel by viewModels()

//    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
//    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
//    private lateinit var userNameET: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 6, messageResId = R.string.validate_input_field_length_6)
    private lateinit var passwordET: EditText


    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 6, messageResId = R.string.validate_input_field_length_6)
    private lateinit var confirmET: EditText


    lateinit var binding: FragmentResetPasswordBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString(Constants.USER_EMAIL_KEY)

        binding.ettMail.setText(email)

        passwordET = binding.etPassword
        confirmET = binding.etConfirmPassword

        binding.togglePwd.setOnClickListener {
            viewModel.updateToggle()
        }

        binding.toggleConfirmPwd.setOnClickListener {
            viewModel.updateConfirmToggle()
        }

        viewModel.toggleState.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.ToggleState.SHOW) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.togglePwd.setImageResource(R.mipmap.ic_eye_close)
            } else {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.togglePwd.setImageResource(R.mipmap.ic_eye_open)

            }
        })

        viewModel.confirmToggleState.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.ToggleState.SHOW) {
                binding.etConfirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.toggleConfirmPwd.setImageResource(R.mipmap.ic_eye_close)
            } else {
                binding.etConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.toggleConfirmPwd.setImageResource(R.mipmap.ic_eye_open)

            }
        })


        binding.nextBtn.setOnClickListener {
            mValidator.validate()
        }

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> toastSuccess(R.string.reset_password_success)
                    AuthNetworkState.EMAIL_NOT_FOUND ->toastFailure(R.string.email_not_found)

                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.resetPasswordRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().popBackStack(R.id.loginFragment,false)
            }
        })
    }


    override fun onValidationSucceeded() {

        val email = binding.ettMail.text.trim().toString()
        val pwd = binding.etPassword.text.toString().trim()
        val confirm = binding.etConfirmPassword.text.toString().trim()

        if (!pwd.equals(confirm)) {
            toastFailure(R.string.password_not_identical_error_msg)
        } else {
            val req = ResetPasswordReq(email,pwd,confirm)
            viewModel.resetPassword(req)
        }

    }

}