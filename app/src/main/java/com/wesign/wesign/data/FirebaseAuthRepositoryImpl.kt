package com.wesign.wesign.data

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wesign.wesign.core.SIGN_IN_REQUEST
import com.wesign.wesign.core.SIGN_UP_REQUEST
import com.wesign.wesign.domain.FirebaseAuthRepository
import com.wesign.wesign.domain.FirebaseRegisterResponse
import com.wesign.wesign.domain.LoginResponse
import com.wesign.wesign.domain.OneTapSignInResponse
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.domain.SignInWithGoogleResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    @ApplicationContext val context: Context,
) : FirebaseAuthRepository {

    override val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    override fun login(email: String, password: String): Flow<LoginResponse> = flow {
        emit(Resource.Loading)
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                emit(Resource.Success(it))
            } ?: kotlin.run {
                throw Exception("User is not available")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex))
        }
    }

    override suspend fun signInWithGoogle(googleCredential: AuthCredential): Flow<SignInWithGoogleResponse> =
        flow {
            emit(Resource.Loading)
            try {
                val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
                val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                if (isNewUser) {
                    // TODO add detail user to server
                }
                emit(Resource.Success(authResult.user))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }

    override fun register(email: String, password: String): Flow<FirebaseRegisterResponse> = flow {
        emit(Resource.Loading)
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(firebaseAuth.currentUser))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex))
        }
    }

    override suspend fun oneTapSignInWithGoogle(): Flow<OneTapSignInResponse> =
        flow {
            emit(Resource.Loading)
            try {
                val signInResult = oneTapClient.beginSignIn(signInRequest).await()
                emit(Resource.Success(signInResult))
            } catch (e: Exception) {
                try {
                    val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                    emit(Resource.Success(signUpResult))
                } catch (e: Exception) {
                    emit(Resource.Error(e))
                }
            }
        }
}