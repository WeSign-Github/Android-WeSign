package com.wesign.wesign.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.R
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(uiState = uiState, onLoginPressed = { viewModel.login() })
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginPressed: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(LocalExtendedColorScheme.current.surfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign in to your account",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 33.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 33.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = { value ->
                        email = value
                    },
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = password,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = { value ->
                        password = value
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val desc = if (passwordVisible) "Hide Password" else "Show Password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = desc)
                        }
                    },
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ElevatedButton(
                    onClick = { onLoginPressed() },
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

                SignInGoogleButton()

            }

            RegisterSection()
        }
    }
}

@Composable
fun RegisterSection() {
    val context = LocalContext.current

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
                Toast.makeText(context, "Register", Toast.LENGTH_SHORT).show()
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SignInGoogleButton() {
    ElevatedButton(
        onClick = {},
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
        LoginScreen(uiState = LoginUiState(), onLoginPressed = {})
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreviewWithText() {
    WeSignTheme() {
        LoginScreen(
            uiState = LoginUiState(
                currentEmail = "methafizh30@gmail.com",
                currentPassword = "12345678"
            ),
            onLoginPressed = {}
        )
    }
}