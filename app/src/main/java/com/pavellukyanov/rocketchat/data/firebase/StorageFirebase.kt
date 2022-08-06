package com.pavellukyanov.rocketchat.data.firebase

import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

interface StorageFirebase : suspend () -> FirebaseStorage

class StorageFirebaseImpl @Inject constructor() : StorageFirebase {
    override suspend operator fun invoke(): FirebaseStorage = FirebaseStorage.getInstance()
}