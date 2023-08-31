package com.orwa.gatherin.fcm


/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log
import com.orwa.gatherin.R
import com.orwa.gatherin.present.student.StudentActivity
import com.orwa.gatherin.present.teacher.TeacherHomeActivity
import com.orwa.gatherin.utils.Constants
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT"></action>
</intent-filter> *
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = "MyFirebaseMsgService"
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "fcm_From: " + remoteMessage.from!!)
//        Log.i(TAG, "fcm_message=$remoteMessage")
        // Check if message contains a data payload.
//        Log.d(TAG, "fcm_Message data payload: " + remoteMessage.data)

//        Log.d(TAG, "fcm_Message notification: " + remoteMessage.notification?.body)


        val notificationData = getNotificationData(remoteMessage.data)
        sendNotification(notificationData)


//        // Check if message contains a notification payload.
//        if (remoteMessage.notification != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

//    fun getNotificationFromType(context: Context): NotificationData {
//        var title: String = context.getString(R.string.notification_title_unknown)
//        var body: String = context.getString(R.string.notification_body_unknown)
//
//                    title = context.getString(R.string.notificatino_new_outlet_title)
//                    body = getNotificationBody(
//                        R.string.notification_new_outlet_assigned,
//                        context,
//                        true
//                    )
//
//        return NotificationData(title, body)
//    }


    private fun getNotificationData(data:Map<String,String>):NotificationData{
        val title = data["title"]
        val body = data["body"]
        return NotificationData(title.toString(),body.toString())
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
//        Log.d(TAG, "fcm_onNewToken=$token")
        //Save new token in SharedPref
//        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        //Current token now will be the old one
//        val oldToken = Util.getCurrentFirebaseTokenFromSharedPref(this)
//        val editor = sp.edit()

        Util.saveFirebaseTokenInSharedPref(this,token)

        sendRegistrationToServer(token)

    }

    fun sendRegistrationToServer(token:String){
        val user = Pref.getUserInfo(this)
        user?.let {
            Intent(this, SendFirebaseTokenService::class.java).also { intent ->
                intent.putExtra(Constants.FIREBASE_TOKEN_TO_SEND_KEY,token)
                startService(intent)
            }
        }


    }



    // [END on_new_token]

//    /**
//     * Schedule async work using WorkManager.
//     */
//    private fun scheduleJob() {
//        // [START dispatch_job]
////        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
////            .build()
////        WorkManager.getInstance(this).beginWith(work).enqueue()
//
//        // [END dispatch_job]
//    }

//    /**
//     * Handle time allotted to BroadcastReceivers.
//     */
//    private fun handleNow() {
//        Log.d(TAG, "Short lived task is done.")
//    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(notifyData: NotificationData) {
        val user = Pref.getUserInfo(this)
        user?.let {
            val activityClass = if(it.type==Constants.USER_TYPE_TEACHER) TeacherHomeActivity::class.java else StudentActivity::class.java
            val intent = Intent(this, activityClass)

            intent.putExtra(Constants.IS_FROM_NOTIFICATION_KEY,true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            val pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val channelId = getString(R.string.default_notification_channel_id)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(notifyData.title)
                .setContentText(notifyData.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }

    }


}