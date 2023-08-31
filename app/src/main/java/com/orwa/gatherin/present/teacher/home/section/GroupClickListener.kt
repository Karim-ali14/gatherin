package com.orwa.gatherin.present.teacher.home.section

import com.orwa.gatherin.model.section.GroupListResItem

interface GroupClickListener {
    fun onClickListener(item:GroupListResItem)
    fun onDeleteListener(id:Int)
    fun onUpdateClickListener(item:GroupListResItem)
}