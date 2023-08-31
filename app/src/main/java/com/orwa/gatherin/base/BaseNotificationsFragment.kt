/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orwa.gatherin.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.FragmentNotificationsBinding
import com.orwa.gatherin.present.notify.NotifyViewModel
import com.orwa.gatherin.present.notify.adapter.LoaderStateAdapter
import com.orwa.gatherin.present.notify.adapter.NotifyAdapter
import com.orwa.gatherin.present.teacher.MyActivityViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Shows the main title screen with a button that navigates to [About].
 */
@ExperimentalPagingApi
abstract class BaseNotificationsFragment : BaseFragment() {

    private val TAG = BaseNotificationsFragment::class.java.simpleName

    lateinit var binding: FragmentNotificationsBinding

    private val viewModel: NotifyViewModel by viewModels()

    private val activityViewModel: MyActivityViewModel by activityViewModels()

//    val request = { activityViewModel.getNotificationsList() }


    //    private lateinit var notifyAdapter : NotificationsRecyclerViewAdapter
    private val adapter = NotifyAdapter()


    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)


//        setupNotificationsRV()

//         add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvNotification.addItemDecoration(decoration)
        binding.retryButton.setOnClickListener { adapter.retry() }


        initAdapter()
//        val query = "savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY"
        search()

        return binding.root
    }


    private fun initAdapter() {
        val loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
        binding.rvNotification.adapter = adapter.withLoadStateFooter(loaderStateAdapter)


        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.rlNoNotifications.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                binding.rvNotification.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                binding.pbNotify.isVisible = loadState.mediator?.refresh is LoadState.Loading
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



//        adapter.addLoadStateListener { loadState ->
//            // show empty list
//            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
//            showEmptyList(isListEmpty)
//
//            // Only show the list if refresh succeeds.
//            binding.rvNotification.isVisible = loadState.source.refresh is LoadState.NotLoading
//            // Show loading spinner during initial load or refresh.
//            if(loadState.source.refresh is LoadState.Loading){
//                binding.rvNotification.showShimmerAdapter()
//            }else{
//                binding.rvNotification.hideShimmerAdapter()
//            }
//            // Show the retry state if initial load or refresh fails.
//            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
//
//            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
//            val errorState = loadState.source.append as? LoadState.Error
//                ?: loadState.source.prepend as? LoadState.Error
//                ?: loadState.append as? LoadState.Error
//                ?: loadState.prepend as? LoadState.Error
//            errorState?.let {
//                Toast.makeText(
//                    requireContext(),
//                    "\uD83D\uDE28 Wooops ${it.error}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }

    }

/*
    private fun initSearch() {
        binding.searchRepo.setText(query)

        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

         Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvNotification.scrollToPosition(0) }
        }
    }
*/

//    private fun updateRepoListFromInput() {
//        binding.searchRepo.text.trim().let {
//            if (it.isNotEmpty()) {
//                search(it.toString())
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.notification))

        //Used for observing change notifications state request call
//        viewModel.networkState.observe(viewLifecycleOwner, Observer {
//            if (it == AuthNetworkState.LOADING) {
//                showProgressDialog()
//            } else {
//                when (it) {
//                    AuthNetworkState.SUCCESS -> {
//                        //Call this again to reflect notification icons in both screens (student & teacher)
////                        activityViewModel.getNotificationsList()
//                    }
//                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
//                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
//                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
//                    else -> {
//                    }
//                }
//                hideProgressDialog()
//
//            }
//        })

//        activityViewModel.notifyNetworkState.observe(viewLifecycleOwner, Observer {
//            if (it == AuthNetworkState.LOADING) {
//                binding.rvNotification.showShimmerAdapter()
//                binding.rlNoNotifications.hide()
//            } else {
//                when (it) {
//                    AuthNetworkState.SUCCESS -> {
//                    }
//                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
//                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
//                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
//                    else -> {
//                    }
//                }
//                binding.rvNotification.hideShimmerAdapter()
//
//            }
//        })

        activityViewModel.notificationsRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "departments=$it")
            if (it != null) {

                if(it.countUnSeen>0){
                    viewModel.setNotificationsStateToRead()
                }

            } else {
                activityViewModel.getNotificationsList()
            }
        })
    }

//    override fun onStart() {
//        super.onStart()
//        activity?.let {
//            if(it is TeacherHomeActivity){
//                it.setNotificationIconStatus(true)
//            }else if(it is StudentActivity){
//
//            }
//        }
//    }

    private fun search(query: String? = null) {
//         Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo().distinctUntilChanged().collectLatest {
//                viewModel.setNetworkState(AuthNetworkState.NONE)
//                Log.i(TAG,"submit_data=$it")
                adapter.submitData(it)
            }
        }



//         Make sure we cancel the previous job before creating a new one
//        searchJob?.cancel()
//        lifecycleScope.launch {
//            combine(shouldScrollToTop, pagingData, ::Pair)
//                // Each unique PagingData should be submitted once, take the latest from
//                // shouldScrollToTop
//                .distinctUntilChangedBy { it.second }
//                .collectLatest { (shouldScroll, pagingData) ->
//                    adapter.submitData(pagingData)
//                    // Scroll only after the data has been submitted to the adapter,
//                    // and is a fresh search
//                    if (shouldScroll) list.scrollToPosition(0)
//                }
//        }
    }

//    private fun showEmptyList(show: Boolean) {
//        if (show) {
//            binding.rlNoNotifications.visibility = View.VISIBLE
//            binding.rvNotification.visibility = View.GONE
//        } else {
//            binding.rlNoNotifications.visibility = View.GONE
//            binding.rvNotification.visibility = View.VISIBLE
//        }
//    }

/*    private fun setupNotificationsRV() {
        // Set the adapter
        val view = binding.rvNotification
        with(view) {
            layoutManager = LinearLayoutManager(context)
            notifyAdapter = NotificationsRecyclerViewAdapter(
                listOf(),
                clickListener,
                requireContext(),
                binding.rlNoNotifications
            )
            adapter = notifyAdapter
        }
    }

    val clickListener = object:NotificationClickListener{
        override fun onNotificationClickListener(id: Int) {
        }
    }*/
}
