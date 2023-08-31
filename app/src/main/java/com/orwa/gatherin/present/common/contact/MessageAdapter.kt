package com.orwa.gatherin.present.common.contact

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orwa.gatherin.R
import com.orwa.gatherin.model.chat.ChatItem
import com.orwa.gatherin.model.chat.Option
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Pref.getUserInfo
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.Util.Companion.getFullPath
import com.orwa.gatherin.utils.Util.Companion.loadImageWithPlaceholder
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show
import java.io.IOException
import java.util.*


class MessageAdapter(
    private val ctx: Context,
    private val messages: ArrayList<ChatItem>,
    private val answerClickListener: TextQuestionClickListener,
    private val imageClickListener: (img: ImageView, imgPath: String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = MessageAdapter::class.java.simpleName

    private inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTxt: TextView = itemView.findViewById(R.id.sentTxt)

        init {
            messageTxt.setOnLongClickListener {

                Util.copyToClipBoard(ctx, messageTxt.text.toString().trim())
                Toast.makeText(ctx, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()

                true
            }

            messageTxt.setOnClickListener {
                tryOpenLink(messageTxt.text.toString().trim())
            }
        }

    }

    private inner class SentImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            imageView.setOnClickListener {
                val item = messages[bindingAdapterPosition]
                imageClickListener.invoke(imageView, item.img.toString())
            }

        }

    }

    private inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName: TextView? = itemView.findViewById(R.id.tvPersonName)

        val messageTxt: TextView
        val userImg: ImageView

        init {
            userImg = itemView.findViewById(R.id.chat_iv)
            messageTxt = itemView.findViewById(R.id.receivedTxt)

            messageTxt.setOnLongClickListener {

                Util.copyToClipBoard(ctx, messageTxt.text.toString().trim())
                Toast.makeText(ctx, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()

                true
            }

            messageTxt.setOnClickListener {
                tryOpenLink(messageTxt.text.toString().trim())
            }

        }
    }

    private inner class ReceivedImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName: TextView = itemView.findViewById(R.id.tvPersonName)

        val userImg: ImageView
        val imageView: ImageView
//        val nameTxt: TextView

        init {
            imageView = itemView.findViewById(R.id.imageView)
//            nameTxt = itemView.findViewById(R.id.nameTxt)
            userImg = itemView.findViewById(R.id.chat_iv)

            imageView.setOnClickListener {
                val item = messages[bindingAdapterPosition]
                imageClickListener.invoke(imageView, item.img.toString())
            }
        }
    }

    private inner class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName: TextView? = itemView.findViewById(R.id.tvPersonName)
        val userImg: ImageView? = itemView.findViewById(R.id.chat_iv)

        //        val videoView: VideoView = itemView.findViewById(R.id.vv)
        val videoView: ImageView = itemView.findViewById(R.id.vv)
        val play: ImageButton = itemView.findViewById(R.id.ibPlay)
//        var player :MediaPlayer? = null
//        var isReadyToPlay = false
//        var isPlaying = false

        fun bind(item: ChatItem) {
            personName?.text = item.senderName
            userImg?.let { iv ->
                item.senderImage?.let { loadUserImg(iv, it) }
            }

//            Log.i(TAG, "VIDEO_PATH=${getFullPath(item.video.toString())}")

//            Util.loadImage(ctx, videoView, getFullPath(item.video.toString()))

            val interval: Long = 2 * 1000
            val options = RequestOptions().frame(interval)
            Glide.with(ctx)
                .asBitmap()
                .load(getFullPath(item.video.toString())) //Actual path for a .mp4 file that works without the .apply(options) but loads the thumbnail which is not what I want
                .placeholder(R.drawable.splash_bg)
                .apply(options)
                .into(videoView)


//            if(isPlaying){
//                play.hide()
//            }else{
//                play.show()
//            }
            //Use a media controller so that you can scroll the video contents
            //and also to pause, start the video.
            //Use a media controller so that you can scroll the video contents
            //and also to pause, start the video.
//            val mediaController = MediaController(ctx)
//            mediaController.setAnchorView(videoView)
//            videoView.setMediaController(mediaController)
//            videoView.setVideoURI(Uri.parse(Util.getFullPath(item.video.toString())))

//            Log.i(TAG, "VIDEO_PATH=${item.video.toString()}")

            videoView.setOnClickListener {
                launchVideoChatActivity(item.video.toString())
            }

            play.setOnClickListener {

                launchVideoChatActivity(item.video.toString())

//                if (isReadyToPlay) {
//                    isPlaying = true
//                    it.hide()
//                    videoView.start()
//                } else {
//                    Toast.makeText(ctx, R.string.play_video_not_ready, Toast.LENGTH_SHORT)
//                        .show()
//                }
            }

//            videoView.setOnPreparedListener {
//                it?.let { it1 ->
//                    player = it1
//                    isReadyToPlay = true
//                    isPlaying = false
//                }
//            }


//            videoView.setOnCompletionListener {
//                it?.let { it1 ->
//                    isPlaying = false
//                    play.show()
//                }
//            }
        }

    }

//    fun getFirstFrame(path: String?, context: Context?): ByteArray? {
//        val mediaMetadataRetriever = MediaMetadataRetriever()
//        mediaMetadataRetriever.setDataSource(context, Uri.parse(path))
//        val bitmap = mediaMetadataRetriever.getFrameAtTime(0)
//        val stream = ByteArrayOutputStream()
//        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        return stream.toByteArray()
//    }

    fun launchVideoChatActivity(videoPath: String) {
        val intent = Intent(ctx, VideoChatActivity::class.java)
        intent.putExtra(VideoChatActivity.VIDEO_URL_KEY, videoPath)
        ctx.startActivity(intent)
    }

    private inner class RecordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName: TextView? = itemView.findViewById(R.id.tvPersonName)

        val userImg: ImageView? = itemView.findViewById(R.id.chat_iv)
        val play: ImageButton = itemView.findViewById(R.id.ibPlay)
        val pause: ImageButton = itemView.findViewById(R.id.ibPause)

        var player: MediaPlayer? = null
        var isReadyToPlay = false
        var isPlaying = false

        init {
            play.setOnClickListener {
                if (isReadyToPlay) {
                    isPlaying = true
                    play.hide()
                    pause.show()
                    player?.start()
                } else {
                    Toast.makeText(ctx, R.string.play_record_not_ready, Toast.LENGTH_SHORT).show()
                }

            }
            pause.setOnClickListener {
                isPlaying = false
                player?.pause()
                pause.hide()
                play.show()
            }

        }

        fun bind(item: ChatItem) {
            personName?.text = item.senderName
            userImg?.let { iv ->
                item.senderImage?.let { loadUserImg(iv, it) }
            }

            if (isPlaying) {
                play.hide()
                pause.show()
            } else {
                play.show()
                pause.hide()
            }
            item.record?.let { initPlaying(getFullPath(it)) }

            player?.setOnPreparedListener {
                isReadyToPlay = true
                isPlaying = false
            }

            player?.setOnCompletionListener {
//                Log.i(TAG, "OnCompletionListener=true")
                isPlaying = false
                pause.hide()
                play.show()
            }
        }

        private fun initPlaying(record: String) {
//            Log.i(TAG, "init_playing_RECORD_PATH=$record")
            player = MediaPlayer().apply {
                try {
                    setDataSource(record)
//                    Log.i(TAG, "before_prepare")
                    prepareAsync()
//                    Log.i(TAG, "after_prepare")
//                    start()
                } catch (e: IOException) {
//                    Log.e(TAG, e.message.toString())
                    Toast.makeText(ctx, R.string.play_record_not_ready, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private inner class ReceivedSingleQuestionHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val personName: TextView = itemView.findViewById(R.id.tvPersonName)

        val userImg: ImageView = itemView.findViewById(R.id.chat_iv)
        val question: TextView = itemView.findViewById(R.id.tvQuestion)
        val view : View = itemView.findViewById(R.id.questionContainer)

        init {
            view.setOnClickListener {
                Util.copyToClipBoard(ctx, question.text.toString().trim())
                Toast.makeText(ctx, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
            }

        }
    }


    private inner class ReceivedMultiQuestionHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val view : View = itemView.findViewById(R.id.questionContainer)

        val personName: TextView = itemView.findViewById(R.id.tvPersonName)

//        private var isLoading = false

        val userImg: ImageView = itemView.findViewById(R.id.chat_iv)
        val question: TextView = itemView.findViewById(R.id.tvQuestion)

        //        val rg = itemView.findViewById<RadioGroup>(R.id.rgAnswers)
        val rb1: RadioButton = itemView.findViewById(R.id.rbFirst)
        val rb2: RadioButton = itemView.findViewById(R.id.rbSecond)
        val rb3: RadioButton = itemView.findViewById(R.id.rbThird)
        val rb4: RadioButton = itemView.findViewById(R.id.rbFourth)

        val pb1: ProgressBar = itemView.findViewById(R.id.progress1)
        val pb2: ProgressBar = itemView.findViewById(R.id.progress2)
        val pb3: ProgressBar = itemView.findViewById(R.id.progress3)
        val pb4: ProgressBar = itemView.findViewById(R.id.progress4)

        val rate1: TextView = itemView.findViewById(R.id.tvRate1)
        val rate2: TextView = itemView.findViewById(R.id.tvRate2)
        val rate3: TextView = itemView.findViewById(R.id.tvRate3)
        val rate4: TextView = itemView.findViewById(R.id.tvRate4)

        init {
            view.setOnClickListener {
                Util.copyToClipBoard(ctx, question.text.toString().trim())
                Toast.makeText(ctx, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
            }

        }


        fun bind(item: ChatItem) {

            personName.text = item.senderName

            loadUserImg(userImg, item.senderImage.toString())
            question.text = item.question?.body.toString()
            var allVotes = 0
            item.question?.options?.let { options ->

                var isOptionSelected = false
                if (options.size >= 2) {
                    isOptionSelected = options[0].selected || options[1].selected
                }
                if (options.size >= 3) {
                    isOptionSelected = isOptionSelected || options[2].selected
                }
                if (options.size >= 4) {
                    isOptionSelected = isOptionSelected || options[3].selected
                }
//                val pos = bindingAdapterPosition
//                Log.i(TAG,"item_pos=$pos,$isOptionSelected")
                if (isOptionSelected) { // If there was an answer to this question (option selected)
                    setButtonsStatus(false)
                } else {
                    setButtonsStatus(true)
                }

                if (options.size >= 2) {
                    val opt1 = options[0]
                    setRadioButtonData(rb1, rb2, rb3, rb4, pb1, 0, opt1)
                    val opt2 = options[1]
                    setRadioButtonData(rb2, rb1, rb3, rb4, pb2, 1, opt2)
                    allVotes += options[0].counter + options[1].counter
                }
                if (options.size >= 3) {
                    val opt3 = options[2]
                    setRadioButtonData(rb3, rb1, rb2, rb4, pb3, 2, opt3)
                    allVotes += options[2].counter
                    rb3.show()
                    rate3.show()

                }
                if (options.size == 4) {
                    val opt4 = options[3]
                    setRadioButtonData(rb4, rb1, rb2, rb3, pb4, 3, opt4)
                    allVotes += options[3].counter
                    rb4.show()
                    rate4.show()
                }
                if (allVotes != 0 && options.size >= 2) {
//                    Log.i(TAG, "allVotes=$allVotes")
                    val rate1Val = (options[0].counter)
                    rate1.text = getStringRate(rate1Val)

                    val rate2Val = (options[1].counter)
                    rate2.text = getStringRate(rate2Val)

                    if (options.size >= 3) {
//                        Log.i(TAG, "OPTION=3")
                        val rate3Val = (options[2].counter)
                        rate3.text = getStringRate(rate3Val)
                    }

                    if (options.size == 4) {
//                        Log.i(TAG, "OPTION=4")
                        val rate4Val = (options[3].counter)
                        rate4.text = getStringRate(rate4Val)
                    }
                }

            }
        }

        fun setRadioButtonData(
            r1: RadioButton,
            r2: RadioButton,
            r3: RadioButton,
            r4: RadioButton,
            pb: ProgressBar,
            optionPos: Int,
            opt: Option
        ) {
            r1.text = opt.body
            r1.isChecked = opt.selected

            r1.setOnClickListener {
                if (r1.isChecked) {
                    removeSelection(r2, r3, r4)
                    pb.show()
                    setButtonsStatus(false)
                    answerClickListener.onQuestionClicked(
                        messages[bindingAdapterPosition].question!!,
                        messages[bindingAdapterPosition].question!!.options!![optionPos],
                        bindingAdapterPosition,
                        optionPos,
                        callBack
                    )
                }
            }

        }

        fun removeSelection(r1: RadioButton, r2: RadioButton, r3: RadioButton) {
            r2.isChecked = false
            r3.isChecked = false
            r1.isChecked = false

        }

        /**
         * Enable or disable the radio buttons
         */
        fun setButtonsStatus(status: Boolean) {
            rb1.isEnabled = status
            rb2.isEnabled = status
            rb3.isEnabled = status
            rb4.isEnabled = status

        }

        //CallBack to used when a result is returned from the server after a radio button is selected
        val callBack = { position: Int, optionPos: Int, wasSelected: Boolean ->
//            Log.i(TAG, "CALL_BACK=$position, $optionPos")
            setButtonsStatus(true)
            pb1.hide()
            pb2.hide()
            val numOptions = messages[position].question!!.options!!.size
            if (numOptions >= 3) {
                pb3.hide()
            } else if (numOptions >= 4) {
                pb4.hide()
            }
            messages[position].question!!.options!![optionPos].selected = wasSelected
            notifyItemChanged(position)
        }

        fun getStringRate(rate: Int): String {
            return ctx.getString(R.string.answers_rate, rate)
        }
    }


    private inner class EmptyMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (isChatMine(message) && !isChatQuestion(message) && !isChatMultiQuestion(message)) { //my chat
            if (isChatText(message))
                return TYPE_MESSAGE_SENT
            else if (isChatImg(message))
                return TYPE_IMAGE_SENT
            else if (isChatVideo(message)) {
                return TYPE_SENT_VIDEO
            } else if (isChatRecord(message)) {
                return TYPE_SENT_RECORD
            }
        } else { //Other chat
            if (isChatText(message))
                return TYPE_MESSAGE_RECEIVED
            else if (isChatImg(message))
                return TYPE_IMAGE_RECEIVED
            else if (isChatVideo(message)) {
                return TYPE_RECEIVED_VIDEO
            } else if (isChatRecord(message)) {
                return TYPE_RECEIVED_RECORD
            } else if (isChatQuestion(message)) { // If chat is a question
                if (isChatSingleQuestion(message)) {
//                    Log.i(TAG, "TYPE=single_question")
                    return TYPE_RECEIVED_SINGLE_QUESTION
                } else if (isChatMultiQuestion(message)) {
                    return TYPE_RECEIVED_MULTI_QUESTION
                }
            }
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(ctx)
        val view: View
        when (viewType) {
            TYPE_MESSAGE_SENT -> {
                view = inflater.inflate(R.layout.item_sent_message, parent, false)
                return SentMessageHolder(view)
            }
            TYPE_MESSAGE_RECEIVED -> {
                view = inflater.inflate(R.layout.item_received_message, parent, false)
                return ReceivedMessageHolder(view)
            }
            TYPE_IMAGE_SENT -> {
                view = inflater.inflate(R.layout.item_sent_image, parent, false)
                return SentImageHolder(view)
            }
            TYPE_IMAGE_RECEIVED -> {
                view = inflater.inflate(R.layout.item_received_photo, parent, false)
                return ReceivedImageHolder(view)
            }
            TYPE_RECEIVED_SINGLE_QUESTION -> {
                view = inflater.inflate(R.layout.item_received_question, parent, false)
                return ReceivedSingleQuestionHolder(view)
            }

            TYPE_RECEIVED_MULTI_QUESTION -> {
                view = inflater.inflate(R.layout.item_received_question_multi, parent, false)
                return ReceivedMultiQuestionHolder(view)
            }
            TYPE_SENT_VIDEO -> {
                view = inflater.inflate(R.layout.item_sent_video, parent, false)
                return VideoHolder(view)
            }

            TYPE_RECEIVED_VIDEO -> {
                view = inflater.inflate(R.layout.item_received_video, parent, false)
                return VideoHolder(view)
            }

            TYPE_SENT_RECORD -> {
                view = inflater.inflate(R.layout.item_sent_record, parent, false)
                return RecordHolder(view)
            }

            TYPE_RECEIVED_RECORD -> {
                view = inflater.inflate(R.layout.item_received_record, parent, false)
                return RecordHolder(view)
            }

        }
        view = inflater.inflate(R.layout.item_received_photo, parent, false) //not thing to do
        return EmptyMessageHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentChat = messages[position]
//        Log.i(TAG, "current_chat($position)=$currentChat")

        if (isChatMine(currentChat) && !isChatSingleQuestion(currentChat) && !isChatMultiQuestion(
                currentChat
            )
        ) { //my chat
            if (isChatText(currentChat)) { //text chat
                val messageHolder = holder as SentMessageHolder
                messageHolder.messageTxt.text = currentChat.msg
            } else if (isChatImg(currentChat)) { // image chat
                val imageHolder = holder as SentImageHolder
                currentChat.img?.let { loadImage(imageHolder.imageView, it) }
            } else if (isChatVideo(currentChat)) {
                val videoHolder = holder as VideoHolder
                videoHolder.bind(currentChat)

            } else if (isChatRecord(currentChat)) {
                val sentRecordHolder = holder as RecordHolder
                sentRecordHolder.bind(currentChat)

            }
        } else { //Others chat
            if (isChatText(currentChat)) { // text chat
                val messageHolder = holder as ReceivedMessageHolder
                messageHolder.personName?.text = currentChat.senderName
                messageHolder.messageTxt.text = currentChat.msg
                currentChat.senderImage?.let { loadUserImg(messageHolder.userImg, it) }
            } else if (isChatImg(currentChat)) { //image chat
                val imageHolder = holder as ReceivedImageHolder
                imageHolder.personName.text = currentChat.senderName
                currentChat.img?.let { loadImage(imageHolder.imageView, it) }
                currentChat.senderImage?.let { loadUserImg(imageHolder.userImg, it) }

            } else if (isChatVideo(currentChat)) {
                val videoHolder = holder as VideoHolder
                videoHolder.bind(currentChat)

            } else if (isChatRecord(currentChat)) {
                val sentRecordHolder = holder as RecordHolder
                sentRecordHolder.bind(currentChat)

            } else if (isChatSingleQuestion(currentChat)) {
//                Log.i(TAG, "SingleQuestionHolder=$currentChat")
                val questionHolder = holder as ReceivedSingleQuestionHolder
                questionHolder.personName.text = currentChat.senderName

                currentChat.senderImage?.let { loadUserImg(questionHolder.userImg, it) }
                currentChat.question?.body.let { questionHolder.question.text = it }
//                questionHolder.answer.text = ctx.getString(R.string.writeAnswerHere)

            } else if (isChatMultiQuestion(currentChat)) {
                val questionHolder = holder as ReceivedMultiQuestionHolder
                questionHolder.personName.text = currentChat.senderName

                questionHolder.bind(currentChat)
                //bla bla bla bind()
            }
        }
    }

    private fun tryOpenLink(link: String) {
        if (link.startsWith("http://") || link.startsWith("https://")) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            if (browserIntent.resolveActivity(ctx.packageManager) != null) {
                ctx.startActivity(browserIntent)
            }
        }

    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

//        Log.i(TAG,"recycled")

        when (holder) {
            is RecordHolder -> {
//                Log.i(TAG,"recycled_record")
                holder.player?.stop()
                holder.isPlaying = false
                holder.player?.release()
                holder.player = null
                holder.isReadyToPlay = false
            }
//            is VideoHolder -> {
//                holder.videoView.stopPlayback()
//                holder.isPlaying = false
//                holder.isReadyToPlay = false
//                holder.player?.release()
//            }
        }
    }

    private fun isChatMine(item: ChatItem): Boolean {
        val user = getUserInfo(ctx)
        return if (user != null) {
            user.id == item.senderId
        } else false
    }

    private fun isChatImg(item: ChatItem): Boolean {
        return item.type == ContactFragment.CHAT_ITEM_TYPE_IMAGE
    }

    private fun isChatText(item: ChatItem): Boolean {
        return item.type == ContactFragment.CHAT_ITEM_TYPE_TEXT
    }

    private fun isChatVideo(item: ChatItem): Boolean {
        return item.type == ContactFragment.CHAT_ITEM_TYPE_VIDEO
    }

    private fun isChatRecord(item: ChatItem): Boolean {
        return item.type == ContactFragment.CHAT_ITEM_TYPE_RECORD
    }

    private fun isChatQuestion(item: ChatItem): Boolean {
        return item.type == ContactFragment.CHAT_ITEM_TYPE_QUESTION
    }

    private fun isChatSingleQuestion(item: ChatItem): Boolean {
        return item.question?.type == ContactFragment.QUESTION_TYPE_SINGLE
    }

    private fun isChatMultiQuestion(item: ChatItem): Boolean {
        return item.question?.type == ContactFragment.QUESTION_TYPE_MULTI
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addItem(chat: ChatItem) {
//        Log.i(TAG, "SIZE=" + messages.size + ", count=" + itemCount)
        messages.add(chat)
        notifyItemInserted(messages.size - 1)
    }

    /**
     * An answer has changed. reflect with adapter to change data
     */
    fun changeItemAnswer(questionId: Int, optionId: Int, userId: Int) {
        val user = Pref.getUserInfo(ctx)
        //search for item position
        for (i in 0 until messages.size) {
            val item = messages[i]
            if (item.type == ContactFragment.CHAT_ITEM_TYPE_QUESTION && item.question?.id == questionId) {
                item.question.let {
                    it.options?.let { opts ->
                        for (opt in opts) {
                            if (opt.id == optionId) { //We reached the targeted option that has changed
                                opt.counter++ // Indicates that there was a new answer to this option. register it
                                if (user!!.id == userId) {// current user is the owner of this change
                                    opt.selected = opt.id == optionId
                                }
                                break
                            }
                        }
                    }
                }
                notifyItemChanged(i)
                break
            }
        }

    }


    private fun loadImage(iv: ImageView, img: String) {
//        Log.i(TAG, "image_path=$img")
        Util.loadImage(ctx, iv, img, R.drawable.ic_error_copy_2)

    }

    private fun loadUserImg(iv: ImageView, img: String) {
        loadImageWithPlaceholder(
            ctx = ctx,
            img = iv,
            imgPath = img,
            errorImg = R.drawable.avatar_1_raster
        )
    }


    fun swapData(chats: List<ChatItem>) {
        messages.clear()
        messages.addAll(chats)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_MESSAGE_SENT = 0
        private const val TYPE_MESSAGE_RECEIVED = 1
        private const val TYPE_IMAGE_SENT = 2
        private const val TYPE_IMAGE_RECEIVED = 3

        private const val TYPE_RECEIVED_SINGLE_QUESTION = 4
        private const val TYPE_RECEIVED_MULTI_QUESTION = 5
        private const val TYPE_SENT_VIDEO = 6
        private const val TYPE_RECEIVED_VIDEO = 7
        private const val TYPE_SENT_RECORD = 8
        private const val TYPE_RECEIVED_RECORD = 9


    }
}