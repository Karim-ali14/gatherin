package com.orwa.gatherin.present.start.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.model.auth.SignInReq
import com.orwa.gatherin.model.auth.SignInRes
import com.orwa.gatherin.rep.AuthRep
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.utils.LoadingState
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val rep: AuthRep
) : BaseNetworkViewModel() {

    private val TAG = LoginViewModel::class.java.simpleName

    enum class ToggleState {
        SHOW, HIDE
    }

    private val _signInRes = MutableLiveData<SignInRes?>()
    val signInRes: LiveData<SignInRes?> = _signInRes

    private val _loadingState = MutableLiveData<LoadingState>(LoadingState.NONE)
    val loadingState: LiveData<LoadingState> = _loadingState

    private var _toggleState: MutableLiveData<ToggleState> = MutableLiveData(ToggleState.SHOW)
    var toggleState: LiveData<ToggleState> = _toggleState

    fun updateToggle() {
        if (_toggleState.value == ToggleState.SHOW)
            _toggleState.value = ToggleState.HIDE
        else _toggleState.value = ToggleState.SHOW
    }


    fun signIn(req: SignInReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.signIn(req)
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _signInRes.value = data

//                Log.i(TAG, "success_res=$data")
//                Log.i(TAG, "status_code_res=$statusCode")

            }

            res.onError {
//                Log.i(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized, StatusCode.UnProcessableEntity ->
                        setNetworkState(AuthNetworkState.INVALID_CREDENTIALS)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }


            }

//            res.onFailure {
//
//                Log.i(TAG, "failure_res=")
//            }
            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.i(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    /**
     * clear the result fetched from API. This is necessary when navigation from [signup or signin] to verificationCodeFragment
     */
    fun clearResult() {
        _signInRes.value = null
        setNetworkState(AuthNetworkState.NONE)
    }
}