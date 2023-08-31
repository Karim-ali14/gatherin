package com.orwa.gatherin.present.teacher.group

interface MemberChooseListener {
    fun onMemberChosen(userId:Int)
    fun onMemberRemoved(userId:Int)
}