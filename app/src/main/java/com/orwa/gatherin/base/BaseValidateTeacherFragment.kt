package com.orwa.gatherin.base

import android.os.Bundle
import com.orwa.gatherin.utils.Util
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator

abstract class BaseValidateTeacherFragment : BaseTeacherFragment(), Validator.ValidationListener {
    lateinit var validator: Validator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validator = Validator(this)
        validator.setValidationListener(this)
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        Util.showValidationErrorMessagesForFields(errors, requireContext())
    }
}