package com.wesign.wesign.domain

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

typealias LoginResponse = Response<FirebaseUser>
typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<FirebaseUser>

interface FirebaseAuthRepository {
    val currentUser: FirebaseUser?

    fun login(email: String, password: String) : Flow<LoginResponse>
    suspend fun signInWithGoogle(googleCredential: AuthCredential) : Flow<SignInWithGoogleResponse>
    suspend fun register(email: String, password: String) : Response<FirebaseUser>

    suspend fun oneTapSignInWithGoogle(): Flow<OneTapSignInResponse>
}