package com.orwa.gatherin.present.teacher.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.present.start.login.LoginViewModel
import com.orwa.gatherin.rep.ProfileRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val rep: ProfileRep
) : BaseNetworkViewModel() {

    private val TAG = ChangePasswordViewModel::class.java.simpleName

    enum class ToggleState {
        SHOW, HIDE
    }

    private var _toggleState: MutableLiveData<LoginViewModel.ToggleState> = MutableLiveData(
        LoginViewModel.ToggleState.SHOW)
    var toggleState: LiveData<LoginViewModel.ToggleState> = _toggleState

    private var _toggleState2: MutableLiveData<LoginViewModel.ToggleState> = MutableLiveData(
        LoginViewModel.ToggleState.SHOW)
    var toggleState2: LiveData<LoginViewModel.ToggleState> = _toggleState2

    private var _toggleState3: MutableLiveData<LoginViewModel.ToggleState> = MutableLiveData(
        LoginViewModel.ToggleState.SHOW)
    var toggleState3: LiveData<LoginViewModel.ToggleState> = _toggleState3

    fun updateToggle() {
        if (_toggleState.value == LoginViewModel.ToggleState.SHOW)
            _toggleState.value = LoginViewModel.ToggleState.HIDE
        else _toggleState.value = LoginViewModel.ToggleState.SHOW
    }

    fun updateToggle2() {
        if (_toggleState2.value == LoginViewModel.ToggleState.SHOW)
            _toggleState2.value = LoginViewModel.ToggleState.HIDE
        else _toggleState2.value = LoginViewModel.ToggleState.SHOW
    }

    fun updateToggle3() {
        if (_toggleState3.value == LoginViewModel.ToggleState.SHOW)
            _toggleState3.value = LoginViewModel.ToggleState.HIDE
        else _toggleState3.value = LoginViewModel.ToggleState.SHOW
    }



    private val _changePwdRes = MutableLiveData<ResponseBody?>()
    val changePwdRes: LiveData<ResponseBody?> = _changePwdRes

    fun changePwd(oldPwd:String, newPwd:String, confirm:String) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.updatePwd(oldPwd, newPwd, confirm)
//            _signInRes.value = res
            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _changePwdRes.value = data

//                Log.i(TAG, "success_res=$data, status_code=$statusCode")

            }

            res.onError {
//                Log.i(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }

            }

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.i(
//                    TAG, "EXCEPTION_res=$message"
//                )
            }
        }
    }
}