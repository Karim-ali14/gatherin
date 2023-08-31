package com.orwa.gatherin.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.orwa.gatherin.R
import com.orwa.gatherin.utils.*
import com.vdx.designertoast.DesignerToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    lateinit var waitDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        waitDialog = Util.createWaitDialog(requireContext())
    }

    fun startActivityWithClear(c: Class<*>) {
        val intent = Intent(requireContext(), c)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    protected fun startActivity(c: Class<*>) {
        val intent = Intent(requireContext(), c)
        startActivity(intent)
    }

    fun navigate(resId: Int, args: Bundle? = null, popUpTo: Int? = null) {
        val navOptions = NavOptions.Builder()
        navOptions.setEnterAnim(R.anim.slide_in_right)
        navOptions.setExitAnim(R.anim.slide_out_left)
        navOptions.setPopEnterAnim(R.anim.slide_in_left)
        navOptions.setPopExitAnim(R.anim.slide_out_right)
        popUpTo?.let {
            navOptions.setPopUpTo(popUpTo, true)
            navOptions.setLaunchSingleTop(true)
        }

        findNavController().navigate(resId, args, navOptions.build())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackButton()
    }

    fun getUser(): User? {
        val user = Pref.getUserInfo(requireContext())
        return user
    }

    fun isTeacherAccount(): Boolean? {
        val user = getUser()
        if (user?.type.equals(Constants.USER_TYPE_TEACHER)) {
            return true
        } else if (user?.type.equals(Constants.USER_TYPE_STUDENT)) {
            return false
        }
        return null
    }

    fun setUpBackButton() {
        val backButton = view?.findViewById<ImageView>(R.id.ivBack)
        backButton?.setOnClickListener {
            findNavController().popBackStack()
            view?.let { it1 -> hideKeyboardFrom(it1) }
        }
    }

    fun setupHeaderTitle(title: String?) {
        val headerTitle = view?.findViewById<TextView>(R.id.header_title)
        headerTitle?.text = title.toString()
    }

    fun setupHeaderDetailsTitle(title: String?) {
        val headerTitle = view?.findViewById<TextView>(R.id.header_title_details)
        headerTitle?.show()
        headerTitle?.text = title.toString()
    }

    fun showProgressDialog() {
        waitDialog.show()
    }

    fun hideProgressDialog() {
        waitDialog.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        waitDialog.dismiss()
    }

    fun toastSuccess(msgId: Int) {
        val ctx = requireContext()
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
        val ctx = requireContext()
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

    fun toastSmall(msgId: Int) {
        Toast.makeText(requireContext(), msgId, Toast.LENGTH_SHORT).show()
    }

    fun toastSmall(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun hideKeyboardFrom(view: View) {
        val ctx = requireContext()
        val imm = ctx.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showRetryConnectionSnackBar(callback: ()->Unit){
        view?.let {
            Snackbar.make(it, R.string.connection_error, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.retry_btn),
                View.OnClickListener {
                    callback()
                }).show()
        }
    }
}