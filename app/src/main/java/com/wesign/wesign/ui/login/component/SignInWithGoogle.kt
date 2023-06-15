package com.wesign.wesign.ui.login.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseUser
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.ui.login.LoginViewModel

@Composable
fun SignInWithGoogle(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccessLogin: (signedIn: FirebaseUser) -> Unit
) {
    val _signInGoogleState by viewModel.signInWithGoogleState.collectAsStateWithLifecycle()

    when (val signInWithGoogleResponse = _signInGoogleState) {
        is Resource.Loading -> {
            viewModel.setLoading(true)
        }

        is Resource.Success -> signInWithGoogleResponse.result?.let { signedIn ->
            viewModel.setLoading(false)
            Log.d("SignInWithGoogle",signedIn.toString())
            LaunchedEffect(signedIn) {
                onSuccessLogin(signedIn)
            }
        }

        is Resource.Error -> LaunchedEffect(Unit) {
            viewModel.setLoading(false)
            print(signInWithGoogleResponse.exception)
        }
    }
}