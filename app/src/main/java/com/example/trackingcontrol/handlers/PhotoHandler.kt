package com.example.trackingcontrol.handlers

import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.trackingcontrol.constants.ConstantsPermissions
import com.example.trackingcontrol.moduls.storage.viewmodels.StorageViewModel
import com.example.trackingcontrol.utils.PermissionsHelper
import javax.inject.Inject

class PhotoHandler @Inject constructor(val fragment: Fragment, storageViewModel: StorageViewModel){
    private var permissionsHelper = PermissionsHelper(fragment, ConstantsPermissions.PERMISSIONS_STORAGE)

    fun onClickPhotoUpdate(view: View) {
        if(permissionsHelper.hasPermissions())
        {
            pickImage()
        }
    }

    private fun pickImage()
    {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickMedia =
        fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                storageViewModel.uploadProfilePhoto(uri)
            }
        }
}