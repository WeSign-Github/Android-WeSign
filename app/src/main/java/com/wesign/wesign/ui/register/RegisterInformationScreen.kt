package com.wesign.wesign.ui.register

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesign.wesign.component.AuthTopbar
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun RegisterInformationRoute(
    viewModelStoreOwner: ViewModelStoreOwner? = null,
    onNavigateUp: () -> Unit,
) {
    val viewModel: RegisterViewModel = viewModelStoreOwner?.let {
        Log.d("RegisterScreen", "with viewModelStoreOwner")
        viewModel(viewModelStoreOwner)
    } ?: run {
        viewModel()
    }

    RegisterInformationScreen(onNavigateUp = onNavigateUp)
}

@Composable
fun RegisterInformationScreen(
    onRegister: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var displayName by rememberSaveable { mutableStateOf("") }

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
                    value = firstName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = { value ->
                        firstName = value
                    },
                    label = { Text("First Name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = lastName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = { value ->
                        lastName = value
                    },
                    label = { Text("Last Name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = displayName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    onValueChange = { value ->
                        displayName = value
                    },
                    label = { Text("Display Name") }
                )
                Spacer(modifier = Modifier.height(40.dp))
                ElevatedButton(
                    onClick = {
                        onRegister()
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
