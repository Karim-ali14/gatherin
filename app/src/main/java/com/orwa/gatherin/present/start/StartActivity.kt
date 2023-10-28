package com.orwa.gatherin.present.start

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.orwa.gatherin.MyApplication
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity() {

    private val TAG = StartActivity::class.java.simpleName

    val viewModel:StartActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

//        Pref.testSP(this)
        handleIntent(intent)
//        Log.i(TAG,"CURRENT_DEFAULT_LANG=${Locale.getDefault().language}")

    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { recipeId ->

//                viewModel.sectionCode = recipeId
                MyApplication.SHARE_LINK = recipeId
//                Log.i(TAG, "data=$recipeId")

//                Uri.parse("content://com.recipe_app/recipe/")
//                    .buildUpon()
//                    .appendPath(recipeId)
//                    .build().also { appData ->
//                        showRecipe(appData)
//                    }
            }
        }
    }

}