//package com.orwa.gatherin.present.teacher.group
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.Observer
//import com.orwa.gatherin.present.common.group.GroupDetailsFragment
//import com.orwa.gatherin.utils.*
//
//class TeacherGroupDetailsFragment : GroupDetailsFragment() {
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        super.onCreateView(inflater, container, savedInstanceState)
//
//        binding.studentChat.hideGone()
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        activityViewModel.groupName.observe(viewLifecycleOwner, Observer {
//            it?.let { setupHeaderTitle(it) }
//        })
//
//        activityViewModel.membersCount.observe(viewLifecycleOwner, Observer {
//            it?.let { setupHeaderDetailsTitle(it.toString()) }
//        })
//    }
//
//
//}