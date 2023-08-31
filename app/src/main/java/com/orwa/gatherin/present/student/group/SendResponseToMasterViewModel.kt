package com.orwa.gatherin.present.student.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.group.SendResponseToMasterReq
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
class SendResponseToMasterViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = SendResponseToMasterViewModel::class.java.simpleName

    private val _sendResToMasterRes = MutableLiveData<ResponseBody?>()
    val sendResToMasterRes: LiveData<ResponseBody?> = _sendResToMasterRes

    fun sendResponseToMaster(req:SendResponseToMasterReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.sendResponseToMaster(req)
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _sendResToMasterRes.value = data

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