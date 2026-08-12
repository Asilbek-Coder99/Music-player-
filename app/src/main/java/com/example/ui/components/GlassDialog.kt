package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun GlassDialog(
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val glassColors = LocalGlassColorScheme.current
    val shape = RoundedCornerShape(28.dp)

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            glassColors.glassBorderHighlight,
            glassColors.glassBorder
        )
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = shape,
                    spotColor = glassColors.glassBorderHighlight.copy(alpha = 0.5f)
                )
                .clip(shape)
                .background(glassColors.canvasBackground.copy(alpha = 0.90f))
                .border(BorderStroke(1.5.dp, borderBrush), shape)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = title,
                    color = glassColors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                content()
            }
        }
    }
}
