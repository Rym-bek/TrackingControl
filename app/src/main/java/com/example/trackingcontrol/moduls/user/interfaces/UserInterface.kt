package com.example.trackingcontrol.moduls.user.interfaces

import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface UserInterface {
    fun getUserData(callback: (Resource<UserData?>) -> Unit)
    suspend fun updateUserDisplayName(newDisplayName: String): Resource<Void>
    suspend fun updateUserPhotoUrl(newPhotoUrl: String): Resource<Void>
    fun exit()
    val currentUser: FirebaseUser?
}