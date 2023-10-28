/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orwa.gatherin.present.teacher.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.orwa.gatherin.BuildConfig
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseValidateTeacherFragment
import com.orwa.gatherin.databinding.FragmentEditProfileBinding
import com.orwa.gatherin.utils.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


/**
 * Shows a profile screen for a user, taking the name from the arguments.
 */
class EditProfileFragment : BaseValidateTeacherFragment(),EasyPermissions.PermissionCallbacks {

    private val TAG = EditProfileFragment::class.java.simpleName

    lateinit var binding: FragmentEditProfileBinding

    private val viewModel: EditProfileViewModel by viewModels()

    private var mAddImages = ArrayList<Uri>()

    private var pickedImg: Uri? = null

    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    @Length(sequence = 2, min = 3, messageResId = R.string.validate_input_field_length_3)
    private lateinit var nameEt: EditText

    companion object{
        const val PHOTO_PERMISSION = 120
    }

//    @NotEmpty(sequence = 1, messageResId = R.string.validate_empty_field)
    private lateinit var phoneEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        nameEt = binding.etUserName
        phoneEt = binding.etUserPhone
//        val user = getUser()
//        user?.let {
//            if(it.type==Constants.USER_TYPE_STUDENT){
//                binding.relativePhone.hideGone()
//            }
//        }

        binding.relativeImage.setOnClickListener {
            pickGalleryImages()
        }

        binding.tvRegister.setOnClickListener {
            validator.validate()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderTitle(getString(R.string.editProfile))

//        binding.ivSettings.setOnClickListener { navigate(R.id.action_profile_to_settingsFragment) }

        //UserInfo is shared with other fragments(settings, profile...) so that it is in ActivityViewModel
        activityViewModel.userInfo.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                if (viewModel.profileImg == null) {
                    Util.loadImage(requireContext(), binding.ivProfileImage, it.picture)
                } else {
                    Util.loadImage(requireContext(), binding.ivProfileImage, viewModel.profileImg!!)
                }
                binding.tvUserName.setText(it.fullName)
                binding.etEmail.setText(it.email)
                binding.etUserName.setText(it.fullName)
                binding.etUserPhone.setText(it.phone)

            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> toastSuccess(R.string.update_profile_success)
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

        viewModel.updateProfileRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val fullName = binding.etUserName.text.trim().toString()
                val phone = binding.etUserPhone.text.trim().toString()
                activityViewModel.updateUserInfo(fullName,phone,it.picture.toString())

                findNavController().popBackStack()
            }

        })
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
//                Log.i("DEBUG_", "${it.key} = ${it.value}")
                if (it.key != null && it.key == Manifest.permission.READ_EXTERNAL_STORAGE && it.value) {
                    launchFilePicker()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.open_file_picker_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

    private fun pickGalleryImages() {
        if (hasPhotoPermission())
            launchFilePicker()
        else
            requestPhotoPermission()
    }

    fun hasPhotoPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE
        )

    fun requestPhotoPermission(){
        EasyPermissions.requestPermissions(
            this,
            "this process cat not work without this permission .",
            PHOTO_PERMISSION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun launchFilePicker() {
        mAddImages = java.util.ArrayList()
        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(mAddImages)
            .setActivityTheme(R.style.FilePickerActivityTheme)
            .pickPhoto(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        Log.i(TAG, "onActivityResult=$data")

        // If the image capture activity was called and was successful
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                val items =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)!!
                for (item in items) {
                    item?.let {
                        //Error placeholder should be set here
//                        activityViewModel.setAttachPickedImg(it)
                        viewModel.profileImg = it
//                        loadImg(it)
                        Util.loadImage(requireContext(),binding.ivProfileImage,it)
                    }

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)

            }
        }
    }

    fun getFileToSend(file: File): MultipartBody.Part {
        val requestFile =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        // MultipartBody.Part is    used to send also the actual file name
        val body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile)

        return body
    }

    override fun onValidationSucceeded() {
        hideKeyboardFrom(binding.root)
        val fullName = binding.etUserName.text.trim().toString()
        val phone = binding.etUserPhone.text.trim().toString()

        if (viewModel.profileImg == null) { //Don't upload image as there is no change
            viewModel.updateProfile(fullName, phone)
        } else {
            val file = Util.getFile(viewModel.profileImg!!, requireContext())
            file?.let {
                val fileToSend = getFileToSend(file)

                val fullNameValue: RequestBody = fullName
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                val phoneValue: RequestBody = phone
                    .toRequestBody("text/plain".toMediaTypeOrNull())

//                val map = HashMap<String,String>()
//                map.put("fullName",fullName)
//                map.put("phone",phone)
                viewModel.updateProfile(fileToSend, fullNameValue, phoneValue)
            }

        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestPhotoPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        launchFilePicker()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}
