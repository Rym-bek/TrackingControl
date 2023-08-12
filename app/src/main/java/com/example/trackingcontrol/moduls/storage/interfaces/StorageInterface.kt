package com.example.trackingcontrol.moduls.storage.interfaces

import android.net.Uri
import com.example.trackingcontrol.utils.Resource

interface StorageInterface {
    suspend fun uploadProfilePhoto(imageUri: Uri): Resource<String>
}