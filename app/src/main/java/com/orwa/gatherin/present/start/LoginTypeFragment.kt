package com.orwa.gatherin.present.start

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseFragment
import com.orwa.gatherin.utils.Constants
import com.orwa.gatherin.databinding.FragmentLoginTypeBinding
import com.orwa.gatherin.utils.Lang
import com.orwa.gatherin.utils.LocaleHelper
import com.orwa.gatherin.utils.Util


/**
 * A simple [Fragment] subclass.
 * Use the [LoginTypeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginTypeFragment : BaseFragment() {

    private val TAG = LoginTypeFragment::class.java.simpleName


    lateinit var binding: FragmentLoginTypeBinding
    var userType:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.i("LoginTypeFragment","oncreate_")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginTypeBinding.inflate(inflater,container,false)


        val lang = Util.getCurrentLang(requireContext())
        if (lang == Lang.ARABIC) {
            binding.tvEnglish.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lightBlack
                )
            )
            binding.tvEnglish.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.lang_bg_non_selected_bordered)

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
                ContextCompat.getDrawable(requireContext(), R.drawable.lang_bg_non_selected_bordered)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlUser.setOnClickListener {
            userType=Constants.USER_TYPE_STUDENT
            navigateToLogin()
        }
        binding.rlTeacher.setOnClickListener {
            userType=Constants.USER_TYPE_TEACHER
            navigateToLogin()
        }
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


    fun navigateToLogin(){
        userType?.let {
            val bundle = bundleOf(Constants.USER_TYPE_KEY to userType)
            navigate(R.id.action_loginTypeFragment_to_loginFragment,bundle)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                LoginTypeFragment()
    }
}