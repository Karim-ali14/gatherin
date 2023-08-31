package com.orwa.gatherin.present.common.section

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.FragmentCreateEditGroupBinding
import com.orwa.gatherin.present.teacher.home.section.TeacherSectionDetailsFragment
import com.orwa.gatherin.base.BaseFragment
import com.orwa.gatherin.databinding.FragmentDeptMembersBinding
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.model.group.GroupMembersResItem
import com.orwa.gatherin.model.section.DeleteSectionMemberReq
import com.orwa.gatherin.model.section.Member
import com.orwa.gatherin.present.common.section.member.SectMembersAdapter
import com.orwa.gatherin.present.common.section.ui.*
import com.orwa.gatherin.present.notify.adapter.LoaderStateAdapter
import com.orwa.gatherin.present.notify.adapter.NotifyAdapter
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.show
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class DeptMembersFragment : BaseFragment() {

    private val TAG = DeptMembersFragment::class.java.simpleName

    private lateinit var binding: FragmentDeptMembersBinding

    private val viewModel: SectMembersViewModel by viewModels()

    private lateinit var adapter :  SectMembersAdapter

    private var searchJob: Job? = null


    private val deleteMemberCallback = { item: SectMemberModel ->
        val deptID = arguments?.getInt(DEPT_ID_KEY, -1) ?: -1
        val req = DeleteSectionMemberReq(listOf(item.id),deptID)
        viewModel.currentMemberIdToDelete = item.id
        viewModel.deleteMember(req)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDeptMembersBinding.inflate(inflater, container, false)



        initAdapter()

        getMembers("")

        binding.etSearchMember.setOnEditorActionListener { tv, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.etSearchMember.setOnKeyListener { tv, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

        binding.etSearchMember.addTextChangedListener {
            it1->
            if(it1.toString().trim().isEmpty()){
                getMembers("")
            }
        }

        return binding.root
    }

    fun updateRepoListFromInput(){
        binding.etSearchMember.text.trim().let {
            if (it.isNotEmpty()) {
                binding.rvMembers.scrollToPosition(0)
                getMembers(it.toString())
//                adapter.submitList(null)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.delete_section_member_success)
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.deleteMembersRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.deleteMemberFromDB()
            }
            viewModel.currentMemberIdToDelete = null
        })
    }

    private fun getMembers(query:String) {
//         Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            val deptID = arguments?.getInt(DEPT_ID_KEY, -1) ?: -1

            viewModel.getSectionMembers(deptID, query).distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }

    }

    private fun initAdapter() {

        adapter = SectMembersAdapter(deleteMemberCallback)

        val loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
        binding.rvMembers.adapter = adapter.withLoadStateFooter(loaderStateAdapter)


        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
//                binding.rvMembers.isVisible = isListEmpty
                if(binding.etSearchMember.text.toString().isEmpty()){
                    binding.membersRVEmpty.setText(R.string.members_rv_empty)

                }else{
                    binding.membersRVEmpty.setText(R.string.search_member_no_matches)

                }
                binding.membersRVEmpty.isVisible = isListEmpty

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

    override fun onDestroyView() {
        super.onDestroyView()
        clearUsersData()
    }

    companion object {
        const val DEPT_ID_KEY = "dept_id"
//        const val DEPT_MASTER_ID_KEY="dept_master_id"
    }
}