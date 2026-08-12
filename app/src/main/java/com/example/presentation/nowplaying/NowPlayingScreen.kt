package com.example.presentation.nowplaying

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Song
import com.example.media.player.PlayerState
import com.example.media.player.RepeatMode
import com.example.ui.components.DynamicBackground
import com.example.ui.components.GlassIconButton
import com.example.ui.components.GlassProgressBar
import com.example.ui.theme.ImmersiveOrange
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun NowPlayingScreen(
    playerState: PlayerState,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleFavorite: (Song) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current
    val song = playerState.currentSong

    val artScale by animateFloatAsState(
        targetValue = if (playerState.isPlaying) 1.0f else 0.90f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "artScale"
    )

    DynamicBackground(currentSong = song) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .testTag("now_playing_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassIconButton(
                    onClick = onClose,
                    icon = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dismiss",
                    size = 44.dp,
                    iconSize = 28.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NOW PLAYING",
                        color = ImmersiveOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.8.sp
                    )
                    Text(
                        text = song?.album ?: "Immersive Audio",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (song != null) {
                    GlassIconButton(
                        onClick = { onToggleFavorite(song) },
                        icon = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        size = 44.dp,
                        iconSize = 22.dp,
                        tint = if (song.isFavorite) ImmersiveOrange else Color.White
                    )
                } else {
                    Spacer(modifier = Modifier.size(44.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Center Album Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .aspectRatio(1f)
                    .scale(artScale)
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(36.dp),
                        spotColor = ImmersiveOrange.copy(alpha = 0.4f)
                    )
                    .clip(RoundedCornerShape(36.dp))
                    .background(glassColors.glassSurface),
                contentAlignment = Alignment.Center
            ) {
                if (song?.albumArtUri != null) {
                    AsyncImage(
                        model = song.albumArtUri,
                        contentDescription = song.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title & Artist
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = song?.title ?: "No Track Selected",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = song?.artist ?: "Glassic Player",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Slider
            GlassProgressBar(
                currentPositionMs = playerState.currentPositionMs,
                durationMs = playerState.durationMs,
                onSeekTo = onSeekTo,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Player Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle
                GlassIconButton(
                    onClick = onToggleShuffle,
                    icon = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    size = 44.dp,
                    iconSize = 20.dp,
                    tint = if (playerState.isShuffleEnabled) ImmersiveOrange else Color.White.copy(alpha = 0.5f)
                )

                // Previous
                GlassIconButton(
                    onClick = onPrevious,
                    icon = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    size = 52.dp,
                    iconSize = 26.dp
                )

                // Large Glass Play/Pause
                GlassIconButton(
                    onClick = onTogglePlayPause,
                    icon = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                    size = 72.dp,
                    iconSize = 38.dp,
                    highlight = true
                )

                // Next
                GlassIconButton(
                    onClick = onNext,
                    icon = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    size = 52.dp,
                    iconSize = 26.dp
                )

                // Repeat
                GlassIconButton(
                    onClick = onToggleRepeat,
                    icon = if (playerState.repeatMode == RepeatMode.ONE) Icons.Default.RepeatOne else Icons.Default.Repeat,
                    contentDescription = "Repeat",
                    size = 44.dp,
                    iconSize = 20.dp,
                    tint = if (playerState.repeatMode != RepeatMode.OFF) ImmersiveOrange else Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

