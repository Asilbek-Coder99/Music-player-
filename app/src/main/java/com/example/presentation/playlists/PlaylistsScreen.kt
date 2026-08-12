package com.example.presentation.playlists

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Playlist
import com.example.domain.model.Song
import com.example.presentation.home.SongRowItem
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassDialog
import com.example.ui.components.GlassIconButton
import com.example.ui.theme.ImmersiveOrange
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun PlaylistsScreen(
    playlists: List<Playlist>,
    playlistSongs: List<Song>,
    currentSong: Song?,
    onSelectPlaylist: (Long) -> Unit,
    onCreatePlaylist: (String) -> Unit,
    onRenamePlaylist: (Long, String) -> Unit,
    onDeletePlaylist: (Long) -> Unit,
    onRemoveSongFromPlaylist: (playlistId: Long, songId: Long) -> Unit,
    onSongSelect: (Song, List<Song>) -> Unit,
    onToggleFavorite: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current

    var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<Playlist?>(null) }

    var newPlaylistName by remember { mutableStateOf("") }
    var renameValue by remember { mutableStateOf("") }

    if (selectedPlaylist != null) {
        val activePlaylist = selectedPlaylist!!

        LaunchedEffect(activePlaylist.id) {
            onSelectPlaylist(activePlaylist.id)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("playlist_detail_view")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    GlassIconButton(
                        onClick = { selectedPlaylist = null },
                        icon = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Column {
                        Text(
                            text = "PLAYLIST",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = activePlaylist.name,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row {
                    GlassIconButton(
                        onClick = {
                            renameValue = activePlaylist.name
                            showRenameDialog = activePlaylist
                        },
                        icon = Icons.Default.Edit,
                        contentDescription = "Rename",
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    GlassIconButton(
                        onClick = {
                            onDeletePlaylist(activePlaylist.id)
                            selectedPlaylist = null
                        },
                        icon = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }

            if (playlistSongs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No songs added to this playlist yet.\nAdd songs using the 3-dot menu on any song row.",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 140.dp)
                ) {
                    itemsIndexed(playlistSongs, key = { _, song -> song.id }) { index, song ->
                        val isSelected = currentSong?.id == song.id

                        SongRowItem(
                            song = song,
                            index = index + 1,
                            isSelected = isSelected,
                            playlists = emptyList(),
                            onSongSelect = { onSongSelect(song, playlistSongs) },
                            onToggleFavorite = { onToggleFavorite(song) },
                            onAddToPlaylist = {}
                        )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("playlists_screen")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "USER MIXES",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Playlists",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                }

                GlassButton(
                    onClick = {
                        newPlaylistName = ""
                        showCreateDialog = true
                    },
                    borderHighlight = true
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = ImmersiveOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NEW",
                        color = ImmersiveOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            if (playlists.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No custom playlists found.\nTap 'NEW' above to create one!",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 140.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(playlists, key = { it.id }) { playlist ->
                        GlassCard(
                            onClick = { selectedPlaylist = playlist },
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(ImmersiveOrange.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlaylistPlay,
                                        contentDescription = null,
                                        tint = ImmersiveOrange,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        text = playlist.name,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Custom Glass Mix",
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Create Playlist Dialog
    if (showCreateDialog) {
        GlassDialog(
            onDismissRequest = { showCreateDialog = false },
            title = "Create Playlist"
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(glassColors.glassSurface)
                    .padding(16.dp)
            ) {
                if (newPlaylistName.isEmpty()) {
                    Text(
                        text = "Playlist Title",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 15.sp
                    )
                }
                BasicTextField(
                    value = newPlaylistName,
                    onValueChange = { newPlaylistName = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = SolidColor(ImmersiveOrange),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                GlassButton(
                    onClick = { showCreateDialog = false },
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                }

                GlassButton(
                    onClick = {
                        if (newPlaylistName.isNotBlank()) {
                            onCreatePlaylist(newPlaylistName)
                            showCreateDialog = false
                        }
                    },
                    borderHighlight = true
                ) {
                    Text("Create", color = ImmersiveOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Rename Playlist Dialog
    if (showRenameDialog != null) {
        val targetPlaylist = showRenameDialog!!
        GlassDialog(
            onDismissRequest = { showRenameDialog = null },
            title = "Rename Playlist"
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(glassColors.glassSurface)
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = renameValue,
                    onValueChange = { renameValue = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = SolidColor(ImmersiveOrange),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                GlassButton(
                    onClick = { showRenameDialog = null },
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                }

                GlassButton(
                    onClick = {
                        if (renameValue.isNotBlank()) {
                            onRenamePlaylist(targetPlaylist.id, renameValue)
                            showRenameDialog = null
                        }
                    },
                    borderHighlight = true
                ) {
                    Text("Save", color = ImmersiveOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

