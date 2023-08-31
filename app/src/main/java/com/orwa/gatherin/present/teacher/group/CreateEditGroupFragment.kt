package com.orwa.gatherin.present.teacher.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateTeacherFragment
import com.orwa.gatherin.databinding.FragmentCreateEditGroupBinding
import com.orwa.gatherin.model.group.CreateGroupReq
import com.orwa.gatherin.model.group.UpdateGroupReq
import com.orwa.gatherin.model.section.GroupListResItem
import com.orwa.gatherin.present.teacher.group.ChooseGroupLeaderFragment.Companion.COMING_FROM_CREATE_PAGE_KEY
import com.orwa.gatherin.present.teacher.home.section.TeacherSectionDetailsFragment
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.orwa.gatherin.model.group.GroupMembersResItem
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.present.common.section.DeptMembersFragment
import com.orwa.gatherin.present.common.section.member.SectMembersAdapterForGroup
import com.orwa.gatherin.present.notify.adapter.LoaderStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class CreateEditGroupFragment : BaseValidateTeacherFragment() {

    private val TAG = CreateEditGroupFragment::class.java.simpleName

    private lateinit var binding: FragmentCreateEditGroupBinding

    private val viewModel: CreateEditGroupViewModel by viewModels()

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var nameET: EditText

//    private lateinit var membersAdapter: SectMembersRecyclerViewAdapter

    private lateinit var adapter : SectMembersAdapterForGroup

    private var searchJob: Job? = null

    private var isEdit = false
//    private var groupId:Int?=null
    private var groupName:String?=null

    //Used
//    val requestSectMembers = {
//        activityViewModel.currentSect.value?.let {
//            viewModel.getMembersList(
//                it.id
//            )
//        }
//    }

    val requestGetGroupDetails={
        activityViewModel.currentGroup.value?.let {
//            Log.i(TAG,"REQUEST_ id = ${it.id}")
            viewModel.getGroupDetails(it.id)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEdit = it.getBoolean(TeacherSectionDetailsFragment.IS_EDIT_GROUP_KEY, false)
//            Log.i(TAG,"isEdit=$isEdit")
            groupName = it.getString(TeacherSectionDetailsFragment.GROUP_NAME_KEY)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateEditGroupBinding.inflate(inflater, container, false)

        nameET = binding.etGroupName
        if(isEdit){
            activityViewModel.currentGroup.value?.let {
                nameET.setText(it.name)
                binding.tvCreateEditGroupTitle.setText(R.string.edit_group_title)
            }

        }

        binding.confirmBtn.setOnClickListener { validator.validate() }

//        setupMembersRV()

        initAdapter()




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                hideProgressDialog()
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        if(isEdit){
                            toastSuccess(R.string.update_group_success)

                        }else{
                            toastSuccess(R.string.create_group_success)
                        }
                    }
                    AuthNetworkState.GROUP_ALREADY_REGISTERED -> {
                        toastFailure(R.string.create_group_already_exists)
                    }
                    AuthNetworkState.GROUP_MEMBERS_FULL -> {
                        toastFailure(R.string.add_group_members_full_error)
                    }
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
            }
        })

        viewModel.membersNetworkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                hideProgressDialog()
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

//        viewModel.sectMembersRes.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                membersAdapter.swapData(it.members)
//                //Bring users previously registered with this group
//                if(isEdit){
//                    requestGetGroupDetails.invoke()
//                }
//            } else {
//                requestSectMembers.invoke()
//            }
//        })

        //Used to get the previously registered users in the current group if the current screen is opened for editing
        if(isEdit){
            viewModel.groupDetailsRes.observe(viewLifecycleOwner, Observer {
//                Log.i(TAG,"REQUEST_ group_details_res=$it")
                if(it==null) {
                    requestGetGroupDetails.invoke()
//                reflectCheckBokWithRegisteredUsers(it.members)
                }else{
                    getMembers(it.members)
                }
            })
        }else{
            getMembers(null)
        }



        viewModel.createGroupRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                setResultStatusToTrue()
                val createdGroup = GroupListResItem(it.id, 0, it.name,join = false,null)
                activityViewModel.setCurrentGroup(createdGroup)
                val bundle = bundleOf(COMING_FROM_CREATE_PAGE_KEY to true)
                navigate(R.id.action_createGroupFragment_to_chooseGroupLeaderFragment,bundle)
            }

        })

        viewModel.updateGroupRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                setResultStatusToTrue()
                findNavController().popBackStack()
            }
        })

    }



    /**
     * Used to reflect changes in previous fragment when create or update processes are done
     */
    private fun setResultStatusToTrue(){
        val result = true
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResult(
            TeacherSectionDetailsFragment.CREATE_GROUP_REQUEST_KEY, bundleOf(
                TeacherSectionDetailsFragment.GROUP_BUNDLE_KEY to result
            )
        )
    }

//    private fun setupMembersRV() {
//        // Set the adapter
//        val view = binding.rvMembers
//        with(view) {
//            layoutManager = LinearLayoutManager(context)
//            membersAdapter = SectMembersRecyclerViewAdapter(
//                listOf(),
//                membersSelectListener,
//                requireContext(),
//                binding.membersRVEmpty
//            )
//            adapter = membersAdapter
//        }
//    }

    val membersSelectListener = object : MemberChooseListener {
        override fun onMemberChosen(userId: Int) {
            if(!viewModel.membersIdsList.contains(userId)){
                viewModel.membersIdsList.add(userId)
                viewModel.setRegisteredMember(userId,true)
            }
//            Log.i(TAG,"MEMBERS=${viewModel.membersIdsList.toString()}")

        }

        override fun onMemberRemoved(userId: Int) {
            if(viewModel.membersIdsList.contains(userId)) {
                viewModel.membersIdsList.remove(userId)
                viewModel.setRegisteredMember(userId,false)

            }
//            Log.i(TAG,"MEMBERS=${viewModel.membersIdsList.toString()}")

//            Log.i(TAG, "WAS_REMOVED=$wasRemoved")
        }

    }

    private fun getMembers(members:List<Member>?) {
//         Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
//            val deptID = arguments?.getInt(DeptMembersFragment.DEPT_ID_KEY, -1) ?: -1
            val deptId = activityViewModel.currentSect.value?.id
//            Log.i(TAG,"DEPT_ID=$deptId")
            deptId?.let {
                viewModel.getSectionMembers(deptId, members).distinctUntilChanged().collectLatest {
                    adapter.submitData(it)
                }
            }

        }

    }

    private fun initAdapter() {

        adapter = SectMembersAdapterForGroup(membersSelectListener, requireContext())

        val loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
        binding.rvMembers.adapter = adapter.withLoadStateFooter(loaderStateAdapter)


        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.rvMembers.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                binding.rvMembers.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error && adapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    toastSmall(it.error.toString())
                }
            }
        }

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvMembers.addItemDecoration(decoration)

    }




    fun clearUsersData(){
        viewModel.deleteAllMemberFromDB()
    }

    override fun onDestroy() {
        clearUsersData()
        super.onDestroy()
    }


    override fun onValidationSucceeded() {
        hideKeyboardFrom(binding.root)
        val sectId = activityViewModel.currentSect.value?.id
        sectId?.let {

            if(isEdit){
//                Log.i(TAG,"is_edit=$isEdit")
                val groupId = activityViewModel.currentGroup.value?.id
                groupId?.let {
//                    Log.i(TAG,"UPDATE_GROUP_ID=$groupId")
                    val updateGroupReq = UpdateGroupReq(groupId,binding.etGroupName.text.toString().trim(),viewModel.membersIdsList.toList())
                    viewModel.updateGroup(updateGroupReq)
                }

            }else{
                val createGroupReq = CreateGroupReq(
                    it, binding.etGroupName.text.toString().trim(),
                    viewModel.membersIdsList.toList())
                viewModel.createGroup(createGroupReq)
            }

        }
    }
}