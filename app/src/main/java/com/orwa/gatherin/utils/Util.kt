package com.orwa.gatherin.utils

import android.content.*
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.orwa.gatherin.R
import com.orwa.gatherin.fcm.SendFirebaseTokenService
import com.orwa.gatherin.present.common.contact.MessageType
import com.mobsandgeeks.saripaar.ValidationError
import com.taishi.flipprogressdialog.FlipProgressDialog
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Util {
    companion object {
        fun showValidationErrorMessagesForFields(errors: List<ValidationError>, context: Context) {
            for (error in errors) {
                val view = error.view
                val message = error.getCollatedErrorMessage(context)

                // Display error messages
                if (view is EditText) {
                    view.error = message
                }
            }
            val validationFailMsg = context.getString(R.string.validation_failed)
            Toast.makeText(context, validationFailMsg, Toast.LENGTH_LONG).show()
        }

        fun createProgressDialog(): FlipProgressDialog {
            // Set imageList
            // Set imageList
            val imageList: MutableList<Int> = ArrayList()
//            imageList.add(R.drawable.ic_favorite_border_white_24dp)
//            imageList.add(R.drawable.ic_favorite_white_24dp)

            imageList.add(R.drawable.logo)

            val fpd = FlipProgressDialog()

            fpd.setImageList(imageList) // *Set a imageList* [Have to. Transparent background png recommended]

            fpd.setCanceledOnTouchOutside(true) // If true, the dialog will be dismissed when user touch outside of the dialog. If false, the dialog won't be dismissed.

            fpd.setDimAmount(0.0f) // Set a dim (How much dark outside of dialog)


// About dialog shape, color

// About dialog shape, color
            fpd.setBackgroundColor(Color.parseColor("#FF4081")) // Set a background color of dialog

            fpd.setBackgroundAlpha(0.2f) // Set a alpha color of dialog

            fpd.setBorderStroke(0) // Set a width of border stroke of dialog

            fpd.setBorderColor(-1) // Set a border stroke color of dialog

            fpd.setCornerRadius(16) // Set a corner radius


// About image

// About image
            fpd.setImageSize(200) // Set an image size

            fpd.setImageMargin(10) // Set a margin of image


// About rotation

// About rotation
            fpd.setOrientation("rotationY") // Set a flipping rotation

            fpd.setDuration(600) // Set a duration time of flipping ratation

            fpd.setStartAngle(0.0f) // Set an angle when flipping ratation start

            fpd.setEndAngle(180.0f) // Set an angle when flipping ratation end

            fpd.setMinAlpha(0.0f) // Set an alpha when flipping ratation start and end

            fpd.setMaxAlpha(1.0f) // Set an alpha while image is flipping


//            fpd.show(getFragmentManager(), "") // Show flip-progress-dialg

//            fpd.dismiss()

            return fpd
        }

        fun createWaitDialog(context: Context): AlertDialog {
            val builder = AlertDialog.Builder(context)
            val dialogView = View.inflate(context, R.layout.progress_wait, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            return builder.create()
        }

        fun showMsgDialog(
            context: Context,
            msgRes: Int,
            clickListener: DialogInterface.OnClickListener? = null
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(msgRes)
            builder.setPositiveButton(
                R.string.ok_btn,
                clickListener
            )
            builder.setCancelable(false)
            builder.create().show()
        }

        /**
         * Show message dialog with 2 buttons (Yes & No)
         */
        fun showOptionMsgDialog(
            context: Context,
            msgRes: Int,
            func: () -> Unit
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(msgRes)
            builder.setPositiveButton(
                R.string.ok_btn, DialogInterface.OnClickListener { dialog, which ->
                    func.invoke()
                }
            )
            builder.setNegativeButton(R.string.cancel_btn, null)
            builder.setCancelable(false)
            builder.create().show()
        }

        /**
         * Show message dialog with 2 buttons (Yes & No)
         */
        fun show1OptionMsgDialog(
            context: Context,
            msgRes: Int,
            func: () -> Unit
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(msgRes)
            builder.setPositiveButton(
                R.string.ok_btn, DialogInterface.OnClickListener { dialog, which ->
                    func.invoke()
                }
            )
            builder.setCancelable(false)
            builder.create().show()
        }

        fun getGroupsNumberLabel(n: Int, ctx: Context): String {
            return ctx.getString(R.string.groups_num_placeholder, n)
        }

        fun getMembersNumberLabel(n: Int, ctx: Context): String {
            return ctx.getString(R.string.members_num_placeholder, n)
        }

        private fun getImagePath(path: String): String {
            return Constants.BASE_LINK + "/" + path
        }

        fun getFullPath(path: String): String {
            return Constants.BASE_LINK + "/" + path
        }


        fun loadImage(ctx: Context, img: ImageView, imgPath: String, errorImg: Int? = null) {
            if (errorImg != null) {
                Glide.with(ctx).load(getImagePath(imgPath)).error(errorImg).into(img)
            } else {
                Glide.with(ctx).load(getImagePath(imgPath)).into(img)
            }
        }

        fun loadImageWithPlaceholder(
            ctx: Context,
            img: ImageView,
            imgPath: String,
            errorImg: Int? = null
        ) {
            if (errorImg != null) {
                Glide.with(ctx).load(getImagePath(imgPath)).placeholder(R.drawable.avatar_1_raster)
                    .error(errorImg).into(img)
            } else {
                Glide.with(ctx).load(getImagePath(imgPath)).placeholder(R.drawable.avatar_1_raster)
                    .into(img)
            }
        }

        fun loadImage(ctx: Context, img: ImageView, imgPath: Uri, errorImg: Int? = null) {
            if (errorImg != null) {
                Glide.with(ctx).load(imgPath).error(errorImg).into(img)
            } else {
                Glide.with(ctx).load(imgPath).into(img)
            }
        }

        fun getFile(uri: Uri, ctx: Context): File? {
            val parcelFileDescriptor = ctx.contentResolver.openFileDescriptor(uri, "r", null)

            parcelFileDescriptor?.let {
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(ctx.cacheDir, getFileName(uri, ctx))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                return file
            }
            return null
        }

        fun getFileName(fileUri: Uri, ctx: Context): String {

            var name = ""
            val returnCursor = ctx.contentResolver.query(fileUri, null, null, null, null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                name = returnCursor.getString(nameIndex)
                returnCursor.close()
            }
            return name
        }

        fun getFileMimeType(fileUri: Uri, ctx: Context): MessageType? {
            /*
 * Get the file's content URI from the incoming Intent, then
 * get the file's MIME type
 */
            val mime = ctx.contentResolver.getType(fileUri)
            mime?.let {
                if (it.startsWith("image")) {
                    return MessageType.IMAGE
                } else if (it.startsWith("video")) {
                    return MessageType.VIDEO
                }
            }
            return null

        }

//        /**
//         * Get correct string value depending on the current language
//         */
//
//        fun getCorrectName(enVal:String, arVal:String, ctx:Context):String{
//            if(getCurrentLang(ctx)== Pref.Lang.AR){
//                return arVal
//            }else{
//                return enVal
//            }
//        }

        /**
         * Creates the temporary image file in the cache directory.
         *
         * @return The temporary image file.
         * @throws IOException Thrown if there is an error creating the file
         */
        @Throws(IOException::class)
        fun createTempImageFileName(context: Context): String? {
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())
            val imageFileName = "RECORD_" + timeStamp + "_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            if (storageDir != null) {
                val finalFilePath = (storageDir.absolutePath + "/" + imageFileName + ".3gp")
//                Log.i("Util", "record_path=" + finalFilePath)
                return finalFilePath
            } else return null

//        Log.v("Bitmap","imageFile absolute path="+imageFile.getAbsolutePath());
//            return File.createTempFile(
//                imageFileName, /* prefix */
//                ".3gp", /* suffix */
//                storageDir      /* directory */
//            )
        }

        fun getCurrentLang(ctx: Context): Lang {
//            Log.i("Util", "CURRENT_DEFAULT_LANG=${Locale.getDefault().language}")
            val defaultLang = Locale.getDefault().language
            val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
            val language = sp.getString(
                Constants.CURRENT_SET_LANGUAGE_KEY,
                defaultLang
            )
            if (language.equals(Constants.ARABIC_LANGUAGE)) {
                return Lang.ARABIC
            } else {
                return Lang.ENGLISH
            }
        }

        fun copyToClipBoard(ctx: Context, text: String) {
            val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(text, text)
            clipboard.setPrimaryClip(clip)
        }

        fun getCurrentFirebaseTokenFromSharedPref(context: Context): String? {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            return sp.getString(Constants.FIREBASE_CURRENT_TOKEN_KEY, null)
        }

        fun saveFirebaseTokenInSharedPref(context: Context, token: String) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString(Constants.FIREBASE_CURRENT_TOKEN_KEY, token)
            editor.putBoolean(Constants.IS_FIREBASE_TOKEN_SYNCED_KEY, false).commit()
        }

//        fun getOldFirebaseTokenFromSharedPref(context: Context): String? {
//            val sp = PreferenceManager.getDefaultSharedPreferences(context)
//            return sp.getString(Constants.FIREBASE_OLD_TOKEN_KEY, null)
//        }

        fun changeFirebaseTokenSyncState(context: Context, state: Boolean) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sp.edit()
            editor.putBoolean(Constants.IS_FIREBASE_TOKEN_SYNCED_KEY, state).commit()
        }

        fun getFirebaseTokenSyncState(context: Context): Boolean {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            return sp.getBoolean(Constants.IS_FIREBASE_TOKEN_SYNCED_KEY, false)
        }

         fun syncFirebaseToken(ctx:Context){ //Only called when the used is logged in
//            Log.i(TAG,"fcm_syncFirebaseToken")
            val isSynced = Util.getFirebaseTokenSyncState(ctx)
            if( !isSynced){ //If the current firebase token was not previously synced
                sendRegistrationToServer(ctx)
            }
        }

        fun sendRegistrationToServer(ctx:Context){
//            Log.i(TAG,"fcm_sendRegistrationToServer")
            val token = Util.getCurrentFirebaseTokenFromSharedPref(ctx)
            token?.let { //Send only when there is a token
//                Log.i(TAG,"fcm_token")

                Intent(ctx, SendFirebaseTokenService::class.java).also { intent ->
                    intent.putExtra(Constants.FIREBASE_TOKEN_TO_SEND_KEY,token)
                    ctx.startService(intent)
                }
            }


        }


    }
}