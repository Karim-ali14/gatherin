package com.orwa.gatherin.present.teacher.home.group_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentDeptsListBinding
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem


class ChooseDepartmentFragment : BaseTeacherFragment() {

    lateinit var binding: FragmentDeptsListBinding

    private val viewModel: ChooseDepartmentFragmentViewModel by viewModels()

    //Get groups list
    private val requestGetDepartments = {  viewModel.getDepartmentsList() }

    private lateinit var departmentsListAdapter: DepartmentsRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(SET_GROUP_RESULT_KEY) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getBoolean(SET_GROUP_RESULT_BUNDLE_KEY, false)
            if (result) {
                if(viewModel.currentSelectedDepartmentPos!=-1){
                    viewModel.departmentsListRes.value?.let {
                        it[viewModel.currentSelectedDepartmentPos].wasDone=true

                    }
                }
            }
            // Do something with the result
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentDeptsListBinding.inflate(inflater, container, false)

        setupDepartmentsRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.select_section_title))

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvDepts.showShimmerAdapter()
//                binding.rvAnswers.hide()
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
                binding.rvDepts.hideShimmerAdapter()

            }
        })

        viewModel.departmentsListRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                departmentsListAdapter.swapData(it)
            } else {
                requestGetDepartments.invoke()
            }
        })
    }

    private fun setupDepartmentsRV() {
        // Set the adapter
        val view = binding.rvDepts
        with(view) {
            layoutManager = LinearLayoutManager(context)
            departmentsListAdapter = DepartmentsRecyclerViewAdapter(
                listOf(),
                departmentOnClickListener,
                requireContext(),
                binding.rvEmptyView
            )
            adapter = departmentsListAdapter
        }
    }

    private val departmentOnClickListener = object : DepartmentSelectListener {

        override fun onDepartmentSelected(item: DepartmentsListResItem, adapterPos:Int) {
            if(item.groups.size==0){
                toastSmall(R.string.set_results_no_group_error)
            }else{
                viewModel.currentSelectedDepartmentPos = adapterPos
                activityViewModel.setCurrentSection(item)
                navigate(R.id.action_chooseDepartmentFragment_to_setDepartmentResultsFragment)
            }

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.setCurrentSection(null)
    }

    companion object{
        const val SET_GROUP_RESULT_KEY="SET_GROUP_RESULT"
        const val SET_GROUP_RESULT_BUNDLE_KEY = "SET_GROUP_RESULT_BUNDLE"
    }
}