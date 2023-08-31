package com.orwa.gatherin.present.teacher.home.group_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.databinding.FragmentGroupResultsDetailsBinding
import com.orwa.gatherin.utils.Util

class GroupResultsDetailsFragment : BaseTeacherFragment() {

    private lateinit var binding:FragmentGroupResultsDetailsBinding

    private val viewModel:GroupResultsDetailsViewModel by viewModels()

    val request = { groupId:Int-> viewModel.getGroupAnswer(groupId)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentGroupResultsDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.results))
        arguments?.let {
            val deptName = it.getString(GroupsResultsFragment.DEPARTMENT_NAME_KEY)
            val groupName = it.getString(GroupsResultsFragment.GROUP_NAME_KEY)
            setupHeaderDetailsTitle(getString(R.string.dept_group_title,deptName,groupName))
        }

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

        viewModel.leaderAnswerRes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.tvLeaderName.text = it.leader.fullName
                binding.tvWriteAnswer.text = it.answer.body
                Util.loadImage(requireContext(),binding.ivProfileImage,it.leader.picture)
                binding.ivProfileImage
            } else {
                arguments?.let { args->
                    request.invoke(args.getInt(GroupsResultsFragment.GROUP_ID_KEY))
                }
            }
        })
    }
}