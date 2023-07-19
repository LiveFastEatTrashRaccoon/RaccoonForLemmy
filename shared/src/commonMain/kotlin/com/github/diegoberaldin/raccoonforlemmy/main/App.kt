package com.github.diegoberaldin.raccoonforlemmy.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.ui.components.BottomNavigation
import com.github.diegoberaldin.raccoonforlemmy.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    AppTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            bottomBar = {
                BottomNavigation(
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
            ) {
            }
        }
    }
}