package com.orwa.gatherin.present.teacher.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.auth.UpdateProfileRes
import com.orwa.gatherin.rep.ProfileRep
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val rep: ProfileRep) :
    BaseNetworkViewModel() {

    private val TAG = EditProfileViewModel::class.java.simpleName

    private val _updateProfileRes = MutableLiveData<UpdateProfileRes?>()
    val updateProfileRes: LiveData<UpdateProfileRes?> = _updateProfileRes

    //save image path picked from file picker
    var profileImg: Uri? = null

    /**
     * @param isImg determine if the upload process for image or for file.
     * PDF file will be uploaded first
     */
    fun updateProfile(file: MultipartBody.Part, fullName: RequestBody, phone:RequestBody) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.updateProfile(file, fullName, phone)
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _updateProfileRes.value = data

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


    fun updateProfile( fullName: String, phone: String) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.updateProfile(fullName, phone)
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _updateProfileRes.value = data

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


//
//    fun clearResponses() {
//        _uploadFileRes.value = null
//        _uploadImgRes.value = null
//    }
}