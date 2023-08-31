package com.orwa.gatherin.present.student.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.ActivitySendMessageToLeaderBinding
import com.orwa.gatherin.model.group.SendResponseToMasterReq
import com.orwa.gatherin.utils.Util
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.vdx.designertoast.DesignerToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendResponseToMasterActivity : AppCompatActivity(), Validator.ValidationListener {

    private val TAG = SendResponseToMasterActivity::class.java.simpleName

    lateinit var validator: Validator

    lateinit var binding: ActivitySendMessageToLeaderBinding

    private val viewModel: SendResponseToMasterViewModel by viewModels()

    lateinit var waitDialog: AlertDialog

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var responseET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageToLeaderBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            finish()
        }

        responseET = binding.etWriteAnswer

        setContentView(binding.root)

        validator = Validator(this)
        validator.setValidationListener(this)

        waitDialog = Util.createWaitDialog(this)

        intent//get  leader name, image

        val img = intent.getStringExtra(LEADER_IMG_KEY)
        img?.let {
            Util.loadImage(this, binding.ivProfileImage, img)
        }
        val leaderName = intent.getStringExtra(LEADER_NAME_KEY)
        leaderName?.let {
            binding.tvLeaderName.text = it
        }


        viewModel.networkState.observe(this, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                        toastSuccess(R.string.response_to_master_sent_success)
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.sendResToMasterRes.observe(this, Observer {
            if (it != null) {
                setResult(RESULT_OK)
                //popup to previous screen
                finish()
            }
        })

        binding.tvSend.setOnClickListener {
            validator.validate()
        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        Util.showValidationErrorMessagesForFields(errors, this)
    }

    override fun onValidationSucceeded() {

//        hideKeyboardFrom(binding.root)
        val res = responseET.text.trim().toString()

        val questionId = intent.getIntExtra(QUESTION_ID_KEY, -1)
        val groupId = intent.getIntExtra(GROUP_ID_KEY, -1)

        if (questionId != -1 && groupId != -1) {
            val req = SendResponseToMasterReq(
                groupId,
                questionId,
                res
            )
            viewModel.sendResponseToMaster(req)

        }


    }

    fun showProgressDialog() {
        waitDialog.show()
    }

    fun hideProgressDialog() {
        waitDialog.dismiss()
    }

    fun toastSuccess(msgId: Int) {
        val ctx = this
        DesignerToast.Success(
            ctx,
            ctx.getString(R.string.app_name),
            ctx.getString(msgId),
            Gravity.TOP,
            Toast.LENGTH_LONG,
            DesignerToast.STYLE_DARK
        )
    }

    fun toastFailure(msgId: Int) {
        val ctx = this
        DesignerToast.Error(
            ctx,
            ctx.getString(R.string.app_name),
            ctx.getString(msgId),
            Gravity.TOP,
            Toast.LENGTH_LONG,
            DesignerToast.STYLE_DARK
        )

//        KToast.customBackgroudToast((Activity) context, msg, Gravity.TOP, KToast.LENGTH_AUTO, R.drawable.background_toast, null, R.drawable.ic_infinite_white);
    }

    companion object {
        const val QUESTION_ID_KEY = "question_id"
        const val GROUP_ID_KEY = "group_id"
        const val LEADER_IMG_KEY = "leader_img"
        const val LEADER_NAME_KEY = "leader_name"
    }

}