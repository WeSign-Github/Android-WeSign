package com.wesign.wesign.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
fun ProfileRoute() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    Image(Icons.Filled.ArrowBack, contentDescription = "Back")
                },
            )
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Image(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Profile Image",
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                )
                Text("Hafizh Sumantri", style = MaterialTheme.typography.headlineMedium)
                Text("(HafizhS)", style = MaterialTheme.typography.bodyLarge)
            }

            Column(
                Modifier
                    .weight(1.7f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 45.dp),
            ) {
                Row(
                    Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        Icons.Filled.Settings,
                        contentDescription = "Setting",
                        Modifier.aspectRatio(1f).padding(5.dp),
                    )
                    Spacer(Modifier.width(40.dp))
                    Text("Setting", style = MaterialTheme.typography.bodyLarge)
                }
                Divider(Modifier.fillMaxWidth())
                Row(
                    Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        Icons.Filled.Settings,
                        contentDescription = "Setting",
                        Modifier.aspectRatio(1f),
                    )
                    Spacer(Modifier.width(40.dp))
                    Text("Setting", style = MaterialTheme.typography.bodyLarge)
                }
            }

        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        ProfileScreen()
    }
}