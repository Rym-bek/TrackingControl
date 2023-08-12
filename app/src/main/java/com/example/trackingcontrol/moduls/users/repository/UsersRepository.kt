package com.example.trackingcontrol.moduls.users.repository

import android.util.Log
import com.example.trackingcontrol.constants.FirebaseConstants
import com.example.trackingcontrol.models.LocationModel
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.moduls.users.interfaces.UsersInterface
import com.example.trackingcontrol.utils.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(db: DatabaseReference):
    UsersInterface {

    private val usersReference = db.child(FirebaseConstants.REF_USERS)


    override fun getUsersData(callback: (Resource<List<UserData?>>) -> Unit) {
        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = mutableListOf<UserData?>()
                for (userSnapshot in dataSnapshot.children) {
                    val name = userSnapshot.child(FirebaseConstants.REF_USERS_NAME).getValue(String::class.java)
                    val photoUrl = userSnapshot.child(FirebaseConstants.REF_USERS_PHOTO).getValue(String::class.java)
                    val uid = userSnapshot.key
                    userList.add(UserData(name,photoUrl,uid))
                }
                Log.d("suka_blya_Repository",userList.toString())
                callback(Resource.Success(userList))
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(Resource.Failure(databaseError.toException()))
            }
        })

        /*val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child(FirebaseConstants.REF_USERS_NAME).getValue(String::class.java)
                    val photoUrl = userSnapshot.child(FirebaseConstants.REF_USERS_PHOTO).getValue(String::class.java)
                    val uid = userSnapshot.key
                    userList.add(UserData(name,photoUrl,uid))
                }
                Log.d("suka_blya_Repository",userList.toString())
                callback(Resource.Success(userList))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(Resource.Failure(databaseError.toException()))
            }
        }
        usersReference.addChildEventListener(childEventListener)*/
    }
}