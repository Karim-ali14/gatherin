package com.orwa.gatherin.base

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orwa.gatherin.utils.LocaleHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(base: Context?) {
        val newCtx = LocaleHelper.onAttach(base)
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newCtx))
    }

    protected fun startActivity(c: Class<*>) {
        val intent = Intent(this, c)
        startActivity(intent)
    }

    protected fun startActivityWithClear(c: Class<*>) {
        val intent = Intent(this, c)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun toastShort(msg:Int){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun toastLong(msg:Int){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    }


}