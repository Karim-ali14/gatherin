package com.orwa.gatherin.present.common.section

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
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.model.group.*
import com.orwa.gatherin.model.notify.Notification
import com.orwa.gatherin.model.section.DeleteSectionMemberReq
import com.orwa.gatherin.present.common.section.member.SectMemberRep

import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class SectMembersViewModel @Inject constructor(
    private val rep: HomeRep, private val rep2: SectMemberRep
) : BaseNetworkViewModel() {

    private val TAG = SectMembersViewModel::class.java.simpleName

    private val _deleteMembersRes = MutableLiveData<ResponseBody?>(null)
    val deleteMembersRes: LiveData<ResponseBody?> = _deleteMembersRes

    var currentMemberIdToDelete:Int?=null

    /**
     * Used to store members id that will be added to the currently added group
     */
//    val membersIdsList = ArrayList<Int>()

    fun getSectionMembers(departmentId:Int, query:String): Flow<PagingData<SectMemberModel>> {
        val newResult: Flow<PagingData<SectMemberModel>> = rep2.getMembersFlowDb(departmentId, query)
            .cachedIn(viewModelScope)

        return newResult
    }

//    fun getMembersList(sectId:Int) {
//        setNetworkState(AuthNetworkState.LOADING)
//        viewModelScope.launch {
//            val res = rep.getDepartmentMembers2(sectId)
//            Log.i(TAG, "res=$res")
//            res.onSuccess {
//                setNetworkState(AuthNetworkState.SUCCESS)
//                _sectMembersRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
//            }
//
//            res.onError {
//                Log.e(TAG, "error_res=$statusCode")
//
//                setNetworkState(AuthNetworkState.FAILURE)
//
//            }
//            res.onException {
//                setNetworkState(AuthNetworkState.CONNECT_ERROR)
//
//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
//            }
//        }
//    }

    fun deleteMember(req:DeleteSectionMemberReq) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.deleteSectionMember(req)
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _deleteMembersRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                setNetworkState(AuthNetworkState.FAILURE)

            }
            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    fun deleteMemberFromDB(){
        viewModelScope.launch {
            currentMemberIdToDelete?.let { rep2.deleteMemberFromDB(it) }
        }
    }

    fun deleteAllMemberFromDB(){
        viewModelScope.launch {
             rep2.deleteAllMemberFromDB()
        }
    }


//    private var currentSearchResult: Flow<PagingData<GroupMembersResItem>>? = null
//
//    fun searchRepo(departmentId: Int): Flow<PagingData<GroupMembersResItem>> {
//
//        val newResult: Flow<PagingData<GroupMembersResItem>> = rep2.getSearchResultStream(departmentId)
//            .cachedIn(viewModelScope)
//        currentSearchResult = newResult
//        return newResult
//    }



}