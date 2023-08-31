package com.orwa.gatherin.present.start.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseValidateDataFragment
import com.orwa.gatherin.databinding.FragmentRegisterEmailBinding
import com.orwa.gatherin.present.start.StartActivityViewModel
import com.orwa.gatherin.utils.*
import com.mobsandgeeks.saripaar.annotation.NotEmpty

class RegisterEmailFragment : BaseValidateDataFragment() {

    private val TAG = RegisterEmailFragment::class.java.simpleName


    lateinit var binding: FragmentRegisterEmailBinding


    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    private lateinit var emailET: EditText

    private val activityViewModel: StartActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterEmailBinding.inflate(inflater, container, false)

        emailET = binding.emailEt

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activityViewModel.phone.observe(viewLifecycleOwner, Observer {
            binding.emailEt.setText(it)
        })

        binding.nextBtn.setOnClickListener {
            mValidator.validate()


        }
    }

    override fun onValidationSucceeded() {
        val email = binding.emailEt.text.trim().toString()
        activityViewModel.setPhone(email)
        //Get userType from TypeOfLoginFragment
        val userType = arguments?.getString(Constants.USER_TYPE_KEY)
//        Log.i(TAG, "USER_TYPE=$userType")
        val isReset = arguments?.getBoolean(Constants.IS_RESET_PWD_PROCESS,false)
        val bundle = bundleOf(Constants.USER_TYPE_KEY to userType, Constants.USER_EMAIL_KEY to email, Constants.IS_RESET_PWD_PROCESS to isReset)
        navigate(R.id.action_registerEmailFragment_to_verificationCodeFragment,bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.setPhone(null)
    }
}