//package com.orwa.gatherin.fcm
//
//import android.app.IntentService
//import android.content.Intent
//import android.util.Log
//import android.widget.Toast
//
//import org.json.JSONException
//import org.json.JSONObject
//import java.net.HttpURLConnection
//import java.util.concurrent.CountDownLatch
//
//class RefreshTokenService : IntentService("RefreshTokenService") {
//    val TAG = RefreshTokenService::class.java.simpleName
//
//    override fun onHandleIntent(p0: Intent?) {
//        val latch = CountDownLatch(1)
//        if (p0 != null) {
//            requestRefreshToken(latch = latch)
//            try {
//                latch.await()
//
//            } catch (e: InterruptedException) {
//                stopSelf()
//            }
//        }
//    }
//
//    private fun requestRefreshToken(latch: CountDownLatch) {
//        val subLink = Constants.SUB_LINK_NOTIFICATIONS + "/" + Constants.SUB_LINK_SAVE_TOKEN
//        val params = getRefreshTokenParams()
//        val link = Utility.buildRequestLinkWithParams(this, subLink, params)
//        Log.v(TAG, "link=$link")
//        Log.i(TAG, "json_params=$params")
//        val request = MyJsonObjectRequest(
//            Request.Method.POST,
//            link,
//            null,
//            Response.Listener { response ->
//                Log.v(TAG, "response=" + response!!)
//                if (processRefreshTokenResponse(response)) {
//                    Utility.changeTokenSyncState(this@RefreshTokenService)
//                }
//                latch.countDown()
//            },
//            Response.ErrorListener { error ->
//                var msg = R.string.connection_error
//                Log.e(TAG, "error=$error")
//                if (error.networkResponse != null) {
//                    val errorMsg = String(error.networkResponse.data)
//                    Log.e(TAG, "sign_in_error=$errorMsg")
//                    val status = error.networkResponse.statusCode
//                    Log.e(TAG, "error_status_code=$status")
//                    val responseBody = String(error.networkResponse.data)
//                    Log.e(TAG, "error_body=$responseBody")
//                    if (status == HttpURLConnection.HTTP_BAD_REQUEST) {
//                        msg = R.string.sign_in_fail_incorrect_credentials
//                    } else {
//                        msg = R.string.unknown_response
//                    }
//                }
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//                latch.countDown()
//            }
//            , this)
//        request.tag = Constants.REFRESH_TOKEN_NETWORK_REQUEST_TAG
//        ConnectToNet.getInstance(this).getRequestQueue().add(request)
//    }
//
//    private fun getRefreshTokenParams(): HashMap<String, String> {
//        val map = HashMap<String, String>()
//
//        //All params keys
//        val OLE_TOKEN_KEY = "oldToken"
//        val NEW_TOKEN_KEY = "newToken"
//
//        val oldToken = Utility.getOldFirebaseTokenFromSharedPref(this)
//        val newToken = Utility.getCurrentFirebaseTokenFromSharedPref(this)
//        oldToken?.let { map[OLE_TOKEN_KEY] = it }
//        newToken?.let { map[NEW_TOKEN_KEY] = it }
//
//        return HashMap(map)
//    }
//
//    /**
//     *
//     * @param response from server
//     * @return true if server response was parsed successfully
//     */
//    private fun processRefreshTokenResponse(response: JSONObject): Boolean {
//        val msg: Int
//
//        return try {
//            true
//        } catch (e: JSONException) {
//            Log.e(TAG, "parsing_error=${e.message}")
//            false
//        }
//    }
//}