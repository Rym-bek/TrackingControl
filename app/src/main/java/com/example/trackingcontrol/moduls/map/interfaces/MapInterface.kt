package com.example.trackingcontrol.moduls.map.interfaces

import com.example.trackingcontrol.models.LocationModel
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface MapInterface {
    suspend fun getMapData(callback: (Resource<LocationModel?>) -> Unit)
    suspend fun setMapData(locationModel: LocationModel)
    val currentUser: FirebaseUser?
}