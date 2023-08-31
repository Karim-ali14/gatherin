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

package com.orwa.gatherin.present.student.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentMessagesBinding
import com.orwa.gatherin.model.chat.ChatUsersListResItem
import com.orwa.gatherin.present.common.contact.ContactFragment
import com.orwa.gatherin.utils.Constants
import com.orwa.gatherin.utils.hide

class MastersChatListFragment : BaseTeacherFragment() {

    lateinit var binding: FragmentMessagesBinding

    private val viewModel: MasterChatListViewModel by viewModels()

    private lateinit var usersAdapter: MasterChatRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentMessagesBinding.inflate(inflater, container, false)

        setupChatUsersRV()

        //used only for students
        binding.etSearchMember.hide()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.contact))

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvUsers.showShimmerAdapter()
            } else {
                binding.rvUsers.hideShimmerAdapter()
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

        viewModel.chatUsersRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                usersAdapter.swapData(it)
            } else {
                viewModel.getChatUsers()
            }
        })
    }

    private val userClickListener = { chatUser: ChatUsersListResItem ->

        val bundle = bundleOf(
            ContactFragment.IS_PRIVATE_CHAT_KEY to true,
            ContactFragment.OTHER_USER_ID_KEY to chatUser.id,
            ContactFragment.OTHER_USER_NAME_KEY to chatUser.fullName
        )

        if (true) {
            arguments?.let {
                val isFromSettings = it.getBoolean(IS_FROM_SETTINGS_KEY, false)
                if (isFromSettings) {
                    navigate(R.id.action_mastersChatListFragment_to_contactFragment, bundle)
                } else {
                    navigate(R.id.action_mastersChatListFragment2_to_groupContactFragment, bundle)

                }
            }
        }


    }

    private fun setupChatUsersRV() {
        // Set the adapter
        val view = binding.rvUsers
        with(view) {
            layoutManager = LinearLayoutManager(context)
            usersAdapter = MasterChatRecyclerViewAdapter(
                listOf(),
                userClickListener,
                requireContext(),
                binding.rvEmptyView
            )
            adapter = usersAdapter
        }
    }

    companion object {
        /**
         * Used to check if the current screen is coming from settings screen or main student home screen
         */
        const val IS_FROM_SETTINGS_KEY = "is_from_settings"
    }

}
