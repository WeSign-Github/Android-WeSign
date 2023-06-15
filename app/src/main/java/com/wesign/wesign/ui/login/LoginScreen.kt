package com.wesign.wesign.ui.login

import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.wesign.wesign.R
import com.wesign.wesign.component.AuthTopbar
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.ui.login.component.OneTapSignIn
import com.wesign.wesign.ui.login.component.SignInWithGoogle
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun LoginRoute(
    onRegisterPressed: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val _loginState by viewModel.loginState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//            Log.d("LoginScreen", result.data?.dataString!!)
            if (result.resultCode == RESULT_OK) {
                try {
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    Log.d("LoginScreen", "Called viewmodel")
                    viewModel.signInWithGoogle(googleCredentials)
                } catch (it: ApiException) {
                    print(it)
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    LaunchedEffect(_loginState) {
        when (val loginState = _loginState) {
            is Resource.Success -> {
                viewModel.setLoading(false)
                loginState.result?.let {
                    onLoginSuccess()
                }
            }

            is Resource.Loading -> {
                Log.d("LoginScreen", "Loading...")
                viewModel.setLoading(true)
            }

            is Resource.Error -> {
                viewModel.setLoading(false)
                Toast.makeText(context, loginState.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    LoginScreen(
        uiState = uiState,
        onRegisterPressed = onRegisterPressed,
        onEmailChanged = viewModel::setEmail,
        onPasswordChanged = viewModel::setPassword,
        onTogglePassword = viewModel::togglePasswordVisible,
        onLoginPressed = viewModel::login,
        onGoogleLoginPressed = viewModel::oneTapSignIn,
    )

    OneTapSignIn(
        viewModel = viewModel,
        launch = {
            launch(it)
        },
    )

    SignInWithGoogle(
        viewModel = viewModel,
        onSuccessLogin = {
            Log.d("LoginScreen - SignInWithGoogle",it.toString())
            onLoginSuccess()
        },
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onRegisterPressed: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onTogglePassword: () -> Unit = {},
    onLoginPressed: (String, String) -> Unit = { _, _ -> },
    onGoogleLoginPressed: () -> Unit = {}
) {

    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            AuthTopbar(title = "Sign in to your account")

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 33.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.email,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = onEmailChanged,
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = uiState.password,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = onPasswordChanged,
                    singleLine = true,
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon =
                            if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val desc =
                            if (uiState.isPasswordVisible) "Hide Password" else "Show Password"

                        IconButton(onClick = onTogglePassword) {
                            Icon(icon, contentDescription = desc)
                        }
                    },
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ElevatedButton(
                    onClick = {
                        onLoginPressed(uiState.email, uiState.password)
                    },
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = LocalExtendedColorScheme.current.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = LocalExtendedColorScheme.current.surfaceContainerLow.copy(
                            alpha = 0.12f
                        ),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    elevation = ButtonDefaults.buttonElevation(3.dp)
                ) {
                    Text(text = "Login", color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Other Sign up
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    Divider(Modifier.weight(1f))
                    Text(
                        "Or Sign In with",
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .wrapContentWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Divider(Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(20.dp))

                SignInGoogleButton(onGoogleLoginPressed)

            }

            RegisterSection(onRegisterPressed)
        }

        if (uiState.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
            ) {
            }
        }
    }
}

@Composable
fun RegisterSection(
    onRegisterPressed: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 7.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Text(
            "Don’t have Account?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "Register",
            modifier = Modifier.clickable {
                onRegisterPressed()
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SignInGoogleButton(
    onGoogleLoginPressed: () -> Unit = {}
) {
    ElevatedButton(
        onClick = {
            onGoogleLoginPressed()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = LocalExtendedColorScheme.current.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = LocalExtendedColorScheme.current.surfaceContainerLow.copy(
                alpha = 0.12f
            ),
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(3.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Sign in With Google"
        )
        Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    WeSignTheme() {
        LoginScreen(uiState = LoginUiState())
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreviewWithText() {
    WeSignTheme() {
        LoginScreen(
            uiState = LoginUiState(
                email = "methafizh30@gmail.com",
                password = "12345678"
            ),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreviewWithTextLoading() {
    WeSignTheme() {
        LoginScreen(
            uiState = LoginUiState(
                email = "methafizh30@gmail.com",
                password = "12345678",
                isLoading = true,
            ),
        )
    }
}
