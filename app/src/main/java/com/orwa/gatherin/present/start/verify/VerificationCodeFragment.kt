package com.orwa.gatherin.present.start.verify

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateDataFragment
import com.orwa.gatherin.databinding.FragmentVerificationCodeBinding
import com.orwa.gatherin.model.auth.VerifyCodeReq
import com.orwa.gatherin.utils.*
import com.mobsandgeeks.saripaar.annotation.NotEmpty

class VerificationCodeFragment : BaseValidateDataFragment() {

    private val TAG = VerificationCodeFragment::class.java.simpleName

    private val viewModel: VerifyViewModel by viewModels()


    lateinit var binding: FragmentVerificationCodeBinding

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    private lateinit var codeET: EditText

    val timer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val time = getTimerTextViewValue((millisUntilFinished / 1000).toInt())
            binding.verifyCodeTimer.text = time
        }

        override fun onFinish() {
            binding.etCode.text.clear()
            Util.showMsgDialog(
                requireContext(),
                R.string.enter_code_time_out,
                DialogInterface.OnClickListener { dialog, which ->
                    binding.sendCodeBtn.hide()
                    binding.resendCodeBtn.show()
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerificationCodeBinding.inflate(inflater, container, false)

        codeET = binding.etCode

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.codeMatch.value == true) { //Necessary to know if we get here from pop up process

        } else {
            sendVerifyCode()
        }

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        timer.start()
                        binding.sendCodeBtn.show()
                        binding.resendCodeBtn.hide()
                        binding.sendCodeBtn.text = getString(R.string.continue_btn)
                        toastSuccess(R.string.verify_code_sent_success)
                    }

                    AuthNetworkState.EMAIL_ALREADY_REGISTERED ->{
                        Util.showMsgDialog(requireContext(), R.string.email_already_registered,
                            DialogInterface.OnClickListener { dialog, which ->
                                findNavController().popBackStack()
                            })
                    }

                    AuthNetworkState.CONNECT_ERROR -> {
                        binding.sendCodeBtn.hide()
                        binding.resendCodeBtn.show()
                        toastFailure(R.string.connection_error)
                    }
                    AuthNetworkState.FAILURE -> {
                        binding.sendCodeBtn.hide()
                        binding.resendCodeBtn.show()
                        toastFailure(R.string.network_error)
                    }
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })



        binding.etCode.addTextChangedListener { editable ->
            val text = editable.toString()
            val value = viewModel.verifyCodeRes.value?.code
            value?.let {
//                Log.i(TAG, "viewModelValue=$value")

            }
//            Log.i(TAG, "TEXT=$text")
//            text.let { Log.i(TAG, "code_length=${it.length}") }


            text.let {
                if (text.length < 6) {
                    viewModel.setCodeMatchValue(null)
                } else {

                    if (text.equals(value)) {
                        viewModel.setCodeMatchValue(true)
                    } else {
                        viewModel.setCodeMatchValue(false)
                    }
                }

            }

        }

        viewModel.codeMatch.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "MATCH=$it")
            if (it == null) {
                binding.checkCodeImg.hide()
            } else {
                binding.checkCodeImg.show()
                if (it) {
                    binding.checkCodeImg.setImageResource(R.drawable.ic_done)
                } else {
                    binding.checkCodeImg.setImageResource(R.drawable.ic_error_copy_2)
                }
            }
        })

        binding.sendCodeBtn.setOnClickListener {
            mValidator.validate()

        }

        binding.resendCodeBtn.setOnClickListener {
            binding.etCode.setText("")
            sendVerifyCode()
        }

    }


    fun getTimerTextViewValue(secondsToFinish: Int): String {
        val min: Int = secondsToFinish / 60
        val sec: Int = secondsToFinish % 60
        return getString(R.string.timer_value, min, sec)
    }

    private fun sendVerifyCode() {
        val email = arguments?.getString(Constants.USER_EMAIL_KEY)
        val isResetPwdProcess:Boolean = arguments?.getBoolean(Constants.IS_RESET_PWD_PROCESS,false) == true

        email?.let {
            Log.d("VerificationCode", "EMAIL_HERE")
            viewModel.sendVerifyCode(VerifyCodeReq(email), isResetPwdProcess)
        }
    }

//    fun completeRegister() {
//
//        arguments?.let {
//            val userId = it.getInt(Constants.USER_ID_KEY)
//            val userType = it.getString(Constants.USER_TYPE_KEY)
//            val token = it.getString(Constants.USER_TOKEN_KEY)
//            //get expiresAt parameter value
//
//            val user = User(
//                userId,
//                userType,
//                null,
//                null,
//                token,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//            )
//            Pref.saveUserInfo(requireContext(), user)
//
//            requireActivity().finish()
//
//        }
//
//    }

    override fun onValidationSucceeded() {
        val userType = arguments?.getString(Constants.USER_TYPE_KEY)
        val email = arguments?.getString(Constants.USER_EMAIL_KEY)
        val isResetPwdProcess = arguments?.getBoolean(Constants.IS_RESET_PWD_PROCESS,false)

        if (viewModel.codeMatch.value == true) {
            //clear current status
            binding.etCode.text.clear()
            timer.cancel()
            viewModel.clearResponses()
            val bundle =
                bundleOf(Constants.USER_TYPE_KEY to userType, Constants.USER_EMAIL_KEY to email)
            if(isResetPwdProcess!=null && isResetPwdProcess == true){
                navigate(R.id.action_verificationCodeFragment_to_resetPasswordFragment,bundle)
            }else{
                navigate(R.id.action_verificationCodeFragment_to_registerFragment, bundle)

            }


        }
    }


}