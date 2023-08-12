package com.example.trackingcontrol.moduls.user.repository

import com.example.trackingcontrol.constants.FirebaseConstants
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.moduls.user.interfaces.UserInterface
import com.example.trackingcontrol.utils.Resource
import com.example.trackingcontrol.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(db: DatabaseReference, private val firebaseAuth: FirebaseAuth): UserInterface {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val uid = currentUser?.uid ?: throw IllegalStateException("User is not signed in")
    private val userReference = db.child(FirebaseConstants.REF_USERS).child(uid)

    override fun getUserData(callback: (Resource<UserData?>) -> Unit) {
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserData::class.java)
                callback(Resource.Success(user))
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(Resource.Failure(databaseError.toException()))
            }
        })
    }

    override suspend fun updateUserDisplayName(newDisplayName: String): Resource<Void> {
        return try {
            val childUpdates = HashMap<String, Any>()
            childUpdates[FirebaseConstants.REF_USERS_NAME] = newDisplayName
            val result = userReference.updateChildren(childUpdates).await()
            Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUserPhotoUrl(newPhotoUrl: String): Resource<Void> {
        return try {
            val childUpdates = HashMap<String, Any>()
            childUpdates[FirebaseConstants.REF_USERS_PHOTO] = newPhotoUrl
            val result = userReference.updateChildren(childUpdates).await()
            return Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun exit() {
        firebaseAuth.signOut()
    }
}