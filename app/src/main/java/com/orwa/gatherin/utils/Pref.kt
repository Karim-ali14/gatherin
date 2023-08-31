package com.orwa.gatherin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

import androidx.preference.PreferenceManager
import com.orwa.gatherin.di.NetworkModule

object Pref {
    private var mSP: SharedPreferences? = null

    @Synchronized
    fun getInstance(ctx: Context): SharedPreferences {
        if (mSP == null) {
            mSP = PreferenceManager.getDefaultSharedPreferences(ctx)
        }
        return mSP!!
    }

//    fun testSP(ctx: Context){
//        val sp = getInstance(ctx)
//        sp.edit().putBoolean("is_test",true).commit()
//    }

    fun getUserInfo(ctx: Context): User? {
        val sp = getInstance(ctx)
        val id = sp.getInt(Constants.USER_ID_KEY,-1)
        val type = sp.getString(Constants.USER_TYPE_KEY,null)
        val name = sp.getString(Constants.USER_FULL_NAME_KEY, null)
        val email = sp.getString(Constants.USER_EMAIL_KEY, null)
        val token = sp.getString(Constants.USER_TOKEN_KEY, null)
        val refreshToken = sp.getString(Constants.USER_REFRESH_TOKEN_KEY, null)
        val photoUrl = sp.getString(Constants.USER_PHOTO_KEY, null)
        val expiresAt = sp.getString(Constants.USER_EXPIRES_AT, null)
        val userName = sp.getString(Constants.USER_NAME_KEY, null)
        val password = sp.getString(Constants.USER_PASSWORD_KEY, null)
        val gender = sp.getInt(Constants.USER_GENDER_KEY, -1)
        val birthday = sp.getString(Constants.USER_BIRTH_DAY_KEY, null)
        val address = sp.getString(Constants.USER_ADDRESS_KEY, null)
        val phone = sp.getString(Constants.USER_PHONE_KEY, null)
        return if (id != -1 && token != null) {
            User(
                id,
                type,
                name,
                email,
                token,
                refreshToken,
                photoUrl,
                expiresAt = expiresAt,
                userName = userName,
                password = password,
                gender = gender,
                birthday = birthday,
                address = address,
                phone = phone
            )
        } else
            null
    }

    @SuppressLint("ApplySharedPref")
    fun saveUserInfo(ctx: Context, user: User?) {
//        Log.i("PREF","save_user_info")
        if (user != null) {
//            Log.i("PREF","inside_save_user_info")

            val editor = getInstance(ctx).edit()
            editor.putInt(Constants.USER_ID_KEY, user.id)
            editor.putString(Constants.USER_TYPE_KEY,user.type)
            editor.putString(Constants.USER_FULL_NAME_KEY, user.name)
            editor.putString(Constants.USER_EMAIL_KEY, user.email)
            editor.putString(Constants.USER_TOKEN_KEY, user.token)
            editor.putString(Constants.USER_REFRESH_TOKEN_KEY, user.refreshToken)
            editor.putString(Constants.USER_PHOTO_KEY, user.photoUrl)
            editor.putString(Constants.USER_EXPIRES_AT, user.expiresAt)

            editor.putString(Constants.USER_NAME_KEY, user.userName)
            editor.putString(Constants.USER_PASSWORD_KEY, user.password)

            editor.commit()
        }
    }

    fun updateUserInfo2(ctx: Context, token: String, refreshToken: String) {
        val editor = getInstance(ctx).edit()
        editor.putString(Constants.USER_TOKEN_KEY, token)
        editor.putString(Constants.USER_REFRESH_TOKEN_KEY, refreshToken)
        editor.commit()
    }

    fun updateUserInfo(ctx: Context, name:String, profileImg:String) {
        val editor = getInstance(ctx).edit()
        editor.putString(Constants.USER_FULL_NAME_KEY, name)
        editor.putString(Constants.USER_PHOTO_KEY, profileImg)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
            /**
             * Delete everything except un, pw, remember me option
             */
    fun deleteUserInfo(ctx: Context) {
        val sp = getInstance(ctx)
        val editor = sp.edit()
        val un = sp.getString(Constants.USER_NAME_KEY, null)
        val pw = sp.getString(Constants.USER_PASSWORD_KEY, null)
//        val rememberMe = sp.getBoolean(Constants.REMEMBER_ME_AUTH_KEY, false)
        editor.clear()
        editor.putString(Constants.USER_NAME_KEY, un)
        editor.putString(Constants.USER_PASSWORD_KEY, pw)
//        editor.putBoolean(Constants.REMEMBER_ME_AUTH_KEY, rememberMe)
        editor.commit()
    }

    fun clearUserData(ctx:Context){

        val sp = getInstance(ctx)
        val editor = sp.edit()
        val firebaseToken = sp.getString(Constants.FIREBASE_CURRENT_TOKEN_KEY,null)
        editor.clear()
        editor.commit()
        firebaseToken?.let {
            editor.putString(Constants.FIREBASE_CURRENT_TOKEN_KEY,it)
            editor.commit()
        }
        NetworkModule.removeAuth()
//        val token = sp.getString(Constants.USER_TOKEN_KEY, null)
//        Log.i("Pref","TOKEN=$token")
    }

    fun saveAuthRememberMe(ctx: Context, rememberMe: Boolean) {
        val editor = getInstance(ctx).edit()
//        editor.putBoolean(Constants.REMEMBER_ME_AUTH_KEY, rememberMe)
        editor.commit()
    }

//    fun getAuthRememberMe(ctx: Context): Boolean {
//        return getInstance(ctx).getBoolean(Constants.REMEMBER_ME_AUTH_KEY, false)
//    }

    fun getSavedUserName(ctx: Context): String? {
        return getInstance(ctx).getString(Constants.USER_NAME_KEY, null)
    }

    fun getSavedPassword(ctx: Context): String? {
        return getInstance(ctx).getString(Constants.USER_PASSWORD_KEY, null)
    }
}
