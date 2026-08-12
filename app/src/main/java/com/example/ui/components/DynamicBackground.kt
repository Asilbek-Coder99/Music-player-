package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.domain.model.Song
import com.example.ui.theme.ImmersiveIndigo
import com.example.ui.theme.ImmersiveOrange
import com.example.ui.theme.LocalGlassColorScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DynamicBackground(
    currentSong: Song?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val glassColors = LocalGlassColorScheme.current

    val defaultColor1 = ImmersiveOrange
    val defaultColor2 = ImmersiveIndigo

    var extractedColor1 by remember { mutableStateOf(defaultColor1) }
    var extractedColor2 by remember { mutableStateOf(defaultColor2) }

    LaunchedEffect(currentSong?.albumArtUri) {
        val artUri = currentSong?.albumArtUri
        if (!artUri.isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                try {
                    val loader = ImageLoader(context)
                    val request = ImageRequest.Builder(context)
                        .data(artUri)
                        .allowHardware(false) // required for Palette extraction
                        .build()

                    val result = loader.execute(request)
                    if (result is SuccessResult) {
                        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                        if (bitmap != null) {
                            val palette = Palette.from(bitmap).generate()
                            val c1 = palette.getVibrantColor(palette.getDominantColor(0xFFFF4E00.toInt()))
                            val c2 = palette.getMutedColor(palette.getDarkVibrantColor(0xFF4E00FF.toInt()))

                            extractedColor1 = Color(c1)
                            extractedColor2 = Color(c2)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            extractedColor1 = defaultColor1
            extractedColor2 = defaultColor2
        }
    }

    val animatedC1 by animateColorAsState(
        targetValue = extractedColor1,
        animationSpec = tween(1000),
        label = "c1"
    )

    val animatedC2 by animateColorAsState(
        targetValue = extractedColor2,
        animationSpec = tween(1000),
        label = "c2"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Dynamic Glowing Gradient Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Background Base
            drawRect(color = glassColors.canvasBackground)

            // Top-Left Radial Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        animatedC1.copy(alpha = if (glassColors.isDark) 0.45f else 0.30f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.2f, height * 0.25f),
                    radius = width * 0.9f
                ),
                center = Offset(width * 0.2f, height * 0.25f),
                radius = width * 0.9f
            )

            // Bottom-Right Radial Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        animatedC2.copy(alpha = if (glassColors.isDark) 0.40f else 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.8f, height * 0.75f),
                    radius = width * 1.1f
                ),
                center = Offset(width * 0.8f, height * 0.75f),
                radius = width * 1.1f
            )

            // Dark or Light Softening Mesh Overlay
            drawRect(
                color = if (glassColors.isDark) Color.Black.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.45f)
            )
        }

        content()
    }
}
