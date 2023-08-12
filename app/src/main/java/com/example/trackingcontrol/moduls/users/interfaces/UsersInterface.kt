package com.example.trackingcontrol.moduls.users.interfaces

import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.utils.Resource

interface UsersInterface {
    fun getUsersData(callback: (Resource<List<UserData?>>) -> Unit)
}