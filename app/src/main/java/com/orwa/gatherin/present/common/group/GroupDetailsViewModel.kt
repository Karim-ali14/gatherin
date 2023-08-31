package com.orwa.gatherin.present.common.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.group.GroupDetailsRes
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = GroupDetailsViewModel::class.java.simpleName

    private val _membersNetworkState = MutableLiveData<AuthNetworkState>(AuthNetworkState.NONE)
    val membersNetworkState: LiveData<AuthNetworkState> = _membersNetworkState

    private val _groupDetailsRes =  MutableLiveData<GroupDetailsRes?>(null)
    val groupDetailsRes: LiveData<GroupDetailsRes?> = _groupDetailsRes

//    var leaderId:Int?=null


    fun getGroupDetails(groupId:Int) {
        _membersNetworkState.value = AuthNetworkState.LOADING
        viewModelScope.launch {
            val res = rep.getGroupDetails(groupId)
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                _membersNetworkState.value = AuthNetworkState.SUCCESS
                _groupDetailsRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                _membersNetworkState.value = AuthNetworkState.FAILURE

            }
            res.onException {
                _membersNetworkState.value = AuthNetworkState.CONNECT_ERROR

//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
            }
        }
    }


}