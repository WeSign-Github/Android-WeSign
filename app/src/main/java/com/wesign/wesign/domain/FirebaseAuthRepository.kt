package com.wesign.wesign.domain

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

typealias LoginResponse = Resource<FirebaseUser>
typealias OneTapSignInResponse = Resource<BeginSignInResult>
typealias SignInWithGoogleResponse = Resource<FirebaseUser>
typealias FirebaseRegisterResponse = Resource<FirebaseUser>

interface FirebaseAuthRepository {
    val currentUser: FirebaseUser?

    fun login(email: String, password: String): Flow<LoginResponse>
    suspend fun signInWithGoogle(googleCredential: AuthCredential): Flow<SignInWithGoogleResponse>
    fun register(email: String, password: String): Flow<FirebaseRegisterResponse>

    suspend fun oneTapSignInWithGoogle(): Flow<OneTapSignInResponse>
}