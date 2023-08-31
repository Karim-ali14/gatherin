package com.orwa.gatherin.fcm

import android.app.Service
import android.content.Intent
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import com.orwa.gatherin.di.NetworkModule
import com.orwa.gatherin.model.fcm.FirebaseUserTokenReq
import com.orwa.gatherin.rep.FCMRep
import com.orwa.gatherin.utils.Constants
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.*


class SendFirebaseTokenService : Service() {

    lateinit var rep: FCMRep

    private val TAG = SendFirebaseTokenService::class.java.simpleName

    val scope = CoroutineScope(Job() + Dispatchers.IO)

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
//            Log.i(TAG, "service_handleMessage=$msg")
            // Normally we would do some work here, like download a file.
            val user = Pref.getUserInfo(this@SendFirebaseTokenService)
            if(user!=null) {
                val req = FirebaseUserTokenReq(msg.data.getString(TOKEN_PARAM_KEY))
                scope.launch {
                    val res = rep.setFirebaseUserToken(req)
//                    Log.i(TAG, "res=$res")

                    res.onSuccess {
//                        Log.i(TAG, "success_res=$data, status_code=$statusCode")

                        Util.changeFirebaseTokenSyncState(this@SendFirebaseTokenService, true)

                    }

                    res.onError {
                        Log.e(TAG, "error_res=$statusCode")

                        when (statusCode) {
                            StatusCode.Unauthorized -> {
                            }
                            else -> {
                            }
                        }
                    }

                    res.onException {

                        Log.e(
                            TAG, "EXCEPTION_res=$message"
                        )
                    }
                    // Stop the service using the startId, so that we don't stop
                    // the service in the middle of handling another job
//                        stopSelf(msg.arg1)
                    stopSelf()

                }
            } else{
                stopSelf()
            }


        }
    }

    override fun onCreate() {
        super.onCreate()
        rep = initRep()
//        Log.i(TAG, "service_onCreate")
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        Log.i(TAG, "service_onStartCommand")
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
//            Log.i(TAG, "service_obtainMessage")
            msg.arg1 = startId
            msg.data.putString(
                TOKEN_PARAM_KEY,
                intent.getStringExtra(Constants.FIREBASE_TOKEN_TO_SEND_KEY)
            )
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
//        Log.i(TAG, "service_onDestroy")

//        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }

    fun initRep(): FCMRep {
        val service = NetworkModule.provideRetrofitServiceAuthenticated(this)
        val rep = FCMRep(service)
        return rep
    }

    companion object {
        const val TOKEN_PARAM_KEY = "token_param"
    }
}