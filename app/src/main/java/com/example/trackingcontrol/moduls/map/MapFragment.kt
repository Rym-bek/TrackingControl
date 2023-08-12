package com.example.trackingcontrol.moduls.map

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.example.trackingcontrol.R
import com.example.trackingcontrol.databinding.MapFragmentBinding
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.moduls.map.viewmodels.MapViewModel
import com.example.trackingcontrol.moduls.users.viewmodels.UsersViewModel
import com.example.trackingcontrol.utils.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: MapFragmentBinding? = null

    private val binding get() = _binding!!

    private var user: FirebaseUser?=null
    private var uid: String?=null

    private lateinit var mMap: GoogleMap

    private lateinit var locationHelper: LocationHelper

    private val mapViewModel: MapViewModel by activityViewModels()

    private val usersViewModel: UsersViewModel by activityViewModels()

    private var markersMap = mutableMapOf<String, Marker>()

    private val users: MutableList<UserData?> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MapFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        user = Firebase.auth.currentUser
        uid= user?.uid

        locationHelper = LocationHelper(this, requireContext(), mapViewModel, uid!!)
        locationHelper.requestLocationUpdates()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun observeUsersData() {
        usersViewModel.usersData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    users.addAll(it.result)
                }
            }
        }
    }


    private fun observeLocation() {
        mapViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val data = it.result
                    val latLng = LatLng(data?.latitude!!, data.longitude!!)
                    val userUid = data.uid
                    addOrUpdateMarker(userUid!!, latLng)
                }
            }
        }
    }

    private fun addOrUpdateMarker(userUid: String, latLng: LatLng) {
        val userData = usersViewModel.getUser(userUid)
        val name =userData?.name
        val photoUrl =userData?.photoUrl
        if (markersMap.containsKey(userUid)) {
            markersMap[userUid]?.position = latLng
        } else {
            val title = if (userUid == uid) {
                "$name (Вы)"
            } else {
                name
            }
            
            Glide.with(this)
                .asBitmap()
                .load(photoUrl)
                .signature(ObjectKey(photoUrl.hashCode()))
                .circleCrop()
                .override(150, 150)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val icon = BitmapDescriptorFactory.fromBitmap(resource)
                        val marker = mMap.addMarker(MarkerOptions().position(latLng).title(title).icon(icon)
                            .snippet("Онлайн"))
                        markersMap[userUid] = marker!!
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Если загрузка изображения была отменена или очищена
                    }
                })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        observeLocation()
        observeUsersData()

        val bundle = this.arguments
        val uid = bundle?.getString("uid")

        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            withContext(Dispatchers.Main) {
                val marker = markersMap[uid]
                locationHelper.getCurrentLocation(mMap,marker)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}