package com.wesign.wesign.ui.login.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.ui.login.LoginViewModel

@Composable
fun OneTapSignIn(
    viewModel: LoginViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit
) {
    val _oneTapState by viewModel.oneTapState.collectAsStateWithLifecycle()

    when (val oneTapSignInResponse = _oneTapState) {
        is Resource.Loading -> {
            viewModel.setLoading(true)
        }

        is Resource.Success -> oneTapSignInResponse.result?.let {
            viewModel.setLoading(false)
            LaunchedEffect(it) {
                launch(it)
            }
        }

        is Resource.Error -> LaunchedEffect(Unit) {
            viewModel.setLoading(false)
            Log.e("OneTapSignIn", "OneTapSignIn Error")
        }
    }
}