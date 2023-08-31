package com.orwa.gatherin.present.teacher.home.send_quest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.group.AllGroupsResItem
import com.orwa.gatherin.model.group.SendQuestionReq
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
class SendQuestionsFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = SendQuestionsFragmentViewModel::class.java.simpleName

    private val _groupsListRes = MutableLiveData<ArrayList<AllGroupsResItem>?>(null)
    val groupsListRes: LiveData<ArrayList<AllGroupsResItem>?> = _groupsListRes

    private val _sendQuestionRes = MutableLiveData<ResponseBody?>(null)
    val sendQuestionRes: LiveData<ResponseBody?> = _sendQuestionRes

    private val _sendQuestionNetworkState = MutableLiveData(AuthNetworkState.NONE)
    val sendQuestionNetworkState: LiveData<AuthNetworkState> = _sendQuestionNetworkState

    fun getGroupsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getAllGroups() // TODO: 6/23/2021
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

    fun sendQuestion(sendReq:SendQuestionReq) {
        _sendQuestionNetworkState.value = AuthNetworkState.LOADING
        viewModelScope.launch {
            val res = rep.sendQuestion(sendReq) // TODO: 6/23/2021
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                _sendQuestionNetworkState.value = AuthNetworkState.SUCCESS
                _sendQuestionRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> _sendQuestionNetworkState.value = AuthNetworkState.USER_UNAUTHORIZED
                    else -> _sendQuestionNetworkState.value = AuthNetworkState.FAILURE
                }
            }
            res.onException {
                _sendQuestionNetworkState.value = AuthNetworkState.CONNECT_ERROR

//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
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