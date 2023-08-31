package com.orwa.gatherin.present.teacher.home.section

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
class EditSectionFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = EditSectionFragmentViewModel::class.java.simpleName

    private val _updateDepartmentRes = MutableLiveData<ResponseBody?>(null)
    val updateDepartmentRes: LiveData<ResponseBody?> = _updateDepartmentRes

    fun updateDepartment(departmentId:Int, name:String, code:String) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.updateDepartment(departmentId, name, code)
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _updateDepartmentRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.BadRequest ->setNetworkState(AuthNetworkState.SECTION_ALREADY_REGISTERED)
                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }


//    /**
//     * clear the result fetched from API. This is necessary when navigation from [signup or signin] to verificationCodeFragment
//     */
//    fun clearResult() {
//        _deleteDepartmentRes.value = null //Should be first to avoid continuous calling of requests
//        _departmentsListRes.value = null
//        setNetworkState(AuthNetworkState.NONE)
//    }
}