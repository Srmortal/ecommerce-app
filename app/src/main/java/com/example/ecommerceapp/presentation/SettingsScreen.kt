package com.example.ecommerceapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceapp.data.local.DataStoreRepository
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.ui.theme.LightOrange
import com.example.ecommerceapp.ui.theme.Orange
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val repository = remember { DataStoreRepository(context) }
    val scope = rememberCoroutineScope()
    val isDarkMode by repository.readDarkModeState().collectAsState(initial = false)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Night Mode Toggle
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        tonalElevation = 0.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.NightsStay,
                                contentDescription = "Night Mode",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Night Mode",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { newValue ->
                        scope.launch {
                            repository.saveDarkModeState(newValue)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        uncheckedThumbColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(28.dp),
            color = Orange,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp
        ) {
            TextButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("signin") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        tonalElevation = 0.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Logout,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.onError
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    EcommerceAppTheme(darkTheme = false) {
        SettingsScreen(
            navController = rememberNavController()
        )
    }
}
