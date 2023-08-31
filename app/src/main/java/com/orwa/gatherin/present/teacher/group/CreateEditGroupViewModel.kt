package com.orwa.gatherin.present.teacher.group

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.orwa.gatherin.rep.HomeRep
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.model.group.*
import com.orwa.gatherin.present.common.section.member.SectMemberRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class CreateEditGroupViewModel
@Inject constructor(
    private val rep: HomeRep , private val rep2: SectMemberRep
) : BaseNetworkViewModel() {

    private val TAG = CreateEditGroupViewModel::class.java.simpleName

    private val _membersNetworkState = MutableLiveData<AuthNetworkState>(AuthNetworkState.NONE)
    val membersNetworkState: LiveData<AuthNetworkState> = _membersNetworkState

    private val _createGroupRes = MutableLiveData<CreateGroupRes?>()
    val createGroupRes: LiveData<CreateGroupRes?> = _createGroupRes

    private val _updateGroupRes = MutableLiveData<ResponseBody?>()
    val updateGroupRes: LiveData<ResponseBody?> = _updateGroupRes

    private val _sectMembersRes = MutableLiveData<SectMembersRes?>(null)
    val sectMembersRes: LiveData<SectMembersRes?> = _sectMembersRes

    //Used when editing the group info
    private val _groupDetailsRes =  MutableLiveData<GroupDetailsRes?>(null)
    val groupDetailsRes: LiveData<GroupDetailsRes?> = _groupDetailsRes

    /**
     * Used to store members id that will be added to the currently added group
     */
    val membersIdsList = ArrayList<Int>()

    fun createGroup(req: CreateGroupReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.createGroup(req)
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _createGroupRes.value = data

//                Log.i(TAG, "success_res=$data")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                     StatusCode.Conflict,StatusCode.UnProcessableEntity -> setNetworkState(AuthNetworkState.GROUP_ALREADY_REGISTERED)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }


            }

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    fun updateGroup(req: UpdateGroupReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.updateGroup(req)
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _updateGroupRes.value = data

//                Log.i(TAG, "success_res=$data")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Conflict, StatusCode.NotAcceptable, StatusCode.UnProcessableEntity -> setNetworkState(AuthNetworkState.GROUP_MEMBERS_FULL)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }

            }

            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

//    fun getMembersList(sectId:Int) {
//        _membersNetworkState.value = AuthNetworkState.LOADING
//        viewModelScope.launch {
//            val res = rep.getDepartmentMembers(sectId)
////            _signInRes.value = res
////            Log.i(TAG, "res=$res")
//            res.onSuccess {
//                _membersNetworkState.value = AuthNetworkState.SUCCESS
//                _sectMembersRes.value = data
////                Log.i(TAG, "success_res=$data, status_code=$statusCode")
//            }
//
//            res.onError {
////                Log.e(TAG, "error_res=$statusCode")
//
//                _membersNetworkState.value = AuthNetworkState.FAILURE
//
//            }
//            res.onException {
//                _membersNetworkState.value = AuthNetworkState.CONNECT_ERROR
//
////                Log.e(TAG, "EXCEPTION_res=$message")
//            }
//        }
//    }

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

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    fun getSectionMembers(departmentId:Int, members:List<Member>?): Flow<PagingData<SectMemberModel>> {
        val newResult: Flow<PagingData<SectMemberModel>> = rep2.getMembersFlowDb(departmentId=departmentId,members= members)
            .cachedIn(viewModelScope)

        return newResult
    }

    fun setRegisteredMember(userId:Int, isRegistered:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            rep2.setRegisteredMemberIntoDB(userId, isRegistered)
        }
    }

    fun deleteAllMemberFromDB(){
        viewModelScope.launch {
            rep2.deleteAllMemberFromDB()
        }
    }


}