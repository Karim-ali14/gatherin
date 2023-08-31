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
import com.orwa.gatherin.present.start.login.LoginViewModel
import com.orwa.gatherin.utils.LoadingState
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val rep: AuthRep
) : ViewModel() {

    private val TAG = RegisterViewModel::class.java.simpleName

    enum class ToggleState {
        SHOW, HIDE
    }

    private val _signUpRes = MutableLiveData<SignUpRes?>()
    val signUpRes: LiveData<SignUpRes?> = _signUpRes

    private val _loadingState = MutableLiveData(LoadingState.NONE)
    val loadingState: LiveData<LoadingState> = _loadingState

    private val _networkState = MutableLiveData(AuthNetworkState.NONE)
    val networkState: LiveData<AuthNetworkState> = _networkState

    private var _toggleState: MutableLiveData<LoginViewModel.ToggleState> = MutableLiveData(
        LoginViewModel.ToggleState.SHOW)
    var toggleState: LiveData<LoginViewModel.ToggleState> = _toggleState

    fun updateToggle() {
        if (_toggleState.value == LoginViewModel.ToggleState.SHOW)
            _toggleState.value = LoginViewModel.ToggleState.HIDE
        else _toggleState.value = LoginViewModel.ToggleState.SHOW
    }


    fun signUp(req: SignUpReq) {
        _networkState.value = AuthNetworkState.LOADING
        viewModelScope.launch {
            val res = rep.signUp(req)
            Log.e(TAG, "res=$res")
            res.onSuccess {
                _networkState.value = AuthNetworkState.SUCCESS
                _signUpRes.value = data

//                Log.i(TAG, "success_res=$data")
//                Log.i(TAG, "status_code_res=$statusCode")

            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                     StatusCode.Conflict -> _networkState.value =
                        AuthNetworkState.EMAIL_ALREADY_REGISTERED
                    else -> _networkState.value = AuthNetworkState.FAILURE
                }


            }

            res.onException {
                _networkState.value = AuthNetworkState.CONNECT_ERROR

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    /**
     * clear the result fetched from API. This is necessary when navigation from [signup or signin] to verificationCodeFragment
     */
    fun clearResult(){
        _signUpRes.value=null
        _networkState.value = AuthNetworkState.NONE
    }
}