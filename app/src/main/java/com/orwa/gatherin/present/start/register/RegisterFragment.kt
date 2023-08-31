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


class RegisterFragment : BaseValidateDataFragment() {

    private val TAG = RegisterFragment::class.java.simpleName

    private val viewModel: RegisterViewModel by viewModels()

//    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
//    @Email(sequence = 2, messageResId = R.string.validate_email_invalid_field)
//    private lateinit var emailET: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var userNameET: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 6, messageResId = R.string.validate_input_field_length_6)
    private lateinit var passwordET: EditText


    lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString(Constants.USER_EMAIL_KEY)

        binding.ettMail.setText(email)

        userNameET = binding.etUserName
        passwordET = binding.etPassword

        binding.togglePwd.setOnClickListener {
            viewModel.updateToggle()
        }

        viewModel.toggleState.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.ToggleState.SHOW) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.togglePwd.setImageResource(R.mipmap.ic_eye_close)
            } else {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.togglePwd.setImageResource(R.mipmap.ic_eye_open)

            }
        })

//        binding.backLogin.setOnClickListener { findNavController().popBackStack() }

        val test = binding.root.findViewById<ImageButton>(R.id.fab_login)
        test.setOnClickListener {
            mValidator.validate()
        }

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> toastSuccess(R.string.sign_up_success)
                    AuthNetworkState.EMAIL_ALREADY_REGISTERED -> {
//                        toastFailure(R.string.email_already_registered)
                        showEmailAlreadyRegisteredDialog()
                    }
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.signUpRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "response=$it")
            it?.let {
                val userType = arguments?.getString(Constants.USER_TYPE_KEY)
                val user = User(
                    it.userId,
                    userType,
                    null,
                    null,
                    it.token,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
                Pref.saveUserInfo(requireContext(), user)
                if(userType==Constants.USER_TYPE_TEACHER){
                    navigate(R.id.action_registerFragment_to_teacherBottomNavActivity)
                    requireActivity().finish()
                }else if(userType==Constants.USER_TYPE_STUDENT){
                    navigate(R.id.action_registerFragment_to_studentActivity)
                    requireActivity().finish()
                }else{
                    toastFailure(R.string.user_role_not_defined)
                }
//                val bundle = bundleOf(
//                    Constants.USER_ID_KEY to it.userId,
//                    Constants.USER_EMAIL_KEY to binding.ettMail.text.trim().toString(),
//                    Constants.USER_TYPE_KEY to userType,
//                    Constants.USER_TOKEN_KEY to it.token
//                )
//                navigate(R.id.action_registerFragment_to_verificationCodeFragment, bundle)
//                viewModel.clearResult()

            }
        })
    }


    override fun onValidationSucceeded() {

        val email = binding.ettMail.text.trim().toString()
        val userName = binding.etUserName.text.trim().toString()
        val pwd = binding.etPassword.text.trim().toString()

        val userType = arguments?.getString(Constants.USER_TYPE_KEY)

        userType?.let {
            val req = SignUpReq(userName, email, it, pwd, pwd)
            viewModel.signUp(req)
        }

    }

    private fun showEmailAlreadyRegisteredDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = View.inflate(context, R.layout.account_already_registered, null)
        val cancel = dialogView.findViewById<ImageButton>(R.id.cancel_btn)
        val login = dialogView.findViewById<Button>(R.id.login_btn)
//        val forgot = dialogView.findViewById<Button>(R.id.forgot_pwd_btn)


        builder.setView(dialogView)
        val dialog = builder.create()

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        login.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }
        dialog.show()

    }
}