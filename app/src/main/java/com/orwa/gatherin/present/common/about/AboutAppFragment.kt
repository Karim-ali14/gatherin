package com.orwa.gatherin.present.common.about

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.FragmentAboutAppBinding
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.utils.Lang
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


class AboutAppFragment : BaseTeacherFragment() {


    lateinit var binding: FragmentAboutAppBinding

    private val viewModel: AboutViewModel by viewModels()

    val request={
        viewModel.getAboutAppRes()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentAboutAppBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaderTitle(getString(R.string.about_app_title))

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
                when (it) {
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE->toastFailure(R.string.network_error)
                else ->{}}

            }
        })

        viewModel.aboutRes.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                val info = if(Util.getCurrentLang(requireContext())== Lang.ARABIC) it.bodyAr else it.bodyEn


                val formattedInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(info, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(info)
                }

                binding.tvAboutApp.setText(formattedInfo)

            }else{
                request.invoke()
            }
        })

    }
}