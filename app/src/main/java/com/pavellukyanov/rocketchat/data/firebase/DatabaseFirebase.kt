package com.pavellukyanov.rocketchat.data.firebase

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

interface DatabaseFirebase : suspend () -> FirebaseDatabase

class DatabaseFirebaseImpl @Inject constructor() : DatabaseFirebase {
    override suspend operator fun invoke(): FirebaseDatabase = FirebaseDatabase.getInstance()
}