package com.orwa.gatherin.present.teacher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseActivityViewModel
import com.orwa.gatherin.model.group.Leader
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.model.notify.NotificationsRes
import com.orwa.gatherin.model.pack.PackageRes
import com.orwa.gatherin.model.section.GroupListResItem
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem
import com.orwa.gatherin.model.teacher_home.UserInfoRes
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyActivityViewModel @Inject constructor(private val rep: HomeRep):BaseActivityViewModel() {

    private val TAG = MyActivityViewModel::class.java.simpleName

    private val _showBottomNav: MutableLiveData<Boolean> = MutableLiveData(true)
    val showBottomNav: LiveData<Boolean> = _showBottomNav

    //Used in SectionDetailsFragment and later
    private val _currentSect: MutableLiveData<DepartmentsListResItem?> = MutableLiveData(null)
    val currentSect: LiveData<DepartmentsListResItem?> = _currentSect

    private val _userInfo: MutableLiveData<UserInfoRes?> = MutableLiveData(null)
    val userInfo: LiveData<UserInfoRes?> = _userInfo

    //Used in SectionDetailsFragment and later
    private val _currentGroup: MutableLiveData<GroupListResItem?> = MutableLiveData(null)
    val currentGroup: LiveData<GroupListResItem?> = _currentGroup

    //Used in SectionDetailsFragment and later
    private val _groupLeader: MutableLiveData<Leader?> = MutableLiveData(null)
    val groupLeader: LiveData<Leader?> = _groupLeader


    private val _notifyNetworkState = MutableLiveData(AuthNetworkState.NONE)
    val notifyNetworkState: LiveData<AuthNetworkState> = _notifyNetworkState

    fun setNotifyNetworkState(state: AuthNetworkState){
        _notifyNetworkState.value = state
    }

    private val _notificationsRes = MutableLiveData<NotificationsRes?>(null)
    val notificationsRes: LiveData<NotificationsRes?> = _notificationsRes

    private val _packStatusRes = MutableLiveData<PackageRes?>(null)
    val packStatusRes: LiveData<PackageRes?> = _packStatusRes



    /**
     * Used to to not moving to the notification fragment again(for second time) after launching it when a FCM notification is received and clicked
     */
    var isFirstTime=true


    var groupMembers:List<Member>?=null

    //Used for sharing link
    var sectionCode:String?=null


    fun setBottomNavStatus(showStatus: Boolean) {
        _showBottomNav.value = showStatus
    }

    fun setCurrentSection(item: DepartmentsListResItem?) {
        _currentSect.value = item
    }

    fun setCurrentGroup(item: GroupListResItem) {
        _currentGroup.value = item
    }

    fun getUserInfo() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getUserInfo()
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _userInfo.value = data
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

    fun getNotificationsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getNotifications()
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNotifyNetworkState(AuthNetworkState.SUCCESS)
                _notificationsRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setNotifyNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setNotifyNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
                setNotifyNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
            }
        }
    }

    fun checkPackageValidity() {
//        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.checkPackage()
//            _signInRes.value = res
//            Log.i(TAG, "res=$res")
            res.onSuccess {
//                setNetworkState(AuthNetworkState.SUCCESS)
                _packStatusRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

//                when (statusCode) {
//                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
//                    else -> setNetworkState(AuthNetworkState.FAILURE)
//                }
            }
            res.onException {
//                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }



    fun setGroupLeader(l:Leader?){
        _groupLeader.value = l
    }

    /**
     * Used after updating profile. Necessary to reflect changes to different screens like home page
     */
    fun updateUserInfo(fullName:String, phone:String, picture:String?=null){
        _userInfo.value?.let {
            _userInfo.value!!.phone = phone
            _userInfo.value!!.fullName = fullName
            if(picture!=null){
                _userInfo.value!!.picture = picture
            }

        }
    }


}