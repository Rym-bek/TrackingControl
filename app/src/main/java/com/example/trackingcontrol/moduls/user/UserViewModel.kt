package com.example.trackingcontrol.moduls.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.moduls.user.repository.UserRepository
import com.example.trackingcontrol.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
    ) : ViewModel() {

    private val _userData = MutableLiveData<Resource<UserData?>>()
    val userData: LiveData<Resource<UserData?>> = _userData

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userRepository.getUserData() {
                _userData.value = it
            }
        }
    }

    fun updateDisplayName(newDisplayName: String) {
        viewModelScope.launch {
            userRepository.updateUserDisplayName(newDisplayName)
        }
    }

    fun updatePhotoUrl(newPhotoUrl: String) {
        viewModelScope.launch {
            userRepository.updateUserPhotoUrl(newPhotoUrl)
        }
    }

    fun exit() {
        userRepository.exit()
    }
}