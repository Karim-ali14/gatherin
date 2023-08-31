package com.orwa.gatherin.present.start.verify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.model.auth.VerifyCodeReq
import com.orwa.gatherin.model.auth.VerifyCodeRes
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
class VerifyViewModel @Inject constructor(
    private val rep: AuthRep
) : BaseNetworkViewModel() {

    private val TAG = VerifyViewModel::class.java.simpleName

//    private val _codeVal = MutableLiveData<Int?>(null)
//    val codeVal : LiveData<Int?> = _codeVal

    private val _codeMatch = MutableLiveData<Boolean?>(null)
    val codeMatch: LiveData<Boolean?> = _codeMatch

    private val _verifyCodeRes = MutableLiveData<VerifyCodeRes?>()
    val verifyCodeRes: LiveData<VerifyCodeRes?> = _verifyCodeRes

    private val _loadingState = MutableLiveData<LoadingState>(LoadingState.NONE)
    val loadingState: LiveData<LoadingState> = _loadingState


    fun sendVerifyCode(req: VerifyCodeReq, isForgot:Boolean = false) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = if(isForgot) rep.verifyCodeForgot(req) else rep.verifyCode(req)
//            _signInRes.value = res
            Log.d(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _verifyCodeRes.value = data

//                Log.i(TAG, "success_res=$data")
//                Log.i(TAG, "status_code_res=$statusCode")

            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")
                when(statusCode){
                    StatusCode.Conflict -> setNetworkState(AuthNetworkState.EMAIL_ALREADY_REGISTERED)
                    else-> setNetworkState(AuthNetworkState.FAILURE)
                }

            }

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
            }
        }
    }

    /**
     * Set if the entered code matches the code was sent to the user email
     */
    fun setCodeMatchValue(value: Boolean?) {
        _codeMatch.value = value
    }

    fun clearResponses() {
        setNetworkState(AuthNetworkState.NONE)
        setCodeMatchValue(null)
        _verifyCodeRes.value = null
    }

//    fun setCodeValue(value:Int){
//
//    }
}