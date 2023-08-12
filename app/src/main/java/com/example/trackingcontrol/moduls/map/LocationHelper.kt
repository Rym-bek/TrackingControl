package com.example.trackingcontrol.moduls.map

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.trackingcontrol.R
import com.example.trackingcontrol.constants.ConstantsPermissions
import com.example.trackingcontrol.models.LocationModel
import com.example.trackingcontrol.moduls.map.viewmodels.MapViewModel
import com.example.trackingcontrol.utils.PermissionsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class LocationHelper(fragment: Fragment, private val context: Context, private val mapViewModel: MapViewModel, private val uid: String) {

    private val permissionsHelper: PermissionsHelper = PermissionsHelper(fragment, ConstantsPermissions.PERMISSIONS_LOCATION)

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                mapViewModel.setLocationData(
                    LocationModel(
                        it.latitude,
                        it.longitude,
                        uid,
                        System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun getCurrentLocation(mMap: GoogleMap, marker: Marker?) {
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            permissionsHelper.requestPermission()
        } else {
            if(marker!=null)
            {
                val update = CameraUpdateFactory.newLatLngZoom(marker.position, 16.0f)
                mMap.moveCamera(update)
                Log.d("suka_blya","intent_zoom")
            }
            else
            {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                        mMap.moveCamera(update)
                    } else {
                        Toast.makeText(context, context.getString(R.string.location_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
                Log.d("suka_blya","pidoras")
            }
        }
    }

    fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000 // 10 seconds
        locationRequest.fastestInterval = 5000 // 5 seconds

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsHelper.requestPermission()
        }
        else
        {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun removeLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}