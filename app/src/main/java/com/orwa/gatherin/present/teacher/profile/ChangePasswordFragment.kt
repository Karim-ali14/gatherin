package com.orwa.gatherin.present.teacher.profile

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.FragmentChangePwdBinding
import com.orwa.gatherin.base.BaseValidateTeacherFragment
import com.orwa.gatherin.present.start.login.LoginViewModel
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty


class ChangePasswordFragment : BaseValidateTeacherFragment() {

    private val TAG = ChangePasswordFragment::class.java.simpleName

    lateinit var binding: FragmentChangePwdBinding

    private val viewModel: ChangePasswordViewModel by viewModels()

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var oldPwdET: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var newPwdET: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var confirmPwdET: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChangePwdBinding.inflate(inflater, container, false)

        oldPwdET = binding.etOldPwd
        newPwdET = binding.etNewPwd
        confirmPwdET = binding.etConfirmPwd

        binding.btnConfirm.setOnClickListener {
            validator.validate()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaderTitle(getString(R.string.change_password))

        binding.togglePwd.setOnClickListener {
            viewModel.updateToggle()
        }

        binding.newTogglePwd.setOnClickListener {
            viewModel.updateToggle2()
        }

        binding.confirmTogglePwd.setOnClickListener {
            viewModel.updateToggle3()
        }

        viewModel.toggleState.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.ToggleState.SHOW) {
                binding.etOldPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.togglePwd.setImageResource(R.mipmap.ic_eye_close)
            } else {
                binding.etOldPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.togglePwd.setImageResource(R.mipmap.ic_eye_open)

            }
        })

        viewModel.toggleState2.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.ToggleState.SHOW) {
                binding.etNewPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.newTogglePwd.setImageResource(R.mipmap.ic_eye_close)
            } else {
                binding.etNewPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.newTogglePwd.setImageResource(R.mipmap.ic_eye_open)

            }
        })

        viewModel.toggleState3.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.ToggleState.SHOW) {
                binding.etConfirmPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.confirmTogglePwd.setImageResource(R.mipmap.ic_eye_close)
            } else {
                binding.etConfirmPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.confirmTogglePwd.setImageResource(R.mipmap.ic_eye_open)

            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.change_password_success)
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.change_pwd_incorrect_old)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.changePwdRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //popup to previous screen
                findNavController().popBackStack()
            }
        })
    }

    override fun onValidationSucceeded() {
        if (!checkPwdMatch()) { //no match
            toastFailure(R.string.pwd_no_match)
        } else {
            hideKeyboardFrom(binding.root)
            val oldPwd = oldPwdET.text.trim().toString()
            val newPwd = newPwdET.text.trim().toString()
            val confirm = confirmPwdET.text.trim().toString()
            viewModel.changePwd(oldPwd, newPwd, confirm)
        }
    }

    fun checkPwdMatch(): Boolean {
        return binding.etNewPwd.text.trim().toString()
            .equals(binding.etConfirmPwd.text.trim().toString(), ignoreCase = false)
    }
}