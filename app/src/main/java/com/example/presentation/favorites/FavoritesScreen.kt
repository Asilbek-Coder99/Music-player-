package com.example.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.GlassIconButton
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun FavoritesScreen(
    favoriteSongs: List<Song>,
    currentSong: Song?,
    playlists: List<Playlist>,
    onSongSelect: (Song, List<Song>) -> Unit,
    onToggleFavorite: (Song) -> Unit,
    onAddToPlaylist: (playlistId: Long, songId: Long) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlassIconButton(
                onClick = onBack,
                icon = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.padding(end = 16.dp)
            )

            Column {
                Text(
                    text = "COLLECTION",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Favorites",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
            }
        }

        if (favoriteSongs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite songs yet.\nTap the heart icon on any song row to save it here!",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                itemsIndexed(favoriteSongs, key = { _, song -> song.id }) { index, song ->
                    val isSelected = currentSong?.id == song.id

                    SongRowItem(
                        song = song,
                        index = index + 1,
                        isSelected = isSelected,
                        playlists = playlists,
                        onSongSelect = { onSongSelect(song, favoriteSongs) },
                        onToggleFavorite = { onToggleFavorite(song) },
                        onAddToPlaylist = { playlistId -> onAddToPlaylist(playlistId, song.id) }
                    )
                }
            }
        }
    }
}

