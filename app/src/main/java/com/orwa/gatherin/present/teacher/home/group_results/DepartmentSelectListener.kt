package com.orwa.gatherin.present.teacher.home.group_results

import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem

interface DepartmentSelectListener {
    fun onDepartmentSelected(item:DepartmentsListResItem, adapterPos:Int)
}