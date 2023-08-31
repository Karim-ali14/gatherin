package com.orwa.gatherin.base

import android.os.Bundle
import com.orwa.gatherin.utils.Util
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator

abstract class BaseValidateDataFragment: BaseFragment(), Validator.ValidationListener {

    lateinit var mValidator: Validator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mValidator = Validator(this)
        mValidator.setValidationListener(this)
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        Util.showValidationErrorMessagesForFields(errors, requireContext())
    }
}