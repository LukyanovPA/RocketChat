package com.pavellukyanov.rocketchat.data.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

interface DatabaseFirebase : suspend () -> FirebaseDatabase

class DatabaseFirebaseImpl @Inject constructor() : DatabaseFirebase {
    override suspend operator fun invoke(): FirebaseDatabase = Firebase.database
}