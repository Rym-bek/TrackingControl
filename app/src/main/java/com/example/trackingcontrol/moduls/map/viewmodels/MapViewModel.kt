package com.example.trackingcontrol.moduls.map.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingcontrol.models.LocationModel
import com.example.trackingcontrol.moduls.map.repository.MapRepository
import com.example.trackingcontrol.utils.Resource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val _locationData = MutableLiveData<Resource<LocationModel?>>()
    val locationData: LiveData<Resource<LocationModel?>> = _locationData

    init {
        observeLocationData()
    }


    private fun observeLocationData() {
        viewModelScope.launch {
            mapRepository.getMapData() {
                _locationData.value = it
            }
        }
    }


    fun setLocationData(locationModel: LocationModel) {
        viewModelScope.launch {
            mapRepository.setMapData(locationModel)
        }
    }
}
