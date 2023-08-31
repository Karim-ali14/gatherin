package com.orwa.gatherin.present.common.about

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.about.AboutAppResItem
import com.orwa.gatherin.rep.ProfileRep
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val rep: ProfileRep) : BaseNetworkViewModel() {

    private val TAG=AboutViewModel::class.java.simpleName

    private val _aboutRes = MutableLiveData<AboutAppResItem?>(null)
    val aboutRes: LiveData<AboutAppResItem?> = _aboutRes

    fun getAboutAppRes() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getAboutApp()
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _aboutRes.value = data

//                Log.i(TAG, "success_res=$data")
//                Log.i(TAG, "status_code_res=$statusCode")

            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")
                setNetworkState(AuthNetworkState.FAILURE)
            }

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
            }
        }
    }


}