package com.orwa.gatherin.present.start

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseFragment
import com.orwa.gatherin.utils.Constants
import com.orwa.gatherin.utils.Util


/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : BaseFragment() {

    private val TAG = SplashFragment::class.java.simpleName

//    private val activityViewModel:StartActivityViewModel by activityViewModels()

    private lateinit var mHandler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed({
            val user = getUser()
//            Log.i(TAG, "USER=$user")
            if (user != null && user.id != -1) {
//                Log.i(TAG, "USER=logged_in")

                //Logged in user
                val userType = user.type
//                Log.i(TAG,"USER_TYPE=$userType")
//                val bundle = bundleOf(Constants.SHARE_LINK_KEY to activityViewModel.sectionCode)
                if (userType.equals(Constants.USER_TYPE_TEACHER)) {
                    navigate(R.id.action_splashFragment_to_home_teacher_Activity)
                    requireActivity().finish()
                } else if (userType.equals(Constants.USER_TYPE_STUDENT)) {
//                    val action = SplashFragmentDirections.actionSplashFragmentToMyStudentActivity("myparam")

//                    navigate(action)
                    findNavController().navigate(R.id.action_splashFragment_to_my_studentActivity)
                    requireActivity().finish()
                } else {
                    toastFailure(R.string.user_role_not_defined)
                    //
                }

                Util.syncFirebaseToken(requireContext())
            } else {
//                Log.i(TAG,"HERE...")
                //Switch to Login page
//                navigate(R.id.action_splashFragment_to_loginTypeFragment,null,R.id.nav_graph)//action_splashFragment_to_loginTypeFragment
                navigate(R.id.action_splashFragment_to_loginTypeFragment,null,R.id.start_graph)
//                requireActivity().finish()
            }

        }, 2000)

        return view
    }







    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacksAndMessages(null)
    }
}