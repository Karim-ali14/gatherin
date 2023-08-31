package com.orwa.gatherin.present.teacher.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.pack.PackageRes
import com.orwa.gatherin.model.teacher_home.DepartmentsListRes
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
class HomeTeacherFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = HomeTeacherFragmentViewModel::class.java.simpleName

    private val _departmentsListRes = MutableLiveData<DepartmentsListRes?>(null)
    val departmentsListRes: LiveData<DepartmentsListRes?> = _departmentsListRes

    private val _deleteDepartmentRes = MutableLiveData<ResponseBody?>(null)
    val deleteDepartmentRes: LiveData<ResponseBody?> = _deleteDepartmentRes

    private val _joinDepartmentNetworkState = MutableLiveData(AuthNetworkState.NONE)
    val joinDepartmentNetworkState: LiveData<AuthNetworkState> = _joinDepartmentNetworkState

    private val _addUserToDepartmentRes = MutableLiveData<ResponseBody?>(null)
    val addUserToDepartmentRes: LiveData<ResponseBody?> = _addUserToDepartmentRes

    var rvPosition = -1
    var nestedScrollY = -1

//    private val _packStatusRes = MutableLiveData<PackageRes?>(null)
//    val packStatusRes: LiveData<PackageRes?> = _packStatusRes



    fun getDepartmentsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getDepartmentsList()
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _departmentsListRes.value = data
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

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    fun deleteDepartment(departmentId:Int) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.deleteDepartment(departmentId)
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _deleteDepartmentRes.value = data
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

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }


    fun addUserToDepartment(code:String) {
        _joinDepartmentNetworkState.value = AuthNetworkState.LOADING
        viewModelScope.launch {
            val res = rep.addUserToDepartment(code)
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                _joinDepartmentNetworkState.value = AuthNetworkState.SUCCESS
                _addUserToDepartmentRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")
                when(statusCode){
                    StatusCode.BadRequest ->{ _joinDepartmentNetworkState.value = AuthNetworkState.DEPARTMENTS_FULL_ERROR }
                    StatusCode.Conflict ->{ _joinDepartmentNetworkState.value = AuthNetworkState.DEPARTMENT_ADD_USER_ERROR }
                    else -> _joinDepartmentNetworkState.value = AuthNetworkState.FAILURE
                }

            }
            res.onException {
                _joinDepartmentNetworkState.value = AuthNetworkState.CONNECT_ERROR

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }


    /**
     * clear the result fetched from API.
     */
    fun clearResult() {
        _deleteDepartmentRes.value = null //Should be first to avoid continuous calling of requests
        _departmentsListRes.value = null
        setNetworkState(AuthNetworkState.NONE)
    }

    fun clearOtherResult(){
        _joinDepartmentNetworkState.value = AuthNetworkState.NONE

    }

}