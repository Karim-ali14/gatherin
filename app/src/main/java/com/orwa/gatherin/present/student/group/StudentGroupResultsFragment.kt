package com.orwa.gatherin.present.student.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseStudentFragment
import com.orwa.gatherin.databinding.StudentGroupsResultsFragmentBinding
import com.orwa.gatherin.utils.hide


class StudentGroupResultsFragment : BaseStudentFragment() {

    private val TAG=StudentGroupResultsFragment::class.java.simpleName

    lateinit var binding:StudentGroupsResultsFragmentBinding

    private val viewModel: StudentGroupResultsViewModel by viewModels()

    //Get groups list
    private val requestGetGroups = { activityViewModel.currentSect.value?.let { viewModel.getDepartmentResults(it.id) } }

    private lateinit var parentAdapter: StudentGroupsResultsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StudentGroupsResultsFragmentBinding.inflate(inflater,container,false)

        setupGroupsResultsRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.results))

        //observer for loading groups results
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


        viewModel.resultsRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                parentAdapter.swapData(it)
            } else {
                requestGetGroups.invoke()
            }
        })

    }

    private fun setupGroupsResultsRV() {
        // Set the adapter
        val view = binding.rvSectionGroups
        with(view) {
            layoutManager = LinearLayoutManager(context)
            parentAdapter = StudentGroupsResultsRecyclerViewAdapter(
                listOf(),
                requireContext(),
                binding.rvEmptyView
            )
            adapter = parentAdapter
        }
    }

}