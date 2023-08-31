package com.orwa.gatherin.present.common.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = SettingsViewModel::class.java.simpleName

    private val _logOutRes = MutableLiveData<ResponseBody?>()
    val logOutRes: LiveData<ResponseBody?> = _logOutRes


    fun logOut() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.logOut()
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _logOutRes.value = data

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

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.i(
//                    TAG, "EXCEPTION_res=$message"
//                )
            }
        }
    }

}