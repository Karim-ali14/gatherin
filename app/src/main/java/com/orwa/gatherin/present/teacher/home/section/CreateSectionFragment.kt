package com.orwa.gatherin.present.teacher.home.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.FragmentCreateSectionBinding
import com.orwa.gatherin.model.section.CreateSectionReq
import com.orwa.gatherin.present.teacher.home.TeacherHomeFragment.Companion.BUNDLE_KEY
import com.orwa.gatherin.present.teacher.home.TeacherHomeFragment.Companion.CREATE_SECTION_REQUEST_KEY
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateTeacherFragment
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.orwa.gatherin.model.section.CreateSectionReq2

class CreateSectionFragment : BaseValidateTeacherFragment() {

    private lateinit var binding: FragmentCreateSectionBinding

    private val viewModel: CreateSectionViewModel by viewModels()

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var nameET: EditText

//    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
//    @Length(sequence = 2, min = 6, max = 6, messageResId = R.string.validate_input_field_length_6_exact)
//    private lateinit var codeET: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentCreateSectionBinding.inflate(inflater, container, false)

        nameET = binding.etSectionName
//        codeET = binding.etSectionCode

        binding.confirmBtn.setOnClickListener {
            validator.validate()
        }

        binding.cbAuto.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.setAutoValue(isChecked)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                hideProgressDialog()
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.create_section_success)
                    }
                    AuthNetworkState.SECTION_ALREADY_REGISTERED -> {
                        toastFailure(R.string.create_section_code_already_exists)
                    }
                    AuthNetworkState.FORBIDDEN -> toastFailure(R.string.create_section_error_max)
                    AuthNetworkState.MALFORMED_INPUT_DATA -> toastFailure(R.string.create_section_code_lamformed_exists)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
            }
        })

        viewModel.autoGenerateCode.observe(viewLifecycleOwner, Observer {
            binding.etSectionCode.isEnabled = !it
        })

        viewModel.createSectionRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val result = true
                // Use the Kotlin extension in the fragment-ktx artifact
                setFragmentResult(CREATE_SECTION_REQUEST_KEY, bundleOf(BUNDLE_KEY to result))
                findNavController().popBackStack()
            }

        })
    }

    override fun onValidationSucceeded() {
        val name = binding.etSectionName.text.toString().trim()
        val code = binding.etSectionCode.text.toString().trim()

        if(binding.cbAuto.isChecked){
            val req = CreateSectionReq2(name)
            hideKeyboardFrom(binding.root)
            viewModel.createSection(req)
        }else{
            if(code.isEmpty() || code.length<6){ // Code not entered by the user
                binding.etSectionCode.setError(getString(R.string.validate_input_field_length_6_exact))
            }else{
                val req = CreateSectionReq(name, code)
                hideKeyboardFrom(binding.root)
                viewModel.createSection(req)
            }
        }

    }

}