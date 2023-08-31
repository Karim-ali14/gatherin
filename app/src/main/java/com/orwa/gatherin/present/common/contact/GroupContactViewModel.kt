package com.orwa.gatherin.present.common.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.auth.SignInReq
import com.orwa.gatherin.model.chat.ReportReq
import com.orwa.gatherin.model.chat.TextAnswer
import com.orwa.gatherin.model.other.UploadFileRes
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class GroupContactViewModel @Inject constructor(private val rep: HomeRep) : BaseNetworkViewModel() {

    private val TAG = ContactFragment::class.java.simpleName

    private val _uploadFileRes = MutableLiveData<UploadFileRes?>()
    val uploadFileRes: LiveData<UploadFileRes?> = _uploadFileRes

    private val _sendAnswerRes = MutableLiveData<ResponseBody?>()
    val sendAnswerRes: LiveData<ResponseBody?> = _sendAnswerRes

    private val _messagesStatusRes = MutableLiveData<ResponseBody?>()
    val messagesStatusRes: LiveData<ResponseBody?> = _messagesStatusRes

    private val _sendReportRes = MutableLiveData<ResponseBody?>()
    val sendReportRes: LiveData<ResponseBody?> = _sendReportRes

    private val _sendAnswerNetworkState = MutableLiveData(AuthNetworkState.NONE)
    val sendAnswerNetworkState: LiveData<AuthNetworkState> = _sendAnswerNetworkState

    fun uploadFile(req: MultipartBody.Part, type: String) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.uploadFile(req)
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _uploadFileRes.value = data

//                Log.i(TAG, "success_res=$data, status_coe=$statusCode")

            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {

                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
                setNetworkState(AuthNetworkState.CONNECT_ERROR)
            }
        }
    }

    fun changeMessagesStatus(otherId: Int) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.changeMessagesStatus(otherId)
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _messagesStatusRes.value = data

            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {

                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
                setNetworkState(AuthNetworkState.CONNECT_ERROR)
            }
        }
    }


    //    fun sendTextAnswer(req: TextAnswer) {
//        _sendAnswerNetworkState.value = AuthNetworkState.LOADING
//        viewModelScope.launch {
//            val res = rep.sendAnswer(req)
////            Log.i(TAG, "res=$res")
//            res.onSuccess {
//                _sendAnswerNetworkState.value = AuthNetworkState.SUCCESS
//                _sendAnswerRes.value = data
//
////                Log.i(TAG, "success_res=$data, status_coe=$statusCode")
//
//            }
//
//            res.onError {
////                Log.e(TAG, "error_res=$statusCode")
//
//                when (statusCode) {
//                    StatusCode.Unauthorized -> _sendAnswerNetworkState.value = AuthNetworkState.USER_UNAUTHORIZED
//                    else -> _sendAnswerNetworkState.value = AuthNetworkState.FAILURE
//                }
//            }
//            res.onException {
////                Log.e(
////                    TAG, "EXCEPTION_res=$message"
////                )
//                _sendAnswerNetworkState.value = AuthNetworkState.CONNECT_ERROR
//            }
//        }
//    }
    fun sendReport(req: ReportReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.reportPersonGroup(req)
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _sendReportRes.value = data

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

//            res.onFailure {
//
//                Log.i(TAG, "failure_res=")
//            }
            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.i(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    fun clearReportData(){
        _sendReportRes.value = null
    }


}