package com.orwa.gatherin.present.teacher.home.group_results

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.teacher_home.DepartmentsListRes
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseDepartmentFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = ChooseDepartmentFragmentViewModel::class.java.simpleName

    private val _departmentsListRes = MutableLiveData<DepartmentsListRes?>(null)
    val departmentsListRes: LiveData<DepartmentsListRes?> = _departmentsListRes

    //Used to know which department was selected when getting back from setResultFragment. useful to reflect item change......
    var currentSelectedDepartmentPos = -1


    fun getDepartmentsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getDepartmentsList()
//            _signInRes.value = res
            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _departmentsListRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
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




//    /**
//     * clear the result fetched from API. This is necessary when navigation from [signup or signin] to verificationCodeFragment
//     */
//    fun clearResult() {
//        _deleteGroupRes.value = null //Should be first to avoid continuous calling of requests
//        _groupsListRes.value = null
//        setNetworkState(AuthNetworkState.NONE)
//    }
}