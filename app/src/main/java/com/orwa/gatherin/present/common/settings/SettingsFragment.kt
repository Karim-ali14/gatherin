package com.orwa.gatherin.present.common.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseFragment
import com.orwa.gatherin.databinding.FragmentSettingsBinding
import com.orwa.gatherin.present.start.StartActivity
import com.orwa.gatherin.present.student.chats.MastersChatListFragment
import com.orwa.gatherin.present.teacher.MyActivityViewModel
import com.orwa.gatherin.utils.*


//Parent fragment might be changed later, could be Base Fragment as SettingsFragment exists in Teacher & Student
class SettingsFragment : BaseFragment() {

    private val TAG = SettingsFragment::class.java.simpleName

    private val activityViewModel: MyActivityViewModel by activityViewModels()

    private val viewModel: SettingsViewModel by viewModels()

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val isTeacher = isTeacherAccount()
        isTeacher?.let {
            if (it) {
                binding.rlLeaderChats.hideGone()
            } else {
                binding.rlSubscription.hideGone()
            }
        }

        binding.rlLogout.setOnClickListener {
            Util.showOptionMsgDialog(
                requireContext(),
                R.string.log_out_confirm_msg_content,
                func = {

                    viewModel.logOut()

                })
        }

        val lang = Util.getCurrentLang(requireContext())
        if (lang == Lang.ARABIC) {
            binding.tvEnglish.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lightBlack
                )
            )
            binding.tvEnglish.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.lang_bg_non_selected)

            binding.tvArabic.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvArabic.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.lang_bg_selected)

        } else {
            binding.tvArabic.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lightBlack
                )
            )
            binding.tvArabic.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.lang_bg_non_selected)

            binding.tvEnglish.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvEnglish.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.lang_bg_selected)
        }

        binding.tvEnglish.setOnClickListener {
            changeLanguage(Constants.ENGLISH_LANGUAGE)
        }

        binding.tvArabic.setOnClickListener {
            changeLanguage(Constants.ARABIC_LANGUAGE)
        }


        return binding.root
    }

    private fun continueLogOut() {
        Pref.clearUserData(requireContext())
        Util.changeFirebaseTokenSyncState(requireContext(), false)
        startActivityWithClear(StartActivity::class.java)
        requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.settings))

        binding.rlEditProfile.setOnClickListener { navigate(R.id.action_settingsFragment_to_editProfileFragment) }
        binding.rlEditPassword.setOnClickListener { navigate(R.id.action_settingsFragment_to_changePasswordFragment) }
        binding.rlAbout.setOnClickListener { navigate(R.id.action_settingsFragment_to_aboutAppFragment) }
        binding.rlPolicy.setOnClickListener { navigate(R.id.action_settingsFragment_to_appPolicyFragment) }
        binding.rlSubscription.setOnClickListener { navigate(R.id.action_settingsFragment_to_subscriptionsListFragment) }
        binding.rlLeaderChats.setOnClickListener {
            val bundle = bundleOf(MastersChatListFragment.IS_FROM_SETTINGS_KEY to true)
            navigate(R.id.action_settingsFragment_to_mastersChatListFragment, bundle)
        }

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        continueLogOut()
                    }
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

    }

    fun changeLanguage(lang: String) {
        storeLanguageChangeInSharePref(lang)
        LocaleHelper.setLocale(requireContext(), lang)
        startActivityWithClear(StartActivity::class.java)
        requireActivity().finish()

    }

    @SuppressLint("ApplySharedPref")
    private fun storeLanguageChangeInSharePref(language: String) {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sp.edit()
        editor.putString(Constants.CURRENT_SET_LANGUAGE_KEY, language).commit()
    }

    override fun onStart() {
        super.onStart()
        activityViewModel.setBottomNavStatus(false)
    }
}