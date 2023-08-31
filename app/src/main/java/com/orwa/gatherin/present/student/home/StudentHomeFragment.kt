package com.orwa.gatherin.present.student.home

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.MyApplication
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseStudentFragment
import com.orwa.gatherin.databinding.FragmentStudentHomeBinding
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem
import com.orwa.gatherin.present.teacher.home.DepartmentClickListener
import com.orwa.gatherin.present.teacher.home.HomeTeacherFragmentViewModel
import com.orwa.gatherin.utils.*
import com.facebook.shimmer.ShimmerFrameLayout
import com.orwa.gatherin.present.student.chats.MastersChatListFragment


class StudentHomeFragment : BaseStudentFragment() {

    private val TAG = StudentHomeFragment::class.java.simpleName

    private val viewModel: HomeTeacherFragmentViewModel by viewModels()

    lateinit var binding: FragmentStudentHomeBinding

    val requestGetDepartments = { viewModel.getDepartmentsList() }

    val getUserInfoRequest = { activityViewModel.getUserInfo() }

    private lateinit var departmentsListAdapter: SectionItemRecyclerViewAdapter

    private lateinit var joinSectionDialog: MyAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        joinSectionDialog = MyAlertDialog()
        joinSectionDialog.create()

        activity?.intent?.let {
            val isFromNotification = it.getBooleanExtra(Constants.IS_FROM_NOTIFICATION_KEY, false)
            if (isFromNotification && activityViewModel.isFirstTime) {
                navigate(R.id.action_student_home_to_notification)
                activityViewModel.isFirstTime = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false)

        binding.ivSettings.setOnClickListener { navigate(R.id.action_student_home_to_settings_graph) }
        binding.ivNotification.setOnClickListener { navigate(R.id.action_student_home_to_notification) }

        binding.rlJoinToSection.setOnClickListener {
            joinSectionDialog.show()
        }
        binding.ivJoinCategory.setOnClickListener {
            joinSectionDialog.show()
        }

        binding.ivChat.setOnClickListener {
            val bundle = bundleOf(MastersChatListFragment.IS_FROM_SETTINGS_KEY to false)
            navigate(R.id.action_student_home_to_mastersChatListFragment2, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shimmerLayout =
            binding.root.findViewById<ShimmerFrameLayout>(R.id.userInfoContainerShimmer)

        initSectionsRV()


        //observing for user info(name+picture)
        activityViewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                shimmerLayout.startShimmer()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    else -> {
                    }
                }
                shimmerLayout.stopShimmer()

            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvSections.showShimmerAdapter()
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
                binding.rvSections.hideShimmerAdapter()

            }
        })

        //UserInfo is shared with other fragments(settings, profile...) so that it is in ActivityViewModel
        activityViewModel.userInfo.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.userInfoContainer.show()
                shimmerLayout.hide()

                Util.loadImage(requireContext(), binding.ivProfileImage, it.picture)
                binding.tvUserName.setText(it.fullName)

                Pref.updateUserInfo(requireContext(), it.fullName, it.picture)


            } else {
                getUserInfoRequest.invoke()
                binding.userInfoContainer.hide()
                shimmerLayout.show()
            }
        })

        viewModel.joinDepartmentNetworkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                joinSectionDialog.showLoadingState()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.join_section_success)
                    }
                    AuthNetworkState.DEPARTMENTS_FULL_ERROR -> toastFailure(R.string.join_department_error_full)
                    AuthNetworkState.DEPARTMENT_ADD_USER_ERROR -> toastFailure(R.string.joind_department_error_code)
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                joinSectionDialog.hideLoadingState()

            }
        })

        observeDepartments.invoke()


        viewModel.addUserToDepartmentRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                joinSectionDialog.dismiss()
                viewModel.clearResult()
            }
        })


        activityViewModel.notificationsRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "departments=$it")
            if (it != null) {
                if (it.countUnSeen > 0) { //Some notifications were not read yet
                    binding.ivNotification.setImageResource(R.drawable.notify_layers_white)
                } else {
                    binding.ivNotification.setImageResource(R.drawable.ic_ab_notification)

                }
            } else {
                activityViewModel.getNotificationsList()
            }
        })


    }

    override fun onStart() {
        super.onStart()
        Util.syncFirebaseToken(requireContext())
    }

    val observeDepartments = {
        viewModel.departmentsListRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "departments=$it")
            if (it != null) {
                if (it.size == 0) {
                    binding.rlJoinToSection.show()
                    binding.rlSections.hide()
                } else {
                    binding.rlSections.show()
                    binding.rlJoinToSection.hide()
                }
                departmentsListAdapter.swapData(it)

                //Checking the share link
                processShareLink()

            } else {
                requestGetDepartments.invoke()
            }
        })
    }

    fun processShareLink() {
//        Log.i(TAG, "share_link")

        if (MyApplication.SHARE_LINK != null) {
            if (TextUtils.isEmpty(MyApplication.SHARE_LINK)) {
                toastFailure(R.string.share_link_malformed)
            } else {
                joinSectionDialog.tvCode.setText(MyApplication.SHARE_LINK)
                joinSectionDialog.show()
                MyApplication.SHARE_LINK = null

            }
        }

    }

    fun initSectionsRV() {
        val rv = binding.rvSections
        binding.rlSections.show()
        binding.rlJoinToSection.hide()

        with(rv) {
            layoutManager = LinearLayoutManager(context)
            departmentsListAdapter = SectionItemRecyclerViewAdapter(
                listOf(),
                departmentClickListener,
                requireContext()
            )
            adapter = departmentsListAdapter
        }
    }

    val departmentClickListener = object : DepartmentClickListener {
        override fun onClickListener(sect: DepartmentsListResItem) {
            activityViewModel.setCurrentSection(sect)
            navigate(R.id.action_student_home_to_sectionFragment)
        }

        override fun onDeleteListener(id: Int) {
            TODO("Not yet implemented")
        }

        override fun onUpdateClickListener(id: Int, name: String, code: String) {
            TODO("Not yet implemented")
        }

    }

    inner class MyAlertDialog : AlertDialog(requireContext()) {

        lateinit var tvCode: EditText
        lateinit var btnConfirm: Button
        lateinit var progress: ProgressBar
        lateinit var ivClose: ImageButton

        override fun onCreate(savedInstanceState: Bundle?) {

            val dialogView = View.inflate(context, R.layout.popup_join_section, null)
            setView(dialogView)


//            val lp = WindowManager.LayoutParams()
//            lp.copyFrom(getWindow()?.getAttributes())
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//            window?.attributes = lp

            tvCode = dialogView.findViewById(R.id.etCode)
            btnConfirm = dialogView.findViewById(R.id.joinConfirm)
            progress = dialogView.findViewById(R.id.progress)
            ivClose = dialogView.findViewById(R.id.ivClose)

            btnConfirm.setOnClickListener {
                if (tvCode.text.isNullOrEmpty()) {
                    tvCode.error = getString(R.string.validate_empty_field)
                } else if (tvCode.text.toString().trim().length < 6) {
                    tvCode.error = getString(R.string.validate_input_field_length_6_exact)
                } else {
                    val codeValue = tvCode.text.toString().trim()
                    viewModel.addUserToDepartment(codeValue)

                }
            }
            ivClose.setOnClickListener {
                tvCode.text.clear()
                dismiss()
            }

            super.onCreate(savedInstanceState)


        }

        fun showLoadingState() {
            setCancelable(false)
            btnConfirm.hide()
            progress.show()
            tvCode.isEnabled = false
            ivClose.isEnabled = false
        }

        fun hideLoadingState() {
            setCancelable(true)
            btnConfirm.show()
            progress.hide()
            tvCode.isEnabled = true
            ivClose.isEnabled = true
        }

        override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
            super.setOnDismissListener(listener)
            tvCode.setText("")
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearOtherResult()
    }
}