package com.orwa.gatherin.present.teacher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.orwa.gatherin.R
import com.orwa.gatherin.base.BaseActivity
import com.orwa.gatherin.databinding.ActivityTeacherBottomNavBinding
import com.orwa.gatherin.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TeacherHomeActivity : BaseActivity() {

    private val TAG = TeacherHomeActivity::class.java.simpleName

    val viewModel: MyActivityViewModel by viewModels()

//    private var currentNavController: LiveData<NavController>? = null

    lateinit var binding: ActivityTeacherBottomNavBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupBottomNavigationBar()

//        if (savedInstanceState == null) {
//            setupBottomNavigationBar()
//        } // Else, need to wait for onRestoreInstanceState

        changeBottomNavStatus()

        val user = Pref.getUserInfo(this)
        user?.let {

            viewModel.notificationsRes.observe(this, Observer {
//                Log.i(TAG, "departments=$it")
                if (it != null) {
                    if (it.countUnSeen > 0) { //Some notifications were not read yet
                        setNotificationIconStatus(false)
                    }else{
                        setNotificationIconStatus(true)
                    }
                } else {
                    viewModel.getNotificationsList()
                }
            })
        }

    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        // Now that BottomNavigationBar has restored its instance state
//        // and its selectedItemId, we can proceed with setting up the
//        // BottomNavigationBar with Navigation
//        setupBottomNavigationBar()
//    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navView: BottomNavigationView = binding.bottomNav

        val navController = findNavController(R.id.nav_host_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val navGraphIds = setOf(
            R.navigation.teacher_graph,
            R.navigation.messages,
            R.navigation.notifications,
            R.navigation.profile
        )

        val appBarConfiguration = AppBarConfiguration(
            navGraphIds
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)




//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val d1 = ContextCompat.getDrawable(this, R.drawable.ic_notification)
//            val d2 = ContextCompat.getDrawable(this, R.drawable.notify_dot)
//
//            val horizontalInset: Int =
//                (d1!!.getIntrinsicWidth() - d2!!.getIntrinsicWidth()) / 2
//
//            val finalDrawable = LayerDrawable(arrayOf(d1, d2))
//            finalDrawable.setLayerInset(1, 0, 0, 0, d1.getIntrinsicHeight()/2)
////            finalDrawable.setLayerInset(
////                1,
////                horizontalInset,
////                d1.getIntrinsicHeight(),
////                horizontalInset,
////                0
////            )


//        }



//        // Setup the bottom navigation view with a list of navigation graphs
//        val controller = bottomNavigationView.setupWithNavController(
//            navGraphIds = navGraphIds,
//            fragmentManager = supportFragmentManager,
//            containerId = R.id.nav_host_container,
//            intent = intent
//        )

//        // Whenever the selected controller changes, setup the action bar.
//        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
//        })
//        currentNavController = controller

        //Check if there is an intent coming from the notification bar(FCM related)
        intent?.let {
            val isFromNotification = it.getBooleanExtra(Constants.IS_FROM_NOTIFICATION_KEY, false)
            if (isFromNotification && viewModel.isFirstTime) {
                navView.selectedItemId = navView.menu.getItem(2).itemId
                viewModel.isFirstTime = false
            }

        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return currentNavController?.value?.navigateUp() ?: false
//    }

    private fun changeBottomNavStatus() {
        viewModel.showBottomNav.observe(this, Observer {
//            Log.i(TAG, "BOTTOM_NAV_STATUS_CHANGED=TRUE")
            if (it) {
                binding.bottomNav.show()
            } else {
                binding.bottomNav.hideGone()
            }
        })
    }

    fun setNotificationIconStatus(areAllRead: Boolean) {
        val d = if (!areAllRead) {
            ContextCompat.getDrawable(this, R.drawable.notify_layers)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_notification)
        }
        binding.bottomNav.menu.getItem(2).icon = d

    }
}