//package com.orwa.gatherin.present.common.group
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.os.bundleOf
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.Observer
//import com.orwa.gatherin.R
//import com.orwa.gatherin.present.teacher.MyActivityViewModel
//import com.orwa.gatherin.utils.*
//
//class StudentGroupDetailsFragment : GroupDetailsFragment() {
//
//    private val activityViewModel: MyActivityViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        super.onCreateView(inflater, container, savedInstanceState)
//
//        binding.teacherContent.hideGone()
//        binding.rlCode.hide()
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
//
//        binding.studentChat.setOnClickListener {
//            val bundle = bundleOf(Constants.GROUP_NAME_KEY to activityViewModel.groupName.value,
//            Constants.GROUP_MEMBERS_COUNT_KEY to activityViewModel.membersCount.value)
//            navigate(R.id.action_studentGroupDetailsFragment_to_groupContactFragment,bundle) }
//    }
//
//
//}