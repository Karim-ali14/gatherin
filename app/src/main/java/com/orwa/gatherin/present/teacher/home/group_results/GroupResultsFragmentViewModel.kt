package com.orwa.gatherin.present.teacher.home.group_results

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.group.AllGroupsResItem
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupResultsFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = GroupResultsFragmentViewModel::class.java.simpleName

    private val _groupsListRes = MutableLiveData<ArrayList<AllGroupsResItem>?>(null)
    val groupsListRes: LiveData<ArrayList<AllGroupsResItem>?> = _groupsListRes

    fun getGroupsResultsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getAllGroups()
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _groupsListRes.value = data
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