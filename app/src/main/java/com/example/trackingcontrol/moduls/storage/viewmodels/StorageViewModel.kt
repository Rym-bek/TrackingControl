package com.example.trackingcontrol.moduls.storage.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingcontrol.moduls.storage.repositories.StorageRepository
import com.example.trackingcontrol.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {
    private val _photoUrl = MutableLiveData<Resource<String?>>()
    val photoUrl: LiveData<Resource<String?>> = _photoUrl

    fun uploadProfilePhoto(imageUri: Uri) {
        viewModelScope.launch {
            _photoUrl.value = storageRepository.uploadProfilePhoto(imageUri)
        }
    }
}