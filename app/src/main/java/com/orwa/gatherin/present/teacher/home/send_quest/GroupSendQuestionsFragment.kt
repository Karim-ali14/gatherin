package com.orwa.gatherin.present.teacher.home.send_quest

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentGroupSendQuestionsBinding
import com.orwa.gatherin.model.group.SendQuestionReq
import com.orwa.gatherin.utils.hideGone
import com.orwa.gatherin.utils.show
import java.util.*

class GroupSendQuestionsFragment : BaseTeacherFragment() {

    private val TAG = GroupSendQuestionsFragment::class.java.simpleName

    private lateinit var binding: FragmentGroupSendQuestionsBinding

    private val viewModel: SendQuestionsFragmentViewModel by viewModels()

    //Get all groups list related to this teacher user
    private val requestGetGroups = { viewModel.getGroupsList() }

    private lateinit var groupsListAdapter: GroupItemRecyclerViewAdapter

    private var questType: String = QUESTION_TYPE_TEXT
    private var numberOfMcq: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGroupSendQuestionsBinding.inflate(inflater, container, false)

        setupGroupsRV()

        initEventDriven()


        binding.tvSend.setOnClickListener {

            if (questType == QUESTION_TYPE_TEXT) {
                if (TextUtils.isEmpty(binding.etEssay.text.trim().toString())) {
                    binding.etEssay.error = getString(R.string.validate_empty_field)
                    toastSmall(R.string.send_question_incomplete_entry_data)
                } else {
                    //send questions to the leader
                    sendQuestion(QUESTION_TYPE_TEXT)
                }
            } else if (questType == QUESTION_TYPE_MULTIPLE_CHOICES) {
                if (TextUtils.isEmpty(binding.etMCQ.text.trim().toString())) {
                    binding.etMCQ.error = getString(R.string.validate_empty_field)
                    toastSmall(R.string.send_question_incomplete_entry_data)
                } else if (numberOfMcq == 2 && (!validateET(binding.etFirstChoice) || !validateET(
                        binding.etSecondChoice
                    ))
                ) {
                    binding.etFirstChoice.error = getString(R.string.validate_empty_field)
                    binding.etSecondChoice.error = getString(R.string.validate_empty_field)
                    toastSmall(R.string.send_question_incomplete_entry_data)

                } else if (numberOfMcq == 3 &&
                    (!validateET(binding.etFirstChoice) ||
                            !validateET(binding.etSecondChoice) ||
                            !validateET(binding.etThirdChoice))
                ) {
                    binding.etFirstChoice.error = getString(R.string.validate_empty_field)
                    binding.etSecondChoice.error = getString(R.string.validate_empty_field)
                    binding.etThirdChoice.error = getString(R.string.validate_empty_field)
                    toastSmall(R.string.send_question_incomplete_entry_data)

                } else if (numberOfMcq == 4 &&
                    (!validateET(binding.etFirstChoice) ||
                            !validateET(binding.etSecondChoice) ||
                            !validateET(binding.etThirdChoice) ||
                            !validateET(binding.etFourthChoice))
                ) {
                    binding.etFirstChoice.error = getString(R.string.validate_empty_field)
                    binding.etSecondChoice.error = getString(R.string.validate_empty_field)
                    binding.etThirdChoice.error = getString(R.string.validate_empty_field)
                    binding.etFourthChoice.error = getString(R.string.validate_empty_field)

                    toastSmall(R.string.send_question_incomplete_entry_data)
                } else { //all parameters are ok, send questions to the leader
                    sendQuestion(QUESTION_TYPE_MULTIPLE_CHOICES)
                }
            }

        }

        binding.cbAllGroups.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                groupsListAdapter.checkAllGroups()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


        //observer for loading groups inside section
        viewModel.sendQuestionNetworkState.observe(viewLifecycleOwner, Observer {
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
//            Log.i(TAG, "groups_list=$it")
            if (it != null) {
                groupsListAdapter.swapData(it)
            } else {
                requestGetGroups.invoke()
            }
        })

        viewModel.sendQuestionRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSendQuestionSuccessDialog()
            }
        })

    }

    private fun initEventDriven() {

        binding.radioEssayQuestion.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                questType = QUESTION_TYPE_TEXT
                binding.radioMCQuestion.isChecked = false
            }
        }
        binding.radioMCQuestion.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                questType = QUESTION_TYPE_MULTIPLE_CHOICES
                binding.radioEssayQuestion.isChecked = false
            }
        }
        binding.etAddChoice.setOnClickListener { v ->
            if (numberOfMcq == 2) {
                binding.etThirdChoice.show()
                numberOfMcq = 3
            } else if (numberOfMcq == 3) {
                binding.etFourthChoice.show()
                numberOfMcq = 4
                binding.etAddChoice.hideGone()
            }
        }
    }


    private fun setupGroupsRV() {
        // Set the adapter
        val view = binding.rvGroupsNames
        with(view) {
            layoutManager = LinearLayoutManager(context)
            groupsListAdapter = GroupItemRecyclerViewAdapter(
                listOf(),
                requireContext(),
                binding.rvEmptyView, false, {
                    binding.cbAllGroups.isChecked = false
                }
            )
            adapter = groupsListAdapter
        }
    }

    fun validateET(et: EditText): Boolean {
        return !TextUtils.isEmpty(et.text.trim().toString())
    }

    fun getSelectedGroupsList(): List<Int> {
        val l = ArrayList<Int>()

        l.addAll(groupsListAdapter.getSelectedGroupsIds())

        return l.toList()
    }

    fun sendQuestion(type: String) {
        val groupsIds = getSelectedGroupsList()
//        Log.i(TAG, "groupsIds=$groupsIds")
        if (groupsIds.isEmpty()) {
            toastFailure(R.string.send_question_no_selected_group)
        } else {
            val question: SendQuestionReq
            if (type == QUESTION_TYPE_TEXT) {
                val body = binding.etEssay.text.trim().toString()
                question = SendQuestionReq(body, groupsIds, null, type)
            } else { //Multi choices question
                val body = binding.etMCQ.text.trim().toString()
                val choices = getChoicesList()
                question = SendQuestionReq(body, groupsIds, choices, type)
            }
            hideKeyboardFrom(binding.root)
            viewModel.sendQuestion(question)
        }

    }

    private fun getChoicesList(): List<String>? {
        val c1 = binding.etFirstChoice.text.trim().toString()
        val c2 = binding.etSecondChoice.text.trim().toString()
        if (numberOfMcq == 2) {
            return (listOf(c1, c2))
        } else if (numberOfMcq == 3) {
            val c3 = binding.etThirdChoice.text.trim().toString()
            return listOf(c1, c2, c3)
        } else if (numberOfMcq == 4) {
            val c3 = binding.etThirdChoice.text.trim().toString()
            val c4 = binding.etFourthChoice.text.trim().toString()

            return listOf(c1, c2, c3, c4)
        }
        return null
    }

    private fun showSendQuestionSuccessDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = View.inflate(context, R.layout.send_question_success_dialog, null)
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


    companion object {
        const val QUESTION_TYPE_MULTIPLE_CHOICES = "multi"
        const val QUESTION_TYPE_TEXT = "single"

    }

}