package com.orwa.gatherin.present.teacher.home.group_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentGroupResultsBinding
import com.orwa.gatherin.model.group.AllGroupsResItem

class GroupsResultsFragment : BaseTeacherFragment() {

    lateinit var binding: FragmentGroupResultsBinding

    private val viewModel: GroupResultsFragmentViewModel by viewModels()

    //Get groups list
    private val requestGetGroups = { viewModel.getGroupsResultsList() }

    private lateinit var groupsListAdapter: GroupsResultsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGroupResultsBinding.inflate(inflater, container, false)

        binding.rrAskQuestions.setOnClickListener {
            navigate(R.id.action_groupResultsFragment_to_groupSendQuestionsFragment)
        }

        binding.rrUpdateResultsList.setOnClickListener {
            navigate(R.id.action_groupResultsFragment_to_chooseDepartmentFragment)
        }

        setupGroupsResultsRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.groupResults))

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvAnswers.showShimmerAdapter()
//                binding.rvAnswers.hide()
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
                binding.rvAnswers.hideShimmerAdapter()

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

    private fun setupGroupsResultsRV() {
        // Set the adapter
        val view = binding.rvAnswers
        with(view) {
            layoutManager = LinearLayoutManager(context)
            groupsListAdapter = GroupsResultsRecyclerViewAdapter(
                listOf(),
                groupOnClickListener,
                requireContext(),
                binding.rvEmptyView
            )
            adapter = groupsListAdapter
        }
    }

    private val groupOnClickListener = object : GroupResultClickListener {

        override fun onClickListener(item: AllGroupsResItem) {
            if(item.isAnswer){
                val bundle = bundleOf(
                    GROUP_ID_KEY to item.id,
                    GROUP_NAME_KEY to item.name,
                    DEPARTMENT_NAME_KEY to item.departmentName
                )
                navigate(R.id.action_groupResultsFragment_to_groupResultsDetailsFragment, bundle)
            }else{
                toastFailure(R.string.no_leader_answer)
            }

        }

    }

    companion object {
        const val GROUP_ID_KEY = "group_id"
        const val GROUP_NAME_KEY = "group_name"
        const val DEPARTMENT_NAME_KEY = "department_name"
    }
}