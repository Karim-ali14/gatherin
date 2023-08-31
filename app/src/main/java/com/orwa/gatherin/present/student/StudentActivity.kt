package com.orwa.gatherin.present.student

import android.os.Bundle
import com.orwa.gatherin.base.BaseActivity
import com.orwa.gatherin.databinding.ActivityStudentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentActivity : BaseActivity() {

    private val TAG = StudentActivity::class.java.simpleName

    lateinit var binding: ActivityStudentBinding

//    private val viewModel: MyActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        intent?.let {
//            val share_link = it.getStringExtra(Constants.SHARE_LINK_KEY)
//            Log.i(TAG,"SHARE_LINK=$share_link")
//        }



    }

}