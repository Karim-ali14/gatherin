package com.orwa.gatherin.base

import androidx.fragment.app.activityViewModels
import com.orwa.gatherin.present.teacher.MyActivityViewModel
import com.orwa.gatherin.present.teacher.home.TeacherHomeFragment
import com.orwa.gatherin.present.teacher.messages.MessagesListFragment
import com.orwa.gatherin.present.teacher.profile.TeacherProfileFragment

abstract class BaseTeacherFragment : BaseFragment() {

    val activityViewModel: MyActivityViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        when (this) {
            is TeacherHomeFragment, is MessagesListFragment, is TeacherProfileFragment ->
                activityViewModel.setBottomNavStatus(true)
            else -> activityViewModel.setBottomNavStatus(false)
        }
    }
}