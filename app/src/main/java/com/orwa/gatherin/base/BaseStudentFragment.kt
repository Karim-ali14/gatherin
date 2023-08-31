package com.orwa.gatherin.base

import androidx.fragment.app.activityViewModels
import com.orwa.gatherin.present.teacher.MyActivityViewModel

abstract class BaseStudentFragment : BaseFragment() {

     val activityViewModel: MyActivityViewModel by activityViewModels()

}