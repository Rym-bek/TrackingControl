package com.example.trackingcontrol.models

data class LocationModel(
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val uid:String? = "",
    val time:Long? = 0
)