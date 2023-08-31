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

package com.orwa.gatherin.present.teacher.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.FragmentProfileBinding
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.utils.Util
import com.facebook.shimmer.ShimmerFrameLayout
import com.orwa.gatherin.model.teacher_home.PacakgeInfo
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.FREE_PACK
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_ANNUAL
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_ANNUAL1
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_MONTHLY
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_MONTHLY1
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_ANNUAL
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_ANNUAL1
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_MONTHLY
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_MONTHLY1
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * Shows a profile screen for a user, taking the name from the arguments.
 */
class TeacherProfileFragment : BaseTeacherFragment() {

    lateinit var binding:FragmentProfileBinding

    val getUserInfoRequest = {activityViewModel.getUserInfo()}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentProfileBinding.inflate(inflater,container,false)

        binding.ivSettings.setOnClickListener { navigate(R.id.action_profile_to_settings_graph) }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shimmerLayout = binding.root.findViewById<ShimmerFrameLayout>(R.id.userInfoContainerShimmer)

        activityViewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                shimmerLayout.startShimmer()
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
                shimmerLayout.stopShimmer()

            }
        })

        //UserInfo is shared with other fragments(settings, profile...) so that it is in ActivityViewModel
        activityViewModel.userInfo.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                binding.userInfoContainer.show()
                shimmerLayout.hide()

                Util.loadImageWithPlaceholder(requireContext(),binding.ivProfileImage,it.picture)
                binding.tvUserName.setText(it.fullName)
                binding.tvPhoneNumber.text= it.phone
                binding.tvEmail.text = it.email
                binding.tvPackType.text = getFormattedPackName(it.pacakgeInfo)
                Glide.with(binding.root).load(it.pacakgeInfo?.image).placeholder(R.drawable.logo).error(R.drawable.ic_error_2).into(binding.ivPack)

            }else{
                getUserInfoRequest.invoke()
                binding.userInfoContainer.hide()
                shimmerLayout.show()
            }
        })

    }

    private fun getFormattedPackName(packInfo:PacakgeInfo?):String{
//        Log.i("ProfileFragment","pack_id=${packInfo?.packAndroidID}")
        val name = when (packInfo?.packAndroidID){
            FREE_PACK -> R.string.free_pack
            SILVER_PACK_MONTHLY1 -> R.string.silver_pack_monthly
            SILVER_PACK_ANNUAL1-> R.string.silver_pack_annual
            GOLDEN_PACK_MONTHLY1->R.string.golden_pack_monthly
            GOLDEN_PACK_ANNUAL1->R.string.golden_pack_annual
            else -> R.string.pack_unknown
        }
        return getString(name)
    }
}
