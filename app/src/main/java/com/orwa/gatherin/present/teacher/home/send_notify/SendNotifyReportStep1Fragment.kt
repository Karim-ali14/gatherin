package com.orwa.gatherin.present.teacher.home.send_notify

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentNotifyReportsStep1Binding
import com.orwa.gatherin.model.notify.SendNotifyReq
import com.orwa.gatherin.present.teacher.home.send_quest.GroupItemRecyclerViewAdapter
import java.util.ArrayList

class SendNotifyReportStep1Fragment : BaseTeacherFragment() {

    private val TAG = SendNotifyReportStep1Fragment::class.java.simpleName

    private val viewModel : SendNotifyReportFragmentViewModel by viewModels()

    private lateinit var binding:FragmentNotifyReportsStep1Binding

    //Get all groups list related to this teacher user
    private val requestGetGroups = {  viewModel.getGroupsList()  }

    private lateinit var groupsListAdapter: GroupItemRecyclerViewAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentNotifyReportsStep1Binding.inflate(inflater,container,false)

        setupGroupsRV()

        binding.ivSend.setOnClickListener {
            val body = binding.etNote.text.trim().toString()
            if(TextUtils.isEmpty(body)){
                binding.etNote.setError(getString(R.string.validate_empty_field))
            }else{
                sendNotifyReport(body)
            }
        }

        binding.cbAllGroups.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                groupsListAdapter.checkAllGroups()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.notificationAndReport))

        //observer for loading groups inside section
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvGroupsNames.showShimmerAdapter()
//                binding.rvEmptyView.hide()
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
                binding.rvGroupsNames.hideShimmerAdapter()

            }
        })


        viewModel.sendNotifyNetworkState.observe(viewLifecycleOwner, Observer {
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

        viewModel.groupsListRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG,"groups_list=$it")
            if (it != null) {
                groupsListAdapter.swapData(it)
            } else {
                requestGetGroups.invoke()
            }
        })

        viewModel.sendNotifyRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSendNotifySuccessDialog()
            }
        })
    }


    private fun setupGroupsRV() {
        // Set the adapter
        val view = binding.rvGroupsNames
        with(view) {
            layoutManager = LinearLayoutManager(context)
            groupsListAdapter = GroupItemRecyclerViewAdapter(
                listOf(),
                requireContext(),
                binding.rvEmptyView,
                true,{
                    binding.cbAllGroups.isChecked = false
                }
            )
            adapter = groupsListAdapter
        }
    }

    fun getSelectedGroupsList() :List<Int>{
        val l = ArrayList<Int>()

//        if (binding.cbAllGroups.isChecked) {
//            viewModel.groupsListRes.value?.let {
//                it.forEach { it1->
//                    l.add(it1.id)
//                }
//            }
//        }

//        else{
            l.addAll(groupsListAdapter.getSelectedGroupsIds())
//        }
        return l.toList()
    }

    fun sendNotifyReport(body:String){
        val groupsIds = getSelectedGroupsList()
//        Log.i(TAG,"groupsIds=$groupsIds")
        if(groupsIds.isEmpty()){
            toastFailure(R.string.send_question_no_selected_group)
        }else{
            val notifyReport =  SendNotifyReq(body,groupsIds)

            hideKeyboardFrom(binding.root)
            viewModel.sendNotifyReport(notifyReport)
        }

    }

    private fun showSendNotifySuccessDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = View.inflate(context, R.layout.send_question_success_dialog, null)
        val text = dialogView.findViewById<TextView>(R.id.textView)
        text.text = getString(R.string.send_notify_success)
        val cancel = dialogView.findViewById<ImageButton>(R.id.fabOk)
        builder.setCancelable(false)


        builder.setView(dialogView)
        val dialog = builder.create()

        cancel.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }
        dialog.show()
    }
}