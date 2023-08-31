package com.orwa.gatherin.present.start.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.model.auth.SignUpReq
import com.orwa.gatherin.model.auth.SignUpRes
import com.orwa.gatherin.rep.AuthRep
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.auth.ResetPasswordReq
import com.orwa.gatherin.present.start.login.LoginViewModel
import com.orwa.gatherin.utils.LoadingState
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val rep: AuthRep
) : BaseNetworkViewModel() {

    private val TAG = ResetViewModel::class.java.simpleName

    enum class ToggleState {
        SHOW, HIDE
    }

    private val _resetPasswordRes = MutableLiveData<ResponseBody?>()
    val resetPasswordRes: LiveData<ResponseBody?> = _resetPasswordRes


    private var _toggleState: MutableLiveData<LoginViewModel.ToggleState> = MutableLiveData(
        LoginViewModel.ToggleState.SHOW
    )
    var toggleState: LiveData<LoginViewModel.ToggleState> = _toggleState


    private var _confirmToggleState: MutableLiveData<LoginViewModel.ToggleState> = MutableLiveData(
        LoginViewModel.ToggleState.SHOW
    )
    var confirmToggleState: LiveData<LoginViewModel.ToggleState> = _confirmToggleState

    fun updateToggle() {
        if (_toggleState.value == LoginViewModel.ToggleState.SHOW)
            _toggleState.value = LoginViewModel.ToggleState.HIDE
        else _toggleState.value = LoginViewModel.ToggleState.SHOW
    }

    fun updateConfirmToggle() {
        if (_confirmToggleState.value == LoginViewModel.ToggleState.SHOW)
            _confirmToggleState.value = LoginViewModel.ToggleState.HIDE
        else _confirmToggleState.value = LoginViewModel.ToggleState.SHOW
    }


    fun resetPassword(req: ResetPasswordReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.resetPassword(req)
//            Log.d(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _resetPasswordRes.value = data

//                Log.i(TAG, "status_code_res=$statusCode")

            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Conflict -> setNetworkState(
                        AuthNetworkState.EMAIL_NOT_FOUND
                    )
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }

            }

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

                Log.e(
                    TAG, "EXCEPTION_res=$message"
                )
            }
        }
    }
}