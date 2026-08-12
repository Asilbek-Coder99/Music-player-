package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImmersiveOrange
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun GlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search songs, artists, albums...",
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current
    val focusManager = LocalFocusManager.current
    val shape = RoundedCornerShape(28.dp)

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            ImmersiveOrange.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.15f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = shape,
                spotColor = ImmersiveOrange.copy(alpha = 0.25f)
            )
            .clip(shape)
            .background(glassColors.glassSurface)
            .border(BorderStroke(1.dp, borderBrush), shape)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag("glass_search_bar")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = ImmersiveOrange,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 6.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 15.sp
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(ImmersiveOrange),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_input")
                )
            }

            if (query.isNotEmpty()) {
                GlassIconButton(
                    onClick = { onQueryChange("") },
                    icon = Icons.Default.Close,
                    contentDescription = "Clear Search",
                    size = 28.dp,
                    iconSize = 16.dp
                )
            }
        }
    }
}

