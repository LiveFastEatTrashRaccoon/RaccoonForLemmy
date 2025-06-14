package com.livefast.eattrash.raccoonforlemmy.core.utils

import android.os.Build
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

/*
 * There is a weird bug on Android Q and below due to which the IME padding is added twice and
 * results in a blank space above the virtual keyboard (the same size of the keyboard itself).
 *
 * This workaround simply ignores the imePadding() modifier in those cases.
 */
@Composable
actual fun Modifier.safeImePadding(): Modifier =
    then(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Modifier.imePadding()
        } else {
            // custom approach for older android versions
            val insets = WindowInsets.ime
            val density = LocalDensity.current

            Modifier.padding(
                bottom = with(density) {
                    (insets.getBottom(density) * 0.5f).toDp()
                },
            )
        },
    )
