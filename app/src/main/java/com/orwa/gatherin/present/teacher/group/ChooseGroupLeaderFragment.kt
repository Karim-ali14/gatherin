package com.orwa.gatherin.present.teacher.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentChooseGroupLeaderBinding
import com.orwa.gatherin.present.common.group.GroupDetailsFragment
import com.orwa.gatherin.utils.Util

class ChooseGroupLeaderFragment : BaseTeacherFragment() {

    private val TAG = ChooseGroupLeaderFragment::class.java.simpleName

    private lateinit var binding: FragmentChooseGroupLeaderBinding

    private val viewModel: ChooseLeaderViewModel by viewModels()

    private lateinit var membersAdapter: GroupSelectMembersRecyclerViewAdapter

    val requestGroupDetails = {
        activityViewModel.currentGroup.value?.let {
            viewModel.getGroupDetails(
                it.id
            )
        }
    }

    val requestSelectLeader = {
        activityViewModel.currentGroup.value?.let {
            viewModel.leaderId?.let { it1 ->
                viewModel.setGroupLeader(
                    it.id, it1
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChooseGroupLeaderBinding.inflate(inflater, container, false)

        setupMembersRV()

        binding.chooseLeaderConfirmBtn.setOnClickListener {
            if (viewModel.leaderId == null || viewModel.leaderId == -1) {
                toastFailure(R.string.no_goup_leader_choosen)
            } else {
                requestSelectLeader.invoke()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Used to populate group info (headers)
        activityViewModel.currentGroup.observe(viewLifecycleOwner, Observer {
            it?.let {
                val header1 = binding.root.findViewById<TextView>(R.id.header_title)
                val header2 = binding.root.findViewById<TextView>(R.id.header_title_details)
                header1.setText(it.name)
                header2.setText(Util.getMembersNumberLabel(it.membersCount, requireContext()))
            }
        })

        viewModel.membersNetworkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvMembers.showShimmerAdapter()
            } else {
                binding.rvMembers.hideShimmerAdapter()
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                    }
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
            }
        })

        viewModel.groupDetailsRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                it.master?.let { it2 -> viewModel.leaderId = it2.id }
                membersAdapter.swapData(it.members)
                //Bring users previously registered with this group
            }else{
                requestGroupDetails.invoke()
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                hideProgressDialog()
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.select_group_leader_success)
                    }
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
            }
        })

        viewModel.setGroupLeaderRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG,"setGroupLeaderRes=$it")
            it?.let {
                setFragmentResult(
                    GroupDetailsFragment.SELECT_GROUP_LEADER_REQUEST_KEY, bundleOf(
                        GroupDetailsFragment.SELECT_GROUP_LEADER_BUNDLE_KEY to true
                    )
                )
                val isFromCreatePage = arguments?.getBoolean(COMING_FROM_CREATE_PAGE_KEY,false)
                if(isFromCreatePage!=null && isFromCreatePage){
                    findNavController().popBackStack(R.id.sectionDetailsFragment,false)

                }else{
                    findNavController().popBackStack(R.id.groupDetailsFragment,false)

                }

            }
        })
    }

    private fun setupMembersRV() {
        // Set the adapter
        val view = binding.rvMembers
        with(view) {
            layoutManager = LinearLayoutManager(context)
            membersAdapter = GroupSelectMembersRecyclerViewAdapter(
                listOf(),
                membersSelectListener,
                requireContext(),
                binding.membersRVEmpty
            )
            adapter = membersAdapter
        }
    }

    val membersSelectListener = object : MemberChooseListener {
        override fun onMemberChosen(userId: Int) {
            if(userId!=-1){
                viewModel.leaderId = userId
            }else{
                viewModel.leaderId=null
            }
//            Log.i(TAG,"leader_id=$userId")
        }

        override fun onMemberRemoved(userId: Int) {
            //nothing to do
        }

    }

    companion object{
        const val COMING_FROM_CREATE_PAGE_KEY="is_new_group"
    }

}