package com.wesign.wesign.ui.login.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseUser
import com.wesign.wesign.domain.Response
import com.wesign.wesign.ui.login.LoginViewModel

@Composable
fun SignInWithGoogle(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccessLogin: (signedIn: FirebaseUser) -> Unit
) {
    val _signInGoogleState by viewModel.signInWithGoogleState.collectAsStateWithLifecycle()

    when (val signInWithGoogleResponse = _signInGoogleState) {
        is Response.Loading -> {

        }

        is Response.Success -> signInWithGoogleResponse.result?.let { signedIn ->
            Log.d("SignInWithGoogle",signedIn.toString())
            LaunchedEffect(signedIn) {
                onSuccessLogin(signedIn)
            }
        }

        is Response.Error -> LaunchedEffect(Unit) {
            print(signInWithGoogleResponse.exception)
        }
    }
}