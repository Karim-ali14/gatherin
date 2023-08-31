package com.orwa.gatherin.present.start.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateDataFragment
import com.orwa.gatherin.databinding.FragmentLoginBinding
import com.orwa.gatherin.model.auth.SignInReq
import com.orwa.gatherin.utils.*
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty

class LoginFragment : BaseValidateDataFragment() {

    private val TAG = LoginFragment::class.java.simpleName

    private val viewModel: LoginViewModel by viewModels()

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var emailET: EditText

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var passwordET: EditText

    lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.tvForgetPassword.setOnClickListener {
            val bundle = bundleOf(Constants.IS_RESET_PWD_PROCESS to true)
            navigate(R.id.action_loginFragment_to_registerEmailFragment, bundle)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val userType = arguments?.getString(Constants.USER_TYPE_KEY)
//        Toast.makeText(requireContext(), "user_type=$userType", Toast.LENGTH_SHORT).show()

        emailET = binding.emailEt
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

        //Get userType from TypeOfLoginFragment
        val userType = arguments?.getString(Constants.USER_TYPE_KEY)
//        Log.i(TAG,"USER_TYPE=$userType")
        val loginLabel = getLoginLabel(userType)
        loginLabel?.let {
            binding.tvCreateEditGroupTitle.text = it
        }

        val bundle = bundleOf(Constants.USER_TYPE_KEY to userType)
        binding.createAccountBtn.setOnClickListener { navigate(R.id.action_loginFragment_to_registerEmailFragment,bundle) }

        val loginBtn = binding.root.findViewById<ImageButton>(R.id.fab_login)


        loginBtn.setOnClickListener {
            mValidator.validate()
        }

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when(it){
                    AuthNetworkState.SUCCESS->toastSuccess(R.string.log_in_success)
                    AuthNetworkState.INVALID_CREDENTIALS -> toastFailure(R.string.log_in_fail)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE->toastFailure(R.string.network_error)
                    else -> {}
                }
                hideProgressDialog()

            }
        })

        viewModel.signInRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "response=$it")
            it?.let {
//                val userType = arguments?.getString(Constants.USER_TYPE_KEY)
                val user = User(
                    it.userId,
                    it.kind,
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
//                if(user.isVerified){
                    Pref.saveUserInfo(requireContext(), user)
                    if(user.type==Constants.USER_TYPE_STUDENT){
                        navigate(R.id.action_loginFragment_to_studentActivity)
                    }else{
                        navigate(R.id.action_loginFragment_to_teacherBottomNavActivity)
                    }
                    requireActivity().finish()
//                }
//                else{
//                    val b = bundleOf(
//                        Constants.USER_ID_KEY to it.userId,
//                        Constants.USER_EMAIL_KEY to emailET.text.trim().toString(),
//                        Constants.USER_TYPE_KEY to userType,
//                        Constants.USER_TOKEN_KEY to it.token
//                    )
//                    navigate(R.id.action_loginFragment_to_verificationCodeFragment,b)
//                    //Clear result to make sure that backing to this screen does not trigger the observer callback again
//                    viewModel.clearResult()
//                }
            }
        })
    }

    fun getLoginLabel(type:String?):String?{
        return if(type==null){
            null
        }else{
            val typeValue = if(type == Constants.USER_TYPE_TEACHER){
                getString(R.string.teacher)
            }else getString(R.string.student)
            getString(R.string.login_as_type,typeValue)
        }
    }

    override fun onValidationSucceeded() {
        hideKeyboardFrom(binding.root)

        val email = binding.emailEt.text.trim().toString()
        val pwd = binding.etPassword.text.trim().toString()
        val req = SignInReq(email, pwd)
        viewModel.signIn(req)
    }

}