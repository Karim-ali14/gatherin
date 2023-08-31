package com.orwa.gatherin.present.teacher.home.section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.section.GroupListRes
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
class SectionDetailsFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = SectionDetailsFragmentViewModel::class.java.simpleName

    private val _groupsListRes = MutableLiveData<GroupListRes?>(null)
    val groupsListRes: LiveData<GroupListRes?> = _groupsListRes

    private val _deleteGroupRes = MutableLiveData<ResponseBody?>(null)
    val deleteGroupRes: LiveData<ResponseBody?> = _deleteGroupRes


    fun getGroupsList(departmentId:Int) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getGroupsList(departmentId)
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _groupsListRes.value = data
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

    fun deleteGroup(groupId:Int) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.deleteGroup(groupId)
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _deleteGroupRes.value = data
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



    /**
     * clear the result fetched from API. This is necessary when navigation from [signup or signin] to verificationCodeFragment
     */
    fun clearResult() {
        _deleteGroupRes.value = null //Should be first to avoid continuous calling of requests
        _groupsListRes.value = null
        setNetworkState(AuthNetworkState.NONE)
    }
}