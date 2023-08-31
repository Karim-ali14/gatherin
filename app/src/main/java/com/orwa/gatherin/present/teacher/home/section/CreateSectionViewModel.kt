package com.orwa.gatherin.present.teacher.home.section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.model.section.CreateSectionReq
import com.orwa.gatherin.model.section.CreateSectionRes
import com.orwa.gatherin.rep.HomeRep
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.section.CreateSectionReq2
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSectionViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = CreateSectionViewModel::class.java.simpleName

    private val _createSectionRes = MutableLiveData<CreateSectionRes?>()
    val createSectionRes: LiveData<CreateSectionRes?> = _createSectionRes

    private val _autoGenerateCode = MutableLiveData<Boolean>(false)
    val autoGenerateCode: LiveData<Boolean> = _autoGenerateCode


    fun createSection(req: CreateSectionReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.createSection(req)
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _createSectionRes.value = data

//                Log.i(TAG, "success_res=$data")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Forbidden ->{
                        setNetworkState(AuthNetworkState.FORBIDDEN)
                    }
                    StatusCode.Conflict -> {
                        setNetworkState(AuthNetworkState.SECTION_ALREADY_REGISTERED)
                    }
                    StatusCode.UnProcessableEntity -> setNetworkState(AuthNetworkState.MALFORMED_INPUT_DATA)
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

    fun createSection(req: CreateSectionReq2) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.createSection2(req)
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _createSectionRes.value = data

//                Log.i(TAG, "success_res=$data")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Forbidden ->{
                        setNetworkState(AuthNetworkState.FORBIDDEN)
                    }
                    StatusCode.Conflict -> {
                        setNetworkState(AuthNetworkState.SECTION_ALREADY_REGISTERED)
                    }
                    StatusCode.UnProcessableEntity -> setNetworkState(AuthNetworkState.MALFORMED_INPUT_DATA)
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


    fun setAutoValue(isAuto:Boolean){
        _autoGenerateCode.value = isAuto
    }

}