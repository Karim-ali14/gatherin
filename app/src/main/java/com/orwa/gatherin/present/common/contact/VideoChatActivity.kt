package com.orwa.gatherin.present.common.contact

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ActivityVideoChatBinding
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show

class VideoChatActivity : AppCompatActivity() {

    lateinit var binding:ActivityVideoChatBinding

    var player : MediaPlayer? = null
    var isReadyToPlay = false
    var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent?.let {
            val url = it.getStringExtra(VIDEO_URL_KEY)
            url?.let { it1->
                val mediaController = MediaController(this)
                mediaController.setAnchorView(binding.vv)
                binding.vv.setMediaController(mediaController)
                binding.vv.setVideoURI(Uri.parse(Util.getFullPath(it1)))

                binding.vv.setOnPreparedListener {
                    it?.let { mediaPlayer ->
                        player = mediaPlayer
                        isReadyToPlay = true
                        isPlaying = false
                    }
                }

                binding.vv.setOnCompletionListener {
                    it?.let { mediaPlayer ->
                        isPlaying = false
                        binding.ibPlay.show()
                    }
                }

                binding.ibPlay.setOnClickListener {
                    if (isReadyToPlay) {
                        isPlaying = true
                        binding.ibPlay.hide()
                        binding.vv.start()
                    } else {
                        Toast.makeText(this, R.string.play_video_not_ready, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    companion object{
        const val VIDEO_URL_KEY="vide_url"
    }
}