package com.github.diegoberaldin.raccoonforlemmy.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.android.presentation.GreetPresenter
import com.github.diegoberaldin.raccoonforlemmy.android.ui.GreetingView
import com.github.diegoberaldin.raccoonforlemmy.android.ui.theme.AppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val presenter: GreetPresenter by inject()
                    GreetingView(presenter)
                }
            }
        }
    }
}
