package com.orwa.gatherin.present.common.contact

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.ActivityNavigator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseActivity
import com.orwa.gatherin.databinding.ActivityChatImageBinding
import com.orwa.gatherin.utils.Util

class ChatImageActivity : BaseActivity() {

    private val TAG = ChatImageActivity::class.java.simpleName

    private lateinit var binding:ActivityChatImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            Log.d(TAG,"inside_intent")
            val imgPath = it.getStringExtra(IMAGE_PATH_KEY)

            Util.loadImage(this,binding.imageView,imgPath.toString())

        }

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    companion object{
        const val IMAGE_PATH_KEY="image_path"
    }
}