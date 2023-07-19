package com.github.diegoberaldin.raccoonforlemmy.android.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.android.presentation.GreetPresenter

@Composable
fun GreetingView(presenter: GreetPresenter) {
    val text = presenter.print()
    Text(text = text)
}
