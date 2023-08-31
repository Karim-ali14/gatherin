package com.orwa.gatherin.present.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartActivityViewModel: ViewModel() {

    private val _phone = MutableLiveData<String?>(null)
    val phone:LiveData<String?> = _phone

//    //Used for sharing link
//    var sectionCode:String?=null

    fun setPhone(phone:String?){
        _phone.value = phone
    }
}