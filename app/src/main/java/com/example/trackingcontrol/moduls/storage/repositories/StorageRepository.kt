package com.example.trackingcontrol.moduls.storage.repositories

import android.net.Uri
import com.example.trackingcontrol.constants.FirebaseConstants
import com.example.trackingcontrol.moduls.storage.interfaces.StorageInterface
import com.example.trackingcontrol.moduls.user.repository.UserRepository
import com.example.trackingcontrol.utils.Resource
import com.example.trackingcontrol.utils.await
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class StorageRepository @Inject constructor(storageDatabase: StorageReference, userRepository: UserRepository) : StorageInterface {

    private val uid = userRepository.uid
    private val storageReference = storageDatabase.child(FirebaseConstants.REF_IMAGES).child(uid)

    override suspend fun uploadProfilePhoto(imageUri: Uri): Resource<String> {
        return try {
            val fileReference = storageReference.child(FirebaseConstants.REF_IMAGES_PROFILE)
            val uploadTask = fileReference.putFile(imageUri).await()
            val imageUrl = fileReference.downloadUrl.await().toString()
            Resource.Success(imageUrl)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}