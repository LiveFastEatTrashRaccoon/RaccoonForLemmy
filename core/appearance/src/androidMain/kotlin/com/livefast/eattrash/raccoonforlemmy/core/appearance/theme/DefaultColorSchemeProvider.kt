package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import org.koin.core.annotation.Single

@Single
internal actual class DefaultColorSchemeProvider(
    private val context: Context,
) : ColorSchemeProvider {
    actual override val supportsDynamicColors: Boolean
        @ChecksSdkIntAtLeast(31)
        get() {
            return Build.VERSION.SDK_INT >= 31
        }

    @SuppressLint("NewApi")
    actual override fun getColorScheme(
        theme: UiTheme,
        dynamic: Boolean,
        customSeed: Color?,
    ): ColorScheme =
        when (theme) {
            UiTheme.Dark -> {
                when {
                    dynamic -> {
                        dynamicDarkColorScheme(context)
                    }

                    customSeed != null -> {
                        dynamicColorScheme(
                            seedColor = customSeed,
                            isDark = true,
                            isAmoled = false,
                            style = PALETTE_STYLE,
                        )
                    }

                    else -> {
                        DarkColors
                    }
                }
            }

            UiTheme.Black -> {
                when {
                    dynamic -> {
                        dynamicDarkColorScheme(context).blackify()
                    }

                    customSeed != null -> {
                        dynamicColorScheme(
                            seedColor = customSeed,
                            isDark = true,
                            isAmoled = true,
                            style = PALETTE_STYLE,
                        )
                    }

                    else -> {
                        BlackColors
                    }
                }
            }

            else -> {
                when {
                    dynamic -> {
                        dynamicLightColorScheme(context)
                    }

                    customSeed != null -> {
                        dynamicColorScheme(
                            seedColor = customSeed,
                            isDark = false,
                            isAmoled = false,
                            style = PALETTE_STYLE,
                        )
                    }

                    else -> {
                        LightColors
                    }
                }
            }
        }
}

private val PALETTE_STYLE = PaletteStyle.Rainbow
