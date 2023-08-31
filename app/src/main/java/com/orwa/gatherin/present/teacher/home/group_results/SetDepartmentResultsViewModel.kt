package com.orwa.gatherin.present.teacher.home.group_results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.group_results.SendResultsReq
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
class SetDepartmentResultsViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = SetDepartmentResultsViewModel::class.java.simpleName

    private val _setResultsRes = MutableLiveData<ResponseBody?>(null)
    val setResultsRes: LiveData<ResponseBody?> = _setResultsRes


    fun sendResults(req:SendResultsReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.sendResults(req)
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _setResultsRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
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

}