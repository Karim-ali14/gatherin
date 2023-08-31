package com.orwa.gatherin.present.common.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseFragment
import com.orwa.gatherin.databinding.GroupDetailsFragmentBinding
import com.orwa.gatherin.present.teacher.MyActivityViewModel
import com.orwa.gatherin.present.teacher.group.ChooseGroupLeaderFragment
import com.orwa.gatherin.present.teacher.group.MemberChooseListener
import com.orwa.gatherin.utils.*

class GroupDetailsFragment : BaseFragment() {

    lateinit var binding: GroupDetailsFragmentBinding

    private val viewModel: GroupDetailsViewModel by viewModels()

    val activityViewModel: MyActivityViewModel by activityViewModels()

    private lateinit var membersAdapter: GroupMembersRecyclerViewAdapter

    val requestGroupDetails = {
        activityViewModel.currentGroup.value?.let {
            viewModel.getGroupDetails(
                it.id
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(SELECT_GROUP_LEADER_REQUEST_KEY) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getBoolean(SELECT_GROUP_LEADER_BUNDLE_KEY, false)
            if (result) {
                //reflect with adapter
                requestGroupDetails.invoke()
            }
            // Do something with the result
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GroupDetailsFragmentBinding.inflate(inflater, container, false)

        val user = getUser()
        user?.let {
            if (it.type == Constants.USER_TYPE_TEACHER) {
                binding.studentChat.hideGone()
                binding.tvMasterName.hideGone()

            } else {
                binding.teacherContent.hideGone()
                binding.rlCode.hideGone()
                binding.vMasterName.show()
            }
        }

        binding.rlChooseLeader.setOnClickListener {
            // TODO: 6/8/2021  add args
            val bundle = bundleOf(ChooseGroupLeaderFragment.COMING_FROM_CREATE_PAGE_KEY to false)
            navigate(R.id.action_groupDetailsFragment_to_chooseGroupLeaderFragment,bundle)
        }

        setupMembersRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.currentGroup.observe(viewLifecycleOwner, Observer {
            it?.let {
                val header1 = binding.root.findViewById<TextView>(R.id.header_title)
                val header2 = binding.root.findViewById<TextView>(R.id.header_title_details)
                header1.setText(it.name)
                header2.setText(Util.getMembersNumberLabel(it.membersCount, requireContext()))

                it.master?.let { master ->
                    binding.tvMasterName.text = master.fullName
                    Util.loadImage(requireContext(), binding.ivMaster, master.picture)
                }

            }
        })

        binding.studentChat.setOnClickListener {
            if (activityViewModel.groupMembers == null) {
                toastFailure(R.string.cant_start_chat_without_members)
            } else {
                navigate(R.id.action_groupDetailsFragment2_to_groupContactFragment)

            }

//             val members = viewModel.groupDetailsRes.value?.members
//             val bundle = bundleOf(GROUP_MEMBERS_KEY to members)
//             if(members!=null){
//             }else{
//                 toastFailure(R.string.cant_start_chat_without_members)
//             }
        }

        binding.teacherChat.setOnClickListener {

            if (activityViewModel.groupMembers == null) {
                toastFailure(R.string.cant_start_chat_without_members)
            } else {
                navigate(R.id.action_groupDetailsFragment_to_groupContactFragment2)

            }



//             val members = viewModel.groupDetailsRes.value?.members
//             val bundle = bundleOf(GROUP_MEMBERS_KEY to members)
//             if(members!=null){
//             }else{
//                 toastFailure(R.string.cant_start_chat_without_members)
//             }
        }


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
                membersAdapter.swapData(it.members)
                activityViewModel.groupMembers = it.members
                //Required to set "leader" label at the end of the leader user
                it.leader?.let { it1 -> membersAdapter.setLeaderUser(it1.id) }

                activityViewModel.setGroupLeader(it.leader)//Used to fetch later in groupContactFragment to check leader user
                //Bring users previously registered with this group
            } else {
                requestGroupDetails.invoke()
            }
        })

    }

    private fun setupMembersRV() {
        // Set the adapter
        val view = binding.rvMembers
        with(view) {
            layoutManager = LinearLayoutManager(context)
            membersAdapter = GroupMembersRecyclerViewAdapter(
                listOf(),
                membersSelectListener,
                requireContext(),
                binding.rvEmptyView
            )
            adapter = membersAdapter
        }
    }

    val membersSelectListener = object : MemberChooseListener {
        override fun onMemberChosen(userId: Int) {
//            navigate(R.id.action_groupDetailsFragment_to_chooseGroupLeaderFragment)
        }

        override fun onMemberRemoved(userId: Int) {
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.groupMembers = null
        activityViewModel.setGroupLeader(null)
    }

    companion object {
        const val SELECT_GROUP_LEADER_REQUEST_KEY = "select_group_leader_request"
        const val SELECT_GROUP_LEADER_BUNDLE_KEY = "select_group_leader_bundle"

//        const val GROUP_MEMBERS_KEY = "group_members"
    }

}