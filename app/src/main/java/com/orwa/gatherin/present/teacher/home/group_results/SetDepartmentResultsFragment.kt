package com.orwa.gatherin.present.teacher.home.group_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentFeaturesListBinding
import com.orwa.gatherin.model.group_results.*
import com.orwa.gatherin.model.teacher_home.Group
import com.orwa.gatherin.present.teacher.home.group_results.ChooseDepartmentFragment.Companion.SET_GROUP_RESULT_BUNDLE_KEY
import com.orwa.gatherin.present.teacher.home.group_results.ChooseDepartmentFragment.Companion.SET_GROUP_RESULT_KEY


class SetDepartmentResultsFragment : BaseTeacherFragment() {

    lateinit var binding: FragmentFeaturesListBinding

    private val viewModel: SetDepartmentResultsViewModel by viewModels()

    private lateinit var parentAdapter: TeacherGroupsResultsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFeaturesListBinding.inflate(inflater, container, false)

        setupParentRV()

        binding.btnConfirm.setOnClickListener {
            activityViewModel.currentSect.value?.let { sect ->
                val isEntryValid = parentAdapter.validateEntryData()
                if (!isEntryValid) {
                    toastFailure(R.string.validation_failed)
                } else {
                    hideKeyboardFrom(binding.root)
                    val req = SendResultsReq(getData(),sect.id)
                    viewModel.sendResults(req)
                }
            }

        }

        binding.btnAddColumn.setOnClickListener {
            activityViewModel.currentSect.value?.let {
                val feature3 = getSingleFeatureData(
                    "",
                    it.groups,
                    TeacherGroupsResultsRecyclerViewAdapter.ItemType.FEATURE,
                    false
                )
                parentAdapter.addItem(feature3)
                binding.rvFeatures.postDelayed(Runnable {
                    binding.rvFeatures.smoothScrollToPosition(parentAdapter.itemCount - 1)
                }, 100)

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.currentSect.observe(viewLifecycleOwner, Observer {
            setupHeaderTitle(it?.name)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
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
                hideProgressDialog()

            }
        })

        viewModel.setResultsRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                setFragmentResult(
                    SET_GROUP_RESULT_KEY, bundleOf(
                        SET_GROUP_RESULT_BUNDLE_KEY to true)
                )
                findNavController().popBackStack()
            }
        })

//        viewModel.departmentsListRes.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                parentAdapter.swapData(it)
//            } else {
//                requestGetDepartments.invoke()
//            }
//        })
    }

    private fun setupParentRV() {
        // Set the adapter
        val view = binding.rvFeatures
        val data = getParentData()
        data?.let {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                parentAdapter = TeacherGroupsResultsRecyclerViewAdapter(
                    it,
                    requireContext(),
                    binding.rvEmptyView
                )
                adapter = parentAdapter
            }
        }

    }

    private fun getParentData(): ArrayList<Feature>? {
        val l = ArrayList<Feature>()

        activityViewModel.currentSect.value?.let {
            val feature1 = getSingleFeatureData(
                getString(R.string.feature_name_collaboration),
                it.groups,
                TeacherGroupsResultsRecyclerViewAdapter.ItemType.FEATURE,
                true
            )
            val feature2 = getSingleFeatureData(
                getString(R.string.feature_name_delivery_on_time),
                it.groups,
                TeacherGroupsResultsRecyclerViewAdapter.ItemType.FEATURE,
                true
            )
            l.add(feature1)
            l.add(feature2)
            return l
        }
        return null
    }

    private fun getSingleFeatureData(
        name: String,
        groups: List<Group>,
        type: TeacherGroupsResultsRecyclerViewAdapter.ItemType,
        isFixed: Boolean
    ): Feature {
        val vals = ArrayList<Value>()
        for (g in groups) {
            vals.add(Value(g, null))
        }
        return Feature(name, vals, type, isFixed)
    }

    fun getData():List<Data>{
        val features = parentAdapter.getData()
        val dataList = ArrayList<Data>()
        for(f in features){
            val name = f.title
            f.values
            val data = Data(name,getGroupsList(f.values))
            dataList.add(data)
        }

        return dataList.toList()
    }

    fun getGroupsList(values:List<Value>):List<GroupX>{
        val groups = ArrayList<GroupX>()
        for(g in values){
            val groupX = GroupX(g.group.id,g.mark!!)
            groups.add(groupX)
        }
        return groups
    }

//    private val addColumnClickListener = object : AddColumnClickListener {
//
//        override fun onAddColumnClicked() {
//            activityViewModel.currentSect.value?.let {
//                val feature3 = getSingleFeatureData(
//                    getString(R.string.feature_name_delivery_on_time),
//                    it.groups,
//                    TeacherGroupsResultsRecyclerViewAdapter.ItemType.ADD_COLUMN,
//                    false
//                )
//                parentAdapter.addItem(feature3)
//                binding.rvFeatures.postDelayed(Runnable {
//                    binding.rvFeatures.smoothScrollToPosition(parentAdapter.itemCount-1)
//                },100)
//
//            }
//        }
//    }

    interface AddColumnClickListener {
        fun onAddColumnClicked()
    }

//    fun getDataListToSend():List<Data>{
//
//    }
}