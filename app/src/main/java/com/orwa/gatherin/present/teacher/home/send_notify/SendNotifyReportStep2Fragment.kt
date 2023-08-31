package com.orwa.gatherin.present.teacher.home.send_notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseTeacherFragment


class SendNotifyReportStep2Fragment : BaseTeacherFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify_reports_step2, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SendNotifyReportStep2Fragment()
    }
}