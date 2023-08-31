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

package com.orwa.gatherin.present.teacher.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentHome2Binding
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * Shows a register form to showcase UI state persistence. It has a button that goes to [Registered]
 */
class TeacherHomeFragment : BaseTeacherFragment() {

    private val TAG = TeacherHomeFragment::class.java.simpleName

    lateinit var binding: FragmentHome2Binding

    private val viewModel: HomeTeacherFragmentViewModel by viewModels()

    val requestPackValidity = { activityViewModel.checkPackageValidity() }

    val requestGetDepartments = { viewModel.getDepartmentsList() }

    val requestGetProfile = { activityViewModel.getUserInfo() }

    private var departmentsListAdapter: DepartmentsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setFragmentResultListener(CREATE_SECTION_REQUEST_KEY) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getBoolean(BUNDLE_KEY, false)
            if (result) {
                //reflect with adapter
                requestGetDepartments.invoke()
            }
            // Do something with the result
        }

        setFragmentResultListener(UPDATE_SECTION_REQUEST_KEY) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getBoolean(BUNDLE_KEY, false)
            if (result) {
                //reflect with adapter
                requestGetDepartments.invoke()
            }
            // Do something with the result
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHome2Binding.inflate(inflater, container, false)

        binding.addSectionFab.setOnClickListener {
//            val action = TeacherHomeFragmentDirections.actionHomeScreenToCreateSectionFragment("param")
            navigate(R.id.action_home_screen_to_createSectionFragment)
        }

        setupDepartmentsRV()

        setScrollListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shimmerLayout =
            binding.root.findViewById<ShimmerFrameLayout>(R.id.userInfoContainerShimmer)

        binding.upgradeRl.setOnClickListener {

//            Util.showMsgDialog(requireContext(), R.string.upgrade_purchase_process_not_ready, DialogInterface.OnClickListener { dialogInterface, i ->
//
//            })
            navigate(R.id.action_home_screen_to_subscriptionsListFragment2)
        }
        binding.rlGroupResults.setOnClickListener {
            //
            if (checkPackValidity()) {
                navigate(R.id.action_home_screen_to_groupResultsFragment)
            }
        }
        binding.rlSendNotifications.setOnClickListener {
            if (checkPackValidity()) {
                navigate(R.id.action_home_screen_to_notifyReportsStep1Fragment)
            }

        }
        binding.addSectionFab.setOnClickListener() {
            if (checkPackValidity()) {
                navigate(R.id.action_home_screen_to_createSectionFragment)
            }
        }

//        activityViewModel.packStatusRes.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                if (it.isValid) {
//                    departmentsListAdapter?.swapActivateActions(true)
//                } else {
//                    Util.showOptionMsgDialog(requireContext(), R.string.package_invalid) {
//                        navigate(R.id.action_home_screen_to_subscriptionsListFragment2)
//                    }
//                }
//            } else {
//                requestPackValidity.invoke()
//            }
//        })

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

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvHomeTeacher.showShimmerAdapter()
                binding.departmentRVEmpty.hide()
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
                binding.rvHomeTeacher.hideShimmerAdapter()

            }
        })

        viewModel.departmentsListRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "departments=$it")
            if (it != null) {
                departmentsListAdapter?.swapData(it)
                if (viewModel.nestedScrollY != -1) {
                    binding.nestedScroll.post(Runnable {
                        binding.nestedScroll.fling(0)
                        binding.nestedScroll.smoothScrollTo(0, viewModel.nestedScrollY)
                    })



                    binding.rvHomeTeacher.scrollToPosition(viewModel.rvPosition)
                }
            } else {
                requestGetDepartments.invoke()
            }
        })

        viewModel.deleteDepartmentRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toastSuccess(R.string.department_deleted_successfully)
                viewModel.clearResult()
            }
        })

        //UserInfo is shared with other fragments(settings, profile...) so that it is in ActivityViewModel
        activityViewModel.userInfo.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.userInfoContainer.show()
                shimmerLayout.hide()

                Util.loadImageWithPlaceholder(requireContext(), binding.ivProfileImage, it.picture)
                binding.tvUserName.setText(it.fullName)
                it.numberOfDepartment.let { maxDepartments ->
                    binding.tvMaxDepartments.setText(
                        getFormattedMaxDepartments(maxDepartments)
                    )
                }



                Pref.updateUserInfo(requireContext(), it.fullName, it.picture)

            } else {
                requestGetProfile.invoke()
                binding.userInfoContainer.hide()
                shimmerLayout.show()
            }
        })


    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)!!.visibility = View.VISIBLE
        Util.syncFirebaseToken(requireContext())
    }

    fun setScrollListener() {
        binding.nestedScroll.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY - oldScrollY < 0) {
                    //scroll up
                    binding.addSectionFab.show()
                } else {
                    //scroll down
                    binding.addSectionFab.hide()
                }
            }
        })
    }

    private fun setupDepartmentsRV() {
        // Set the adapter
        if (departmentsListAdapter == null) {
            departmentsListAdapter = DepartmentsRecyclerViewAdapter(
                listOf(),
                departmentOnClickListener,
                requireContext(),
                binding.departmentRVEmpty
            )
        }
        val view = binding.rvHomeTeacher
        val rvLayoutMgr = LinearLayoutManager(context)
        with(view) {
            layoutManager = rvLayoutMgr

            adapter = departmentsListAdapter
        }
        //set recycler view scroll listener to hide/show fab button
        var firstVisibleInListview = rvLayoutMgr.findFirstVisibleItemPosition()
//        binding.rvHomeTeacher.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val currentFirstVisible: Int = rvLayoutMgr.findFirstVisibleItemPosition()
//
//                if (currentFirstVisible > firstVisibleInListview) {
//                    //scroll up
//                    binding.addSectionFab.show()
//                } else {
//                    //scroll down
//                    binding.addSectionFab.show()
//                }
//
//
//                firstVisibleInListview = currentFirstVisible
//            }
//        })

    }

    private val departmentOnClickListener = object : DepartmentClickListener {

        override fun onClickListener(sect: DepartmentsListResItem) {

            if (checkPackValidity()) {
                activityViewModel.setCurrentSection(sect)
                navigate(R.id.action_home_screen_to_sectionDetailsFragment)
            }

        }

        override fun onDeleteListener(id: Int) {
            if (checkPackValidity()) {

                val deleteSection = { viewModel.deleteDepartment(id) }
                Util.showOptionMsgDialog(
                    requireContext(),
                    R.string.delete_section_confirm_dialog_msg,
                    deleteSection
                )
            }
        }

        override fun onUpdateClickListener(id: Int, name: String, code: String) {
            if (checkPackValidity()) {
                val bundle =
                    bundleOf(SECT_ID_KEY to id, SECT_NAME_KEY to name, SECT_CODE_KEY to code)
                navigate(R.id.action_home_screen_to_editSectionFragment, bundle)
//            viewModel.updateDepartment(id,name,code)
            }
        }
    }

    fun getFormattedMaxDepartments(maxValue: Int): String {
        return getString(R.string.max_departments_value, maxValue)
    }

    private fun checkPackValidity(): Boolean {
//        if (activityViewModel.packStatusRes.value?.isValid == true) {
//            return true
//        }
//        return false
        return true
    }


    override fun onDestroyView() {
//        Log.i(TAG,"onDestroyView")
        super.onDestroyView()
//        viewModel.rvPosition = (binding.rvHomeTeacher.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//
//        viewModel.nestedScrollY = binding.rvHomeTeacher.getChildAt(viewModel.rvPosition).getY().toInt()
        viewModel.nestedScrollY = binding.nestedScroll.scrollY

    }


    companion object {
        const val CREATE_SECTION_REQUEST_KEY = "create_section_request"
        const val UPDATE_SECTION_REQUEST_KEY = "update_section_request"

        const val BUNDLE_KEY = "bundle_key"

        const val SECT_ID_KEY = "sect_id"
        const val SECT_NAME_KEY = "sect_name"
        const val SECT_CODE_KEY = "sect_code"

    }
}
