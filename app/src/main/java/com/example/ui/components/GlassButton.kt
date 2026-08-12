package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    containerColor: Color? = null,
    borderHighlight: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val glassColors = LocalGlassColorScheme.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "scale"
    )

    val fillColor = containerColor ?: if (isPressed) glassColors.glassSurfaceHover else glassColors.glassSurface

    val borderBrush = Brush.linearGradient(
        colors = if (borderHighlight) {
            listOf(glassColors.glassBorderHighlight, glassColors.glassBorderHighlight.copy(alpha = 0.5f))
        } else {
            listOf(glassColors.glassBorderHighlight, glassColors.glassBorder)
        }
    )

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.3f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .clip(shape)
            .background(fillColor)
            .border(BorderStroke(1.dp, borderBrush), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = glassColors.textPrimary.copy(alpha = 0.2f)),
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun GlassIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    tint: Color? = null,
    shape: Shape = CircleShape,
    highlight: Boolean = false
) {
    val glassColors = LocalGlassColorScheme.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "scale"
    )

    val borderBrush = Brush.linearGradient(
        colors = if (highlight) {
            listOf(glassColors.glassBorderHighlight, glassColors.glassBorderHighlight)
        } else {
            listOf(glassColors.glassBorderHighlight, glassColors.glassBorder)
        }
    )

    val iconColor = tint ?: glassColors.textPrimary

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .shadow(
                elevation = if (highlight) 12.dp else 4.dp,
                shape = shape,
                spotColor = if (highlight) glassColors.glassBorderHighlight else Color.Transparent
            )
            .clip(shape)
            .background(if (highlight) glassColors.glassBorderHighlight.copy(alpha = 0.3f) else glassColors.glassSurface)
            .border(BorderStroke(1.dp, borderBrush), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = iconColor.copy(alpha = 0.3f)),
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = iconColor
        )
    }
}
