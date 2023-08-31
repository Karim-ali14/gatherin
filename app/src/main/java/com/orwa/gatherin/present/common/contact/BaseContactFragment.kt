//package com.orwa.gatherin.present.common.contact
//
//import android.Manifest
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.media.MediaRecorder
//import android.net.Uri
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.activityViewModels
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.orwa.gatherin.R
//import com.orwa.gatherin.base.AuthNetworkState
//import com.orwa.gatherin.base.BaseFragment
//import com.orwa.gatherin.databinding.GroupContactFragmentBinding
//import com.orwa.gatherin.model.chat.ChatItem
//import com.orwa.gatherin.model.chat.Option
//import com.orwa.gatherin.model.chat.Question
//import com.orwa.gatherin.present.student.group.SendResponseToMasterActivity
//import com.orwa.gatherin.present.teacher.MyActivityViewModel
//import com.orwa.gatherin.utils.*
//import com.risingpark.risingvoiceindicator.RisingVoiceIndicator
//import com.risingpark.risingvoiceindicator.VoiceIndicator
//import droidninja.filepicker.FilePickerBuilder
//import droidninja.filepicker.FilePickerConst
//import io.socket.client.Ack
//import io.socket.client.IO
//import io.socket.client.Socket
//import io.socket.emitter.Emitter
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.asRequestBody
//import org.json.JSONArray
//import org.json.JSONException
//import org.json.JSONObject
//import java.io.File
//import java.io.IOException
//import java.net.URISyntaxException
//import kotlin.random.Random
//
//
//abstract class BaseContactFragment : BaseFragment(), TextWatcher {
//
//
//    private val TAG = BaseContactFragment::class.java.simpleName
//
//    private val viewModel: GroupContactViewModel by viewModels()
//
//    private val activityViewModel: MyActivityViewModel by activityViewModels()
//
//    lateinit var binding: GroupContactFragmentBinding
//
//    private var mSocket: Socket? = null
//
//    private var isConnected = false
//
//    private var mAddImages = ArrayList<Uri>()
//
//    private lateinit var chatUsers: List<ChatUser>
//    private val chats = ArrayList<ChatItem>()
//
//    private var lastQuestionId = -1
//
//    private lateinit var sendRecordDialog: RecordAudioDialog
//
//    private var lastFileType: MessageType? = null
//
//
//    //    private val SERVER_PATH = "ws://echo.websocket.org"
////    private val SERVER_PATH = "ws://apis.gathering.host/"
//    private val SERVER_PATH = Constants.SOCKET_LINK
//
//
//    private var messageEdit: EditText? = null
//    private var sendBtn: View? = null
//    private var pickImgBtn: android.view.View? = null
////    private val IMAGE_REQUEST_ID = 1
//
//    private lateinit var onlineMembersAdapter: OnlineMembersRecyclerViewAdapter
//    private lateinit var messageAdapter: MessageAdapter
//
//    private var recorder: MediaRecorder? = null
//    private var fileName: String? = null
//
//
//
////    val pickImageLauncher =
////        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
////            if (result) {
////                Log.i(TAG, "IMG=$imageUri")
////                processFetchedImage(imageUri)
////            }
////        }
//
//
//    private fun pickGalleryImages() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            requestReadExternalPermission.launch(
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            )
//        } else
//            launchFilePicker()
//    }
//
//    private fun startRecordingProcess() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.RECORD_AUDIO
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            requestRecordAudioPermission.launch(
//                arrayOf(
//                    Manifest.permission.RECORD_AUDIO
//                )
//            )
//        } else{
//            sendRecordDialog.show()
//        }
//    }
//
//    private val requestRecordAudioPermission =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            permissions.entries.forEach {
//                Log.i("DEBUG_", "${it.key} = ${it.value}")
//                if (it.key != null && it.key == Manifest.permission.RECORD_AUDIO && it.value) {
//                    sendRecordDialog.show()
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        getString(R.string.open_file_picker_permission_denied),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//
//    private val requestReadExternalPermission =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            permissions.entries.forEach {
//                Log.i("DEBUG_", "${it.key} = ${it.value}")
//                if (it.key != null && it.key == Manifest.permission.READ_EXTERNAL_STORAGE && it.value) {
//                    launchFilePicker()
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        getString(R.string.open_file_picker_permission_denied),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//
//    private fun startRecording() {
//        fileName = Util.createTempImageFileName(requireContext())
//        if(fileName==null){
//            toastFailure(R.string.error_recording_audio)
//        }else{
//            recorder = MediaRecorder().apply {
//                setAudioSource(MediaRecorder.AudioSource.MIC)
//                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                setOutputFile(fileName)
//                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//
//                try {
//                    prepare()
//                } catch (e: IOException) {
//                    toastFailure(R.string.error_recording_audio)
//                }
//
//                start()
//            }
//        }
//
//    }
//
//    private fun stopRecording() {
//        recorder?.apply {
//            stop()
//            release()
//        }
//        recorder = null
//    }
//
//    private fun launchFilePicker() {
//        mAddImages = java.util.ArrayList()
//        FilePickerBuilder.instance.setMaxCount(1)
//            .setSelectedFiles(mAddImages)
//            .setActivityTheme(R.style.FilePickerActivityTheme)
//            .enableVideoPicker(true)
//            .pickPhoto(this)
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        sendRecordDialog = RecordAudioDialog()
//        sendRecordDialog.create()
////        name = "user_name"
//        initiateSocketConnection()
//    }
//
//    private fun initiateSocketConnection() {
////        val client = OkHttpClient()
////        val request: Request = Request.Builder().url(SERVER_PATH).build()
////        webSocket =
////            client.newWebSocket(request, SocketListener())
//
//        try {
//            Log.i(TAG, "socket_connect_to=$SERVER_PATH")
//            mSocket = IO.socket(SERVER_PATH)
//        } catch (e: URISyntaxException) {
//            toastFailure(R.string.socket_connect_error)
//            binding.rvProgress.hide()
//            return
//        }
//
//        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
//        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
////        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        //        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket!!.on(EVENT_JOIN, onUserJoined)
////        mSocket!!.on("sendMessage", onMessageSent)
//        mSocket!!.on(EVENT_RECEIVE_MESSAGE, onMessageReceived)
//        mSocket!!.on(EVENT_USERS_LIST, onUserListChanged)
//        mSocket!!.on(EVENT_LAST_CHATS, onLastChatsFetched)
//
//        mSocket!!.on(EVENT_CHANGE_OPTION, onOptionChanged)
//
//
////        mSocket!!.on("user left", onUserLeft)
////        mSocket!!.on("typing", onTyping)
////        mSocket!!.on("stop typing", onStopTyping)
////        attemptJoin()
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = GroupContactFragmentBinding.inflate(inflater, container, false)
//        mSocket!!.connect()
//        //This should be displayed to indicate connection is underway
//        binding.rvProgress.show()
//        val user = getUser()
//        //If user is a leader(student), show the SendResponse layout
//        Log.i(
//            TAG,
//            "IS_TEACHER=${isTeacherAccount()}, master=${activityViewModel.groupLeader.value?.id}, userId=${user?.id}"
//        )
//        if (isTeacherAccount() == false && activityViewModel.groupLeader.value?.id == user?.id) {
//            binding.rlSendAnswer.show()
//            binding.rlSendAnswer.setOnClickListener {
//                if (lastQuestionId == -1) {
//                    toastFailure(R.string.missing_question)
//                } else {
//
//                    activityViewModel.currentGroup.value?.let {
//                        val name = it.master?.fullName
//                        val img = it.master?.picture
//
//                        val intent =
//                            Intent(requireContext(), SendResponseToMasterActivity::class.java)
//                        intent.putExtra(
//                            SendResponseToMasterActivity.QUESTION_ID_KEY,
//                            lastQuestionId
//                        )
//                        intent.putExtra(
//                            SendResponseToMasterActivity.GROUP_ID_KEY,
//                            activityViewModel.currentGroup.value!!.id
//                        )
//                        intent.putExtra(SendResponseToMasterActivity.LEADER_NAME_KEY, name)
//                        intent.putExtra(SendResponseToMasterActivity.LEADER_IMG_KEY, img)
//
//                        startActivity(intent)
//                    }
//
//
////                    val bundle = bundleOf(SendResponseToMasterFragment.QUESTION_ID_KEY to lastQuestionId)
////                    navigate(R.id.action_groupContactFragment_to_sendResponseToMasterFragment, bundle)
//                }
//
//            }
//        }
//        setupMembersRV()
//        binding.rvChat.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//            if (bottom < oldBottom) {
//                Log.i(TAG, "CONDITION=TRUE")
//                binding.rvChat.postDelayed(Runnable() {
//
//                    binding.rvChat.scrollToPosition(messageAdapter.itemCount - 1)
//
//                }, 100);
//            }
//        })
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        activityViewModel.currentGroup.value?.let {
//            setupHeaderTitle(it.name)
//            setupHeaderDetailsTitle(it.membersCount.toString())
//        }
//
//        viewModel.networkState.observe(viewLifecycleOwner, Observer {
//            if (it == AuthNetworkState.LOADING) {
//                showProgressDialog()
//            } else {
//                when (it) {
//                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
//                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
//                    else -> {
//                    }
//                }
//                hideProgressDialog()
//
//            }
//        })
//
//        // Used to track network state when sending an answer to the server
//        viewModel.sendAnswerNetworkState.observe(viewLifecycleOwner, Observer {
//            if (it == AuthNetworkState.LOADING) {
//                sendRecordDialog.showLoadingState()
//            } else {
//                when (it) {
//                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
//                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
//                    else -> {
//                    }
//                }
//                sendRecordDialog.hide()
//
//            }
//        })
//
////        viewModel.sendAnswerRes.observe(viewLifecycleOwner, Observer {
////            it?.let {
////                toastSmall("Answer was sent")
////            }
////        })
//
//        viewModel.uploadFileRes.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                //get url of the file
//                //create new message and set the url inside
//
//                val jsonObject = JSONObject()
//                try {
//
////                    val chat = ChatItem(
////                        id = Random(1000000).nextInt(),
////                        img = it.path,
////                        msg = null,
////                        record = null,
////                        room = activityViewModel.currentGroup.value?.id!!,
////                        senderId = getUser()!!.id,
////                        senderImage = getUser()!!.photoUrl.toString(),
////                        senderName = getUser()!!.name.toString(),
////                        type = CHAT_ITEM_TYPE_IMAGE,
////                        null,
////                        null,
////                        null,
////                        null
////                    )
//
//                    jsonObject.put("room", activityViewModel.currentGroup.value?.id)
//                    jsonObject.put("senderId", getUser()?.id)
//                    jsonObject.put("senderName", getUser()?.id)
//                    jsonObject.put("senderImage", getUser()?.photoUrl)
//                    val fileType: String
//                    when (lastFileType) {
//                        MessageType.IMAGE -> {
//                            fileType = CHAT_ITEM_TYPE_IMAGE
//                            jsonObject.put("img", it.path)
//
//                        }
//                        MessageType.VIDEO -> {
//                            fileType = CHAT_ITEM_TYPE_VIDEO
//                            jsonObject.put("video", it.path)
//
//                        }
//                        MessageType.AUDIO->{
//                            fileType = CHAT_ITEM_TYPE_RECORD
//                            jsonObject.put("record", it.path)
//                        }
//                        else -> fileType = ""
//                    }
//                    jsonObject.put("type", fileType)
//                    // TODO: 6/20/2021
////                    val json = Gson().toJson(chat)
//
//                    changeViewsBeforeSend()
//
//                    Log.i(TAG, "socket_send_video=${jsonObject}")
//
//                    mSocket!!.emit(EVENT_SEND_MESSAGE, jsonObject, Ack {
//                        Log.e(TAG, "send_file_ackData=${it.toString()}")
//                        requireActivity().runOnUiThread {
//
//                            if (messageAdapter.itemCount == 0) { //The first message in chat
//                                binding.rlNoMessages.hide()
//                            }
//
////                            addItem(chat)
//
//                            changeViewsAfterSend()
//
//                        }
//
//                    })
//                    Log.i(TAG, "socket_emit_event= sendMessage,data=$jsonObject")
//
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//
////                if (it.status) { // If the document file was uploaded successfully, continue to upload the image
////                    activityViewModel.setPickedFileId(it.data.id) //Save the doc_id in the viewModel
////                    val file = Util.getFile(pickedImg!!, requireContext())
////                    file?.let { it2 ->
////                        val fileToSend = getFileToSend(it2)
////                    }
////                }
//            }
//        })
//
//    }
//
//
//    private fun initializeView() {
//        messageEdit = binding.etChat
//        sendBtn = binding.btnSendMsg
//        pickImgBtn = binding.btnPickImg
//        messageAdapter = MessageAdapter(requireContext(), ArrayList(0), textQuestionClickListener)
//        binding.rvChat.adapter = messageAdapter
//        binding.rvChat.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//
//        messageEdit!!.addTextChangedListener(this)
//        sendBtn!!.setOnClickListener { v: View? ->
//            if (isConnected) {
////                attemptSendMsg()
//
//                val jsonObject = JSONObject()
//                try {
//
//                    val msgId = Random(10000).nextInt()
//
//                    val chat = ChatItem(
//                        msgId,
//                        null,
//                        messageEdit!!.text.trim().toString(),
//                        null,
//                        activityViewModel.currentGroup.value?.id!!,
//                        getUser()!!.id,
//                        getUser()!!.photoUrl.toString(),
//                        getUser()!!.name.toString(),
//                        CHAT_ITEM_TYPE_TEXT,
//                        null,
//                        null,
//                        null,
//                        null
//                    )
//
//                    jsonObject.put("room", activityViewModel.currentGroup.value?.id)
//                    jsonObject.put("senderId", getUser()?.id)
//                    jsonObject.put("senderName", getUser()?.name)
//                    jsonObject.put("senderImage", getUser()?.photoUrl)
//                    jsonObject.put("type", CHAT_ITEM_TYPE_TEXT)
//                    jsonObject.put("msg", messageEdit!!.text.trim().toString())
//
////                    val json = Gson().toJson(chat)
//
//                    changeViewsBeforeSend()
//
//                    mSocket!!.emit(EVENT_SEND_MESSAGE, jsonObject, Ack {
//
//                        requireActivity().runOnUiThread {
//
//                            changeViewsAfterSend()
//
//                            val result = it[0] as Int
//                            if (result == 200) {
////                            addItem(chat)
//                                if (messageAdapter.itemCount == 0) { //The first message in chat
//                                    binding.rlNoMessages.hide()
//                                }
//
//                            } else {
//                                toastSmall(R.string.network_error)
//                            }
//
//                        }
//
//                    })
//                    Log.i(TAG, "socket_emit_event= sendMessage,data=$jsonObject")
//
//
//                } catch (e: Exception) {
//                    Log.e(TAG, "error_parsing=" + e.message!!)
//
//                    when (e) {
//                        is JSONException, is ClassCastException -> {
//                            toastSmall(R.string.error_parsing_server_response) // It may happen when cast the result(200) fails
//                        }
//                        else -> {
//                            toastFailure(R.string.unknown_error)
//
//                        }
//                    }
//
////                    return@Runnable
//                }
//            } else {
//                toastSmall(R.string.connection_error)
//            }
//
//        }
//        pickImgBtn!!.setOnClickListener { v: View? ->
//            pickGalleryImages()
//        }
//        binding.btnRecord.setOnClickListener {
//            startRecordingProcess()
//        }
//    }
//
//    private fun setupMembersRV() {
//        // Set the adapter
//
//        val members = getGroupMembersFromViewModel()
//        Log.i(TAG, "all_members=${members.toString()}")
//
//        val membersRV = binding.rvMembers
//        with(membersRV) {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            onlineMembersAdapter = OnlineMembersRecyclerViewAdapter(
//                members,
//                requireContext()
//            )
//            adapter = onlineMembersAdapter
//        }
//    }
//
//    private fun getGroupMembersFromViewModel(): List<GroupMember> {
//        val members = ArrayList<GroupMember>()
//        activityViewModel.groupMembers?.let {
//            for (item in it) {
//                val member = GroupMember(item.id, item.fullName, item.picture, false)
//                members.add(member)
//            }
//        }
//        return members.toList()
//
//    }
//
//    fun changeViewsBeforeSend() {
//        binding.relativeSend.hide()
//        binding.shimmerSend.show()
//    }
//
//    fun changeViewsAfterSend() {
//        binding.relativeSend.show()
//        binding.shimmerSend.hideGone()
//    }
//
//
//    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//    }
//
//    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//    }
//
//    override fun afterTextChanged(s: Editable?) {
//        val string = s.toString().trim()
//
//        if (string.isEmpty()) {
//            resetMessageEdit()
//        } else {
//            sendBtn?.show()
//            pickImgBtn?.hide()
//        }
//    }
//
//    private fun resetMessageEdit() {
//        messageEdit!!.removeTextChangedListener(this)
//        messageEdit!!.setText("")
//        sendBtn!!.hide()
//        pickImgBtn!!.show()
//        messageEdit!!.addTextChangedListener(this)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
//        // If the image capture activity was called and was successful
//        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                val items =
//                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)!!
//                for (item in items) {
//                    item?.let {
//                        Log.i(TAG, "FILE_TO_UPLOAD=$item")
//                        Log.i(TAG, "file_type=${Util.getFileMimeType(it, requireContext())}")
//                        lastFileType = Util.getFileMimeType(it, requireContext())
//                        val file = Util.getFile(it, requireContext())
//
//                        file?.let { fileData ->
//                            val fileToSend = getFileToSend(fileData)
//                            viewModel.uploadFile(fileToSend, "image")
//                        }
//                    }
//                }
//            } else {
//                super.onActivityResult(requestCode, resultCode, data)
//
//            }
//        }
//
//
////        if (requestCode == IMAGE_REQUEST_ID && resultCode == Activity.RESULT_OK) {
////            try {
////                val isi: InputStream? =
////                    requireContext().getContentResolver().openInputStream(data!!.data!!)
////                val image = BitmapFactory.decodeStream(isi)
////                sendImage(image)
////            } catch (e: FileNotFoundException) {
////                e.printStackTrace()
////            }
////        }
//    }
//
//    fun getFileToSend(file: File): MultipartBody.Part {
//        val requestFile =
//            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//        // MultipartBody.Part is    used to send also the actual file name
//        val body = MultipartBody.Part.createFormData("file", file.getName(), requestFile)
//
//        return body
//    }
//
////    fun processFetchedImage(uri: Uri) {
////        try {
////            val isi: InputStream? =
////                requireContext().contentResolver.openInputStream(uri)
////            val image = BitmapFactory.decodeStream(isi)
////            sendImage(image)
////        } catch (e: FileNotFoundException) {
////            e.printStackTrace()
////        }
////
////    }
//
////    private fun sendImage(image: Bitmap) {
////        val outputStream = ByteArrayOutputStream()
////        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
////        val base64String = Base64.encodeToString(
////            outputStream.toByteArray(),
////            Base64.DEFAULT
////        )
////        val jsonObject = JSONObject()
////        try {
////            jsonObject.put("userId", getUser()?.id)
////            jsonObject.put("type", CHAT_ITEM_TYPE_IMAGE)
////            jsonObject.put("img", base64String)
////            mSocket!!.emit(EVENT_SEND_MESSAGE, jsonObject)
////            Log.i(TAG, "socket_emit_event= sendMessage")
////
////
////        } catch (e: JSONException) {
////            e.printStackTrace()
////        }
////    }
//
//    private val onConnect = Emitter.Listener {
//        requireActivity().runOnUiThread {
//            Log.i(TAG, "SOCKET_onConnect")
//            Log.i(TAG, "SOCKET_isConnected=$isConnected")
//
//            if (!isConnected) {
//                toastSuccess(R.string.socket_connect_success)
//                attemptJoin()
//                initializeView()
//                isConnected = true
//            }
//        }
//    }
//
//    private val onDisconnect = Emitter.Listener {
//        requireActivity().runOnUiThread {
//            Log.i(TAG, "socket_diconnected")
//            isConnected = false
//        }
//    }
//
//    private val onConnectError = Emitter.Listener {
//        requireActivity().runOnUiThread {
//            Log.e(TAG, "socket_onConnectionError, event=${Socket.EVENT_CONNECT_ERROR}")
//            toastFailure(R.string.connection_error)
//            binding.rvProgress.hide()
//            binding.relativeSend.show()
//            binding.shimmerSend.hideGone()
//        }
//    }
//
//    private fun attemptJoin() {
//
//        activityViewModel.currentGroup.value?.let {
//            val obj = JSONObject()
//            obj.put("room", it.id)
////            obj.put("name", getUser()?.name)
////            obj.put("email", getUser()?.email)
//            obj.put("userID", getUser()?.id)
//
////            mSocket!!.emit(EVENT_JOIN, obj, onUserJoined)
//
//            mSocket!!.emit(EVENT_JOIN, obj, Ack {
////                val ackData = it[0]
//                Log.e(TAG, "join_ackData=$it")
//
//            })
//            Log.i(TAG, "socket_emit_event= join")
//
//        }
//    }
//
////    private fun attemptSendMsg(obj: JSONObject, chatItem: ChatItem, callBack: () -> Unit) {
////        mSocket!!.emit(EVENT_SEND_MESSAGE, obj, callBack)
////
////    }
//
//    private val onUserJoined =
//        Emitter.Listener { args ->
//            requireActivity().runOnUiThread(Runnable {
//                Log.i(TAG, "socket_user_joined")
//            })
//        }
//
//    private val onMessageReceived =
//        Emitter.Listener { args ->
//            requireActivity().runOnUiThread(Runnable {
//                val data = args[0] as JSONObject
//                Log.i(TAG, "socket_message=${data.toString()}")
//                try {
//
//                    val chatItem = parseSingleChat(data)
//
//                    if (messageAdapter.itemCount == 0) { //The first message in chat
//                        binding.rlNoMessages.hide()
//                    }
//
//                    addItem(chatItem)
////                    }
//                } catch (e: JSONException) {
//                    Log.e(TAG, "error_parsing=" + e.message!!)
//                    toastFailure(R.string.error_parsing_server_response)
//                    return@Runnable
//                }
//
//            })
//        }
//
//    private val onOptionChanged =
//        Emitter.Listener { args ->
//            requireActivity().runOnUiThread(Runnable {
//                Log.i(TAG, "socket_onOptionChanged=${args[0].toString()}")
//                try {
//                    val data = args[0] as JSONObject
//                    val questionId = data.getInt("questionId")
//                    val optionId = data.getInt("optionId")
//                    val userId = data.getInt("userId")
//
//                    messageAdapter.changeItemAnswer(questionId, optionId, userId)
//
//                } catch (e: JSONException) {
//                    Log.e(TAG, "error_parsing=" + e.message!!)
//                    toastFailure(R.string.error_parsing_server_response)
//                    return@Runnable
//                }
//
//            })
//        }
//
////    private val onMessageSent =
////        Emitter.Listener { args ->
////            requireActivity().runOnUiThread(Runnable {
////                val data = args[0] as JSONObject
////                Log.i(TAG, "socket_message_sent_callback=${data.toString()}")
////            })
////        }
//
//
//    private val onUserListChanged =
//        Emitter.Listener { args ->
//            requireActivity().runOnUiThread(Runnable {
//                Log.i(TAG, "socket_onUserListChanged")
//                try {
//                    val data = args[0] as JSONArray
//                    Log.i(TAG, "socket_onUserListChanged=$data")
//                    chatUsers = parseOnlineUsersIdsList(data)
//                    Log.i(TAG, "chat_users=$chatUsers")
////                    onlineMembersAdapter.swapData(chatUsers)
//                    onlineMembersAdapter.setOnlineMembers(chatUsers)
//                    // TODO: 6/12/2021
//
//                } catch (e: JSONException) {
//                    Log.e(TAG, "error_parsing=" + e.message!!)
//                    toastFailure(R.string.error_parsing_server_response)
//                    return@Runnable
//                }
//            })
//        }
//
//    private val onLastChatsFetched =
//        Emitter.Listener { args ->
//            requireActivity().runOnUiThread(Runnable {
//                Log.i(TAG, "socket_onLastChatsFetched=${args[0]}")
//
//                binding.rvProgress.hide()
//
//                try {
//                    val data = args[0] as JSONArray
//                    Log.i(TAG, "socket_onLastChatsFetched=$data")
//                    chats.clear()
//                    chats.addAll(parsChatsList(data))
////                    Log.i(TAG, "socket_onLastChatsFetched=$chats")
//
//                    if (chats.size == 0) {
//                        binding.rlNoMessages.show()
//                        messageAdapter.swapData(listOf())
//                    } else {
//                        binding.rlNoMessages.hide()
//                        messageAdapter.swapData(chats)
//                        binding.rvChat.scrollToPosition(messageAdapter.itemCount - 1)
//
//                    }
//
//                } catch (e: Exception) {
//                    Log.e(TAG, "error_parsing=" + e.message!!)
//
//                    when (e) {
//                        is JSONException, is ClassCastException -> {
//                            toastSmall(R.string.error_parsing_server_response) // It may happen when cast the result(200) fails
//                        }
//                        else -> {
//                            toastFailure(R.string.unknown_error)
//
//                        }
//                    }
//
//                    return@Runnable
//                }
//
//            })
//        }
//
//    @Throws(JSONException::class)
//    fun parseOnlineUsersIdsList(ar: JSONArray): List<ChatUser> {
//        val users = ArrayList<ChatUser>()
//        for (i in 0 until ar.length()) {
//            val obj = ar.getInt(i)
//            val user = ChatUser(obj)
//            users.add(user)
//        }
//        return users
//    }
//
//    @Throws(JSONException::class)
//    fun parsChatsList(ar: JSONArray): List<ChatItem> {
//        val chats = ArrayList<ChatItem>()
//        for (i in 0 until ar.length()) {
//            val obj = ar.getJSONObject(i)
//            val chatItem = parseSingleChat(obj!!)
//            chats.add(chatItem)
//        }
//        return chats
//    }
//
//    @Throws(JSONException::class)
//    fun parseSingleChat(obj: JSONObject): ChatItem {
////        val id: Int = obj.getInt("id")
//        val senderId: Int = obj.getInt("senderId")
//        val senderName: String = obj.getString("senderName")
//        val senderImg = obj.optString("senderImage", "2021-06-24T04:28:40.370Z-android.png")
//        val type: String = obj.getString("type")
//        val msg: String = obj.optString("msg")
//        val room = obj.getInt("room")
//        val img: String = obj.optString("img")
//        val video: String = obj.optString("video")
//        val record :String = obj.optString("record")
//        val question = if (type == CHAT_ITEM_TYPE_QUESTION) {
//            parseSingleQuestion(obj.getJSONObject("Question"))
//        } else null
//
//        val chat = ChatItem(
//            id,
//            img,
//            msg,
//            record,
//            room,
//            senderId,
//            senderImg,
//            senderName,
//            type,
//            video,
//            question,
//            null,
//            null
//        )
//
//        return chat
//    }
//
//    @Throws(JSONException::class)
//    fun parseSingleQuestion(obj: JSONObject): Question {
//        val id: Int = obj.getInt("id")
//        val type: String = obj.getString("type")
//        val body: String = obj.optString("body")
//        val optionsArray = obj.optJSONArray("options")
//        val options = if (optionsArray == null) null else parseOptionsArray(optionsArray)
//
//        val question = Question(id, type, body, options)
//
//        //save question id for later use in sendResponseToMaster
//        lastQuestionId = id
//
//        return question
//    }
//
//    @Throws(JSONException::class)
//    fun parseOptionsArray(ar: JSONArray): List<Option> {
//
//        val options = ArrayList<Option>()
//
//        for (i in 0 until ar.length()) {
//            val item = ar.getJSONObject(i)
//            val id = item.getInt("id")
//            val body = item.getString("body")
//            val counter = item.getInt("counter")
//            val isSelected = item.getBoolean("selected")
//
//            val opt = Option(id, body, counter, isSelected)
//            options.add(opt)
//        }
//
//        return options
//    }
//
//
//    private fun addItem(item: ChatItem) {
//        messageAdapter.addItem(item)
//        binding.rvChat.smoothScrollToPosition(messageAdapter.itemCount - 1)
//        resetMessageEdit()
//    }
//
//    val textQuestionClickListener = object : TextQuestionClickListener {
//        override fun onQuestionClicked(
//            question: Question,
//            option: Option,
//            itemPos: Int,
//            optionPos: Int,
//            callBack: (itemPos: Int, optionPos: Int, wasSelected: Boolean) -> Unit
//        ) {
////            sendTextAnswerDialog.setQuestionId(question)
////            sendTextAnswerDialog.show()
//
//            sendAnswer(question, option.id, itemPos, optionPos, callBack)
//
//        }
//    }
//
//    /**
//     * Send an answer selected from the radio group in the received question message
//     */
//    fun sendAnswer(
//        question: Question,
//        optionId: Int,
//        itemPos: Int,
//        optionPos: Int,
//        callBack: (itemPos: Int, optionPos: Int, wasSelected: Boolean) -> Unit
//    ) {
//        activityViewModel.currentGroup.value?.let {
//            val jsonObject = JSONObject()
//            jsonObject.put("groupId", it.id)
//            jsonObject.put("userId", getUser()?.id)
//            jsonObject.put("optionId", optionId)
//            jsonObject.put("questionId", question.id)
//
//            Log.i(TAG, "socket_emit_event= selectOption,data=$jsonObject")
//
//            mSocket!!.emit(EVENT_SELECT_OPTION, jsonObject, Ack {
//
//                Log.i(TAG, "socket_ack=${it[0]}")
//
//                requireActivity().runOnUiThread {
//
//                    try {
//
//                        val result = it[0] as Boolean
//                        if (result) {
//
//                            if (messageAdapter.itemCount == 0) { //The first message in chat
//                                binding.rlNoMessages.hide()
//                            }
//
//                        } else {
//                            toastSmall(R.string.network_error)
//
//                        }
//                        callBack.invoke(itemPos, optionPos, result)
//
//                    } catch (e: ClassCastException) {
//                        toastSmall(R.string.error_parsing_server_response) // It may happen when cast the result(200) fails
//                        callBack.invoke(itemPos, optionPos, false)
//
//                    }
//
//
//                }
//            })
//        }
//
//    }
//
//    inner class RecordAudioDialog : AlertDialog(requireContext()) {
//
//        lateinit var tvRecord: TextView
//        lateinit var ibRecord:ImageButton
//        lateinit var btnSend: Button
//        lateinit var progress: ProgressBar
//        lateinit var indicator :RisingVoiceIndicator
//
//        private var isRecording = false
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//
//            val dialogView = View.inflate(context, R.layout.dialog_send_record, null)
//            setView(dialogView)
//
//            tvRecord = dialogView.findViewById(R.id.tvRecord)
//            ibRecord = dialogView.findViewById(R.id.btnRecordAction)
//            progress = dialogView.findViewById(R.id.pbSend)
//            btnSend = dialogView.findViewById(R.id.btnSend)
//            indicator = dialogView.findViewById(R.id.voice_indicator)
//
//
//            ibRecord.setOnClickListener {
//                if(isRecording){ // Previously recording, stop
//                    tvRecord.setText(R.string.recordAudioLabel)
//                    ibRecord.setImageResource(R.drawable.ic_mic_big)
//                    indicator.stop()
//                    isRecording= false
//                    btnSend.show()
//                    stopRecording()
//                }else{ // Not recording, start
//                    tvRecord.setText(R.string.recordAudioLabel2)
//                    ibRecord.setImageResource(R.drawable.ic_mic_off)
////                    indicator.start()
//                    indicator.start(VoiceIndicator.START_SYSTEM)
//                    isRecording = true
//                    btnSend.hide()
//                    startRecording()
//                }
//            }
//
//            btnSend.setOnClickListener {
//                fileName?.let {
//                    dismiss()
//                    btnSend.hide()
//                    val file = File(it)
//                    val fileToSend = getFileToSend(file)
//                    lastFileType = MessageType.AUDIO
//                    viewModel.uploadFile(fileToSend, "record")
//                }
//            }
//
//            super.onCreate(savedInstanceState)
//
//        }
//
//        override fun onStop() {
//            super.onStop()
//            isRecording =false
//            btnSend.hide()
//
//        }
//
//        fun showLoadingState() {
//            setCancelable(false)
//            btnSend.hide()
//            progress.show()
//        }
//
//        fun hideLoadingState() {
//            setCancelable(true)
//            btnSend.show()
//            progress.hide()
//        }
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        recorder?.release()
//        recorder = null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        mSocket!!.disconnect()
//
//        mSocket!!.off(Socket.EVENT_CONNECT, onConnect)
//        mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
//        mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
//
//        mSocket!!.off(EVENT_RECEIVE_MESSAGE, onMessageReceived)
//        mSocket!!.off(EVENT_USERS_LIST, onUserListChanged)
//        mSocket!!.off(EVENT_LAST_CHATS, onLastChatsFetched)
//        mSocket!!.off(EVENT_CHANGE_OPTION, onOptionChanged)
//    }
//
//    companion object {
//
//        const val EVENT_JOIN = "join"
//        const val EVENT_SEND_MESSAGE = "sendMessage"
//        const val EVENT_RECEIVE_MESSAGE = "receiveMessage"
//        const val EVENT_USERS_LIST = "usersList"
//        const val EVENT_LAST_CHATS = "lastChat"
//        const val EVENT_SELECT_OPTION = "selectOption"
//        const val EVENT_CHANGE_OPTION = "changeOption"
//
//        const val CHAT_ITEM_TYPE_KEY = "chat_type"
//
//        const val CHAT_ITEM_TYPE_IMAGE = "image"
//        const val CHAT_ITEM_TYPE_TEXT = "text"
//        const val CHAT_ITEM_TYPE_VIDEO = "video"
//        const val CHAT_ITEM_TYPE_RECORD = "record"
//
//
//        //GENERAL TYPE OF THE MESSAGE
//        const val CHAT_ITEM_TYPE_QUESTION = "question"
//        const val QUESTION_TYPE_SINGLE = "single"
//        const val QUESTION_TYPE_MULTI = "multi"
//
//    }
//}