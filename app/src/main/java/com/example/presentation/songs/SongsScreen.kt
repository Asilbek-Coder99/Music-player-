package com.example.presentation.songs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Playlist
import com.example.domain.model.Song
import com.example.presentation.home.SongRowItem
import com.example.ui.components.GlassButton
import com.example.ui.theme.ImmersiveOrange
import com.example.ui.theme.LocalGlassColorScheme

enum class SongSortOrder { TITLE, ARTIST, DURATION }

@Composable
fun SongsScreen(
    songs: List<Song>,
    currentSong: Song?,
    playlists: List<Playlist>,
    onSongSelect: (Song, List<Song>) -> Unit,
    onToggleFavorite: (Song) -> Unit,
    onAddToPlaylist: (playlistId: Long, songId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current
    var sortOrder by remember { mutableStateOf(SongSortOrder.TITLE) }

    val sortedSongs = remember(songs, sortOrder) {
        when (sortOrder) {
            SongSortOrder.TITLE -> songs.sortedBy { it.title }
            SongSortOrder.ARTIST -> songs.sortedBy { it.artist }
            SongSortOrder.DURATION -> songs.sortedByDescending { it.durationMs }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("songs_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "LIBRARY",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Songs",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
            }
        }

        // Sort Options Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlassButton(
                onClick = { sortOrder = SongSortOrder.TITLE },
                borderHighlight = sortOrder == SongSortOrder.TITLE
            ) {
                Text(
                    text = "TITLE",
                    color = if (sortOrder == SongSortOrder.TITLE) ImmersiveOrange else Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            GlassButton(
                onClick = { sortOrder = SongSortOrder.ARTIST },
                borderHighlight = sortOrder == SongSortOrder.ARTIST
            ) {
                Text(
                    text = "ARTIST",
                    color = if (sortOrder == SongSortOrder.ARTIST) ImmersiveOrange else Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            GlassButton(
                onClick = { sortOrder = SongSortOrder.DURATION },
                borderHighlight = sortOrder == SongSortOrder.DURATION
            ) {
                Text(
                    text = "DURATION",
                    color = if (sortOrder == SongSortOrder.DURATION) ImmersiveOrange else Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            itemsIndexed(sortedSongs, key = { _, song -> song.id }) { index, song ->
                val isSelected = currentSong?.id == song.id

                SongRowItem(
                    song = song,
                    index = index + 1,
                    isSelected = isSelected,
                    playlists = playlists,
                    onSongSelect = { onSongSelect(song, sortedSongs) },
                    onToggleFavorite = { onToggleFavorite(song) },
                    onAddToPlaylist = { playlistId -> onAddToPlaylist(playlistId, song.id) }
                )
            }
        }
    }
}

