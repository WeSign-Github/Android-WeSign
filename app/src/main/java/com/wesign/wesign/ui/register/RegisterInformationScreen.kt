package com.wesign.wesign.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesign.wesign.component.AuthTopbar
import com.wesign.wesign.component.LoadingView
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun RegisterInformationRoute(
    viewModel: RegisterInformationViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onRegisterSuccess: () -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val registerState by viewModel.registerState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is Resource.Success -> {
                viewModel.setLoading(false)
                state.result?.let {
                    onRegisterSuccess()
                }

            }

            is Resource.Error -> {
                viewModel.setLoading(false)
                Toast.makeText(context, state.exception?.message, Toast.LENGTH_LONG).show()
            }

            is Resource.Loading -> {
                viewModel.setLoading(true)
            }
        }
    }

    RegisterInformationScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onRegister = viewModel::registerDetailInformation,
        onFirstNameChanged = viewModel::changeFirstName,
        onLastNameChanged = viewModel::changeLastName,
        onDisplayNameChanged = viewModel::changeDisplayName
    )

    if (uiState.isLoading) LoadingView()
}

@Composable
fun RegisterInformationScreen(
    uiState: RegisterInformationState = RegisterInformationState(),
    onFirstNameChanged: (String) -> Unit = {},
    onLastNameChanged: (String) -> Unit = {},
    onDisplayNameChanged: (String) -> Unit = {},
    onRegister: (String, String, String) -> Unit = { _, _, _ -> },
    onNavigateUp: () -> Unit = {}
) {

    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            AuthTopbar(
                title = "We need your Information",
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
                    value = uiState.firstName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = onFirstNameChanged,
                    label = { Text("First Name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = uiState.lastName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = onLastNameChanged,
                    label = { Text("Last Name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = uiState.displayName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = onDisplayNameChanged,
                    label = { Text("Display Name") }
                )
                Spacer(modifier = Modifier.height(40.dp))
                ElevatedButton(
                    onClick = {
                        onRegister(uiState.firstName, uiState.lastName, uiState.displayName)
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
                    Text(text = "Register", color = MaterialTheme.colorScheme.primary)
                }


            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterInformationScreenPreview() {
    WeSignTheme() {
        RegisterInformationScreen()
    }
}
