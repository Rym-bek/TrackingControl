package com.example.trackingcontrol.moduls.users.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.moduls.user.repository.UserRepository
import com.example.trackingcontrol.moduls.users.repository.UsersRepository
import com.example.trackingcontrol.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel()  {

    private val _usersData = MutableLiveData<Resource<List<UserData?>>>()
    val usersData: LiveData<Resource<List<UserData?>>> = _usersData

    init {
        loadUsersData()
    }

    private fun loadUsersData() {
        viewModelScope.launch {
            usersRepository.getUsersData() {
                _usersData.value = it
            }
        }
    }

    fun getUser(uid: String): UserData? {
        return usersData.value?.toData()?.find { it?.uid == uid }
    }
}