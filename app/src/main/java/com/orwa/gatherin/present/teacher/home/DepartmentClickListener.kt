package com.orwa.gatherin.present.teacher.home

import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem

interface DepartmentClickListener {
    fun onClickListener(sect: DepartmentsListResItem)
    fun onDeleteListener(id:Int)
    fun onUpdateClickListener(id:Int, name:String, code:String)
}