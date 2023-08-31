package com.orwa.gatherin.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


abstract class BaseNetworkViewModel:ViewModel() {

    private val _networkState = MutableLiveData(AuthNetworkState.NONE)
    val networkState: LiveData<AuthNetworkState> = _networkState

    fun setNetworkState(state: AuthNetworkState){
        _networkState.value = state
    }
}