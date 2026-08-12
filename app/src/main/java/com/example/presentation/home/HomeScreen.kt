package com.example.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Playlist
import com.example.domain.model.Song
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassIconButton
import com.example.ui.theme.ImmersiveOrange
import com.example.ui.theme.ImmersiveRose
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun HomeScreen(
    recentlyPlayed: List<Song>,
    allSongs: List<Song>,
    playlists: List<Playlist>,
    currentSong: Song?,
    isPlaying: Boolean,
    onSongSelect: (Song, List<Song>) -> Unit,
    onToggleFavorite: (Song) -> Unit,
    onAddToPlaylist: (playlistId: Long, songId: Long) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_column"),
        contentPadding = PaddingValues(bottom = 140.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NOW LISTENING",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Music",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlassIconButton(
                        onClick = onOpenFavorites,
                        icon = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = ImmersiveOrange,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    GlassIconButton(
                        onClick = onOpenSearch,
                        icon = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    GlassIconButton(
                        onClick = onOpenSettings,
                        icon = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }

        // Recently Played Section
        if (recentlyPlayed.isNotEmpty()) {
            item {
                Text(
                    text = "RECENTLY PLAYED",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    items(recentlyPlayed, key = { it.id }) { song ->
                        GlassCard(
                            onClick = { onSongSelect(song, recentlyPlayed) },
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.width(160.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(136.dp)
                                        .clip(RoundedCornerShape(26.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    ImmersiveOrange,
                                                    ImmersiveRose
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (song.albumArtUri != null) {
                                        AsyncImage(
                                            model = song.albumArtUri,
                                            contentDescription = song.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.matchParentSize()
                                        )
                                    } else {
                                        // Vinyl record ring decoration matching Immersive UI design
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black.copy(alpha = 0.2f))
                                                .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = song.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = song.artist,
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Local Library Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LOCAL LIBRARY",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    text = "${allSongs.size} TRACKS",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // All Songs List
        if (allSongs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Scanning audio files on device...",
                        color = glassColors.textSecondary,
                        fontSize = 15.sp
                    )
                }
            }
        } else {
            itemsIndexed(allSongs, key = { _, song -> song.id }) { index, song ->
                val isSelected = currentSong?.id == song.id

                SongRowItem(
                    song = song,
                    index = index + 1,
                    isSelected = isSelected,
                    playlists = playlists,
                    onSongSelect = { onSongSelect(song, allSongs) },
                    onToggleFavorite = { onToggleFavorite(song) },
                    onAddToPlaylist = { playlistId -> onAddToPlaylist(playlistId, song.id) }
                )
            }
        }
    }
}

@Composable
fun SongRowItem(
    song: Song,
    index: Int = 1,
    isSelected: Boolean,
    playlists: List<Playlist>,
    onSongSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToPlaylist: (Long) -> Unit
) {
    val glassColors = LocalGlassColorScheme.current
    var menuExpanded by remember { mutableStateOf(false) }
    var playlistSubmenuExpanded by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    val formattedIndex = if (index < 10) "0$index" else "$index"
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(shape)
            .background(if (isSelected) ImmersiveOrange.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.04f))
            .border(
                BorderStroke(
                    1.dp,
                    if (isSelected) ImmersiveOrange.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.08f)
                ),
                shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = ImmersiveOrange),
                onClick = onSongSelect
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index number or Artwork box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                if (song.albumArtUri != null) {
                    AsyncImage(
                        model = song.albumArtUri,
                        contentDescription = song.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                } else {
                    Text(
                        text = formattedIndex,
                        color = if (isSelected) ImmersiveOrange else Color.White.copy(alpha = 0.35f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Title and Artist
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp)
            ) {
                Text(
                    text = song.title,
                    color = if (isSelected) ImmersiveOrange else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = song.artist,
                    color = Color.White.copy(alpha = 0.40f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Duration in mono
            Text(
                text = song.durationFormatted,
                color = Color.White.copy(alpha = 0.35f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 6.dp)
            )

            // Three-dot Menu
            Box {
                GlassIconButton(
                    onClick = { menuExpanded = true },
                    icon = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    size = 32.dp,
                    iconSize = 18.dp
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(glassColors.canvasBackground)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (song.isFavorite) ImmersiveOrange else glassColors.textPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (song.isFavorite) "Remove from Favorites" else "Add to Favorites",
                                    color = glassColors.textPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            menuExpanded = false
                            onToggleFavorite()
                        }
                    )

                    if (playlists.isNotEmpty()) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PlaylistAdd,
                                        contentDescription = null,
                                        tint = glassColors.textPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Add to Playlist...",
                                        color = glassColors.textPrimary,
                                        fontSize = 14.sp
                                    )
                                }
                            },
                            onClick = {
                                menuExpanded = false
                                playlistSubmenuExpanded = true
                            }
                        )
                    }
                }

                DropdownMenu(
                    expanded = playlistSubmenuExpanded,
                    onDismissRequest = { playlistSubmenuExpanded = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(glassColors.canvasBackground)
                ) {
                    playlists.forEach { playlist ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = playlist.name,
                                    color = glassColors.textPrimary,
                                    fontSize = 14.sp
                                )
                            },
                            onClick = {
                                playlistSubmenuExpanded = false
                                onAddToPlaylist(playlist.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

