package com.orwa.gatherin.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseActivityViewModel:BaseNetworkViewModel() {

    /**
     * Current section name
     */
    private val _sectionName: MutableLiveData<String?> = MutableLiveData(null)
    val sectionName: LiveData<String?> = _sectionName

    /**
     * Number of groups inside section
     */
    private val _groupsCount: MutableLiveData<Int?> = MutableLiveData(null)
    val groupsCount : LiveData<Int?> = _groupsCount

    /**
     * Current group name
     */
    private val _groupName: MutableLiveData<String?> = MutableLiveData(null)
    val groupName: LiveData<String?> = _groupName

    private val _membersCount: MutableLiveData<Int?> = MutableLiveData(null)
    val membersCount : LiveData<Int?> = _membersCount

    fun setSectionName(sectionName:String?){
        _sectionName.value = sectionName
    }

    fun setGroupName(name:String?){
        _groupName.value = name
    }

    fun setMembersCount(c:Int?){
        _membersCount.value = c
    }
}