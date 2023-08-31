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
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateTeacherFragment
import com.orwa.gatherin.databinding.FragmentEditSectionBinding
import com.orwa.gatherin.present.teacher.home.TeacherHomeFragment
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty

class EditSectionFragment : BaseValidateTeacherFragment() {


    private lateinit var binding: FragmentEditSectionBinding

    private val viewModel: EditSectionFragmentViewModel by viewModels()

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var nameET: EditText


    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var codeET: EditText

    private var sectionId = -1

    val request = {
        viewModel.updateDepartment(sectionId,nameET.text.toString().trim(),codeET.text.toString().trim())}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditSectionBinding.inflate(inflater, container, false)

        nameET = binding.etSectionName
        codeET = binding.etSectionCode

        return binding.root
    }

    override fun onValidationSucceeded() {
        hideKeyboardFrom(binding.root)
        request.invoke()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            sectionId = it.getInt(TeacherHomeFragment.SECT_ID_KEY)
            val secName = it.getString(TeacherHomeFragment.SECT_NAME_KEY)
            val secCode = it.getString(TeacherHomeFragment.SECT_CODE_KEY)

            nameET.setText(secName)
            codeET.setText(secCode)
        }

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> toastSuccess(R.string.update_section_success)
                    AuthNetworkState.SECTION_ALREADY_REGISTERED -> toastFailure(R.string.create_section_code_already_exists)
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.updateDepartmentRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val result = true
                // Use the Kotlin extension in the fragment-ktx artifact
                setFragmentResult(
                    TeacherHomeFragment.UPDATE_SECTION_REQUEST_KEY, bundleOf(
                        TeacherHomeFragment.BUNDLE_KEY to true
//                        TeacherHomeFragment.SECT_ID_KEY to sectionId,
//                        TeacherHomeFragment.SECT_NAME_KEY to nameET,
//                        TeacherHomeFragment.SECT_CODE_KEY to codeET
                    )
                )
                findNavController().popBackStack()
            }

        })

        binding.confirmBtn.setOnClickListener {
            hideKeyboardFrom(binding.root)
            validator.validate()
        }

    }
}