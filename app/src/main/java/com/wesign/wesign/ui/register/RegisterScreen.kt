package com.wesign.wesign.ui.register

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesign.wesign.component.AuthTopbar
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun RegisterRoute(
    viewModelStoreOwner: ViewModelStoreOwner? = null,
    onNavigateUp: () -> Unit,
    onNext: () -> Unit,
) {
    val viewModel: RegisterViewModel = viewModelStoreOwner?.let {
        Log.d("RegisterScreen","with viewModelStoreOwner")
        viewModel(viewModelStoreOwner)
    } ?: run {
        viewModel()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreen(
        uiState = uiState,
        onNext = onNext,
        onNavigateUp = onNavigateUp,
        onEmailChanged = viewModel::setEmail,
        onPasswordChanged = viewModel::setPassword,
        onRePassword = viewModel::setRePassword,
        onPasswordToggle = viewModel::togglePassword,
        onRePasswordToggle = viewModel::toggleRePassword,
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterState,
    onNext: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onRePassword: (String) -> Unit = {},
    onPasswordToggle: () -> Unit = {},
    onRePasswordToggle: () -> Unit = {},
) {


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
                        onNext()
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
