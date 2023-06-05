package com.wesign.wesign.ui.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesign.wesign.component.AuthTopbar
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun RegisterRoute(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onNext: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.firebaseUser) {
        uiState.firebaseUser?.let {
            Log.d("Register", it.toString())
            onNext()
        }
    }

    RegisterScreen(
        uiState = uiState,
        onNextPressed = viewModel::register,
        onNavigateUp = onNavigateUp,
        onEmailChanged = viewModel::setEmail,
        onPasswordChanged = viewModel::setPassword,
        onRePassword = viewModel::setRePassword,
        onPasswordToggle = viewModel::togglePassword,
        onRePasswordToggle = viewModel::toggleRePassword,
    )

    if (uiState.isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        ) {
        }
    }

}

@Composable
fun RegisterScreen(
    uiState: RegisterState,
    onNextPressed: (String, String) -> Unit = { _, _ -> },
    onNavigateUp: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onRePassword: (String) -> Unit = {},
    onPasswordToggle: () -> Unit = {},
    onRePasswordToggle: () -> Unit = {},
) {

    val context = LocalContext.current

    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            AuthTopbar(
                title = "Register",
                showNavigateUp = true,
                onNavigateUp = { onNavigateUp() },
            )

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

                        IconButton(onClick = onPasswordToggle) {
                            Icon(icon, contentDescription = desc)
                        }
                    },
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = uiState.rePassword,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = onRePassword,
                    singleLine = true,
                    visualTransformation = if (uiState.isRePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon =
                            if (uiState.isRePasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val desc =
                            if (uiState.isRePasswordVisible) "Hide Password" else "Show Password"

                        IconButton(onClick = onRePasswordToggle) {
                            Icon(icon, contentDescription = desc)
                        }
                    },
                    label = { Text("Re-type Password") }
                )
                Spacer(modifier = Modifier.height(30.dp))
                ElevatedButton(
                    onClick = {
                        if (uiState.email.isEmpty() || uiState.password.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Email or Password cannot be Empty",
                                Toast.LENGTH_LONG
                            ).show()
                            return@ElevatedButton
                        }
                        onNextPressed(uiState.email, uiState.password)
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
                    Text(text = "Next", color = MaterialTheme.colorScheme.primary)
                }


            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    WeSignTheme() {
        RegisterScreen(
            RegisterState()
        )
    }
}
