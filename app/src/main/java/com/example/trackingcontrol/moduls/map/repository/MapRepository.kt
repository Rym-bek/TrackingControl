package com.example.trackingcontrol.moduls.map.repository

import android.util.Log
import com.example.trackingcontrol.constants.FirebaseConstants
import com.example.trackingcontrol.models.LocationModel
import com.example.trackingcontrol.moduls.map.interfaces.MapInterface
import com.example.trackingcontrol.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepository @Inject constructor(
    db: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
): MapInterface{

    private val databaseReference = db.child(FirebaseConstants.REF_LOCATIONS)

    override suspend fun getMapData(callback: (Resource<LocationModel?>) -> Unit) {

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                callback(Resource.Success(snapshot.getValue(LocationModel::class.java)))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(Resource.Failure(databaseError.toException()))
            }
        }
        databaseReference.addChildEventListener(childEventListener)
    }

    override suspend fun setMapData(locationModel: LocationModel) {
        databaseReference.child(currentUser!!.uid).setValue(locationModel)
    }

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

}