package com.orwa.gatherin.present.teacher.home.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.FragmentSectionDetailsBinding
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.model.section.GroupListResItem
import com.orwa.gatherin.present.common.section.DeptMembersFragment
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide

@ExperimentalPagingApi
class TeacherSectionDetailsFragment : BaseTeacherFragment() {

    lateinit var binding: FragmentSectionDetailsBinding

    private val viewModel: SectionDetailsFragmentViewModel by viewModels()

    //Get groups list
    private val requestGetGroups = { activityViewModel.currentSect.value?.let { viewModel.getGroupsList(it.id) } }

    private lateinit var groupsListAdapter: GroupsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(CREATE_GROUP_REQUEST_KEY) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getBoolean(GROUP_BUNDLE_KEY, false)
            if (result) {
                //reflect with adapter
                requestGetGroups.invoke()
            }
            // Do something with the result
        }
    }

//    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSectionDetailsBinding.inflate(inflater, container, false)

        setupGroupsRV()

        binding.rlCopy.setOnClickListener {
            Util.copyToClipBoard(requireContext(),binding.tvLink.text.toString())
            toastSmall(R.string.copied_to_clipboard)
        }

        binding.btnMembers.setOnClickListener {
            activityViewModel.currentSect.value?.let {
                val bundle = bundleOf(DeptMembersFragment.DEPT_ID_KEY to it.id)
                navigate(R.id.action_sectionDetailsFragment_to_deptMembersFragment,bundle)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addGroupFab.setOnClickListener { navigate(R.id.action_sectionDetailsFragment_to_createGroupFragment) }


        //observer for loading groups inside section
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvGroups.showShimmerAdapter()
                binding.rvEmptyView.hide()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                binding.rvGroups.hideShimmerAdapter()

            }
        })


        activityViewModel.currentSect.observe(viewLifecycleOwner, Observer {
            it?.let {
                val header1 = binding.root.findViewById<TextView>(R.id.header_title)
                val header2 = binding.root.findViewById<TextView>(R.id.header_title_details)
                header1.setText(it.name)
                header2.setText(Util.getGroupsNumberLabel(it.groups.size, requireContext()))
                binding.tvCode.setText(it.code)
                binding.tvLink.setText(it.url)
            }
        })

        viewModel.groupsListRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                groupsListAdapter.swapData(it)
            } else {
                requestGetGroups.invoke()
            }
        })

        viewModel.deleteGroupRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toastSuccess(R.string.group_deleted_successfully)
                viewModel.clearResult()
            }
        })
    }

    private fun setupGroupsRV() {
        // Set the adapter
        val view = binding.rvGroups
        with(view) {
            layoutManager = LinearLayoutManager(context)
            groupsListAdapter = GroupsRecyclerViewAdapter(
                listOf(),
                groupOnClickListener,
                requireContext(),
                binding.rvEmptyView
            )
            adapter = groupsListAdapter
        }
    }

    private val groupOnClickListener = object : GroupClickListener {

        override fun onClickListener(item: GroupListResItem) {
            activityViewModel.setCurrentGroup(item)
            navigate(R.id.action_sectionDetailsFragment_to_groupDetailsFragment)
        }

        override fun onDeleteListener(id: Int) {
            val deleteSection = { viewModel.deleteGroup(id) }
            Util.showOptionMsgDialog(
                requireContext(),
                R.string.delete_group_confirm_dialog_msg,
                deleteSection
            )
        }

        override fun onUpdateClickListener(item:GroupListResItem) {
            activityViewModel.setCurrentGroup(item)
            val bundle = bundleOf(IS_EDIT_GROUP_KEY to true)
            navigate(R.id.action_sectionDetailsFragment_to_createGroupFragment,bundle)
        }
    }

    companion object{
        const val CREATE_GROUP_REQUEST_KEY="create_group_request"
        const val GROUP_BUNDLE_KEY = "group_bundle"

        //Used to know if the CreateEditGroupFragment is for edition of not
        const val IS_EDIT_GROUP_KEY="is_edit_group"
        const val GROUP_ID_KEY="group_id"
        const val GROUP_NAME_KEY="group_name"
    }

}