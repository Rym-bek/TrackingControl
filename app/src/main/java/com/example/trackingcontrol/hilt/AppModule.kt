package com.example.trackingcontrol.hilt

import com.example.trackingcontrol.moduls.map.interfaces.MapInterface
import com.example.trackingcontrol.moduls.map.repository.MapRepository
import com.example.trackingcontrol.moduls.storage.interfaces.StorageInterface
import com.example.trackingcontrol.moduls.storage.repositories.StorageRepository
import com.example.trackingcontrol.moduls.user.interfaces.UserInterface
import com.example.trackingcontrol.moduls.user.repository.UserRepository
import com.example.trackingcontrol.moduls.users.interfaces.UsersInterface
import com.example.trackingcontrol.moduls.users.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideDatabaseReference(): DatabaseReference {
        return Firebase.database.reference
    }

    @Provides
    fun provideFirebaseStorage(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideUserRepository(userRepository: UserRepository): UserInterface = userRepository

    @Singleton
    @Provides
    fun provideUsersRepository(usersRepository: UsersRepository): UsersInterface = usersRepository

    @Singleton
    @Provides
    fun provideMapRepository(mapRepository: MapRepository): MapInterface = mapRepository

    @Singleton
    @Provides
    fun providesStorageRepository(storageRepository: StorageRepository): StorageInterface = storageRepository
}