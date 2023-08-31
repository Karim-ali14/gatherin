package com.orwa.gatherin.present.student.section

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseStudentFragment
import com.orwa.gatherin.databinding.SectionFragmentBinding
import com.orwa.gatherin.model.section.GroupListResItem
import com.orwa.gatherin.present.teacher.home.section.SectionDetailsFragmentViewModel
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show

class StudentSectionDetailsFragment : BaseStudentFragment() {

    private val TAG=StudentSectionDetailsFragment::class.java.simpleName

    lateinit var binding:SectionFragmentBinding

    private val viewModel: SectionDetailsFragmentViewModel by viewModels()

    //Get groups list
    private val requestGetGroups = { activityViewModel.currentSect.value?.let { viewModel.getGroupsList(it.id) } }

    private lateinit var groupsListAdapter: GroupItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SectionFragmentBinding.inflate(inflater,container,false)

        val resultsBtn = binding.root.findViewById<AppCompatButton>(R.id.btnResults)
        resultsBtn.show()
        resultsBtn.setOnClickListener {
            navigate(R.id.action_studentSectionsFragment_to_studentGroupResultsFragment)
        }

        setupGroupsRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        activityViewModel.sectionName.observe(viewLifecycleOwner, Observer {
//            it?.let { setupHeaderTitle(it) }
//        })

        //observer for loading groups inside section
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvSectionGroups.showShimmerAdapter()
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
                binding.rvSectionGroups.hideShimmerAdapter()

            }
        })


        activityViewModel.currentSect.observe(viewLifecycleOwner, Observer {
            it?.let {
                val header1 = binding.root.findViewById<TextView>(R.id.header_title)
//                val header2 = binding.root.findViewById<TextView>(R.id.header_title_details)
                header1.setText(it.name)
//                header2.setText(Util.getGroupsNumberLabel(it.groups.size, requireContext()))
//                binding.tvCode.setText(it.code)
            }
        })

        viewModel.groupsListRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                groupsListAdapter.swapData(it)
            } else {
                requestGetGroups.invoke()
            }
        })

    }

    private fun setupGroupsRV() {
        // Set the adapter
        val view = binding.rvSectionGroups
        with(view) {
            layoutManager = LinearLayoutManager(context)
            groupsListAdapter = GroupItemRecyclerViewAdapter(
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
//            Log.i(TAG,"group_item=$item")
            activityViewModel.setCurrentGroup(item)
            navigate(R.id.action_sectionFragment_to_groupDetailsFragment2)
        }
    }
}