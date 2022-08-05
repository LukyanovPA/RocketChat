package com.pavellukyanov.rocketchat.data.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface AuthFirebase : suspend () -> FirebaseAuth

class AuthFirebaseImpl @Inject constructor() : AuthFirebase {
    override suspend operator fun invoke(): FirebaseAuth = FirebaseAuth.getInstance()
}