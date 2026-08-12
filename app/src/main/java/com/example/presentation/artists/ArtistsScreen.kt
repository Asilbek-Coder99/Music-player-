package com.example.presentation.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Artist
import com.example.domain.model.Playlist
import com.example.domain.model.Song
import com.example.presentation.home.SongRowItem
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassIconButton
import com.example.ui.theme.LocalGlassColorScheme

@Composable
fun ArtistsScreen(
    artists: List<Artist>,
    allSongs: List<Song>,
    currentSong: Song?,
    playlists: List<Playlist>,
    onSongSelect: (Song, List<Song>) -> Unit,
    onToggleFavorite: (Song) -> Unit,
    onAddToPlaylist: (playlistId: Long, songId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColorScheme.current
    var selectedArtist by remember { mutableStateOf<Artist?>(null) }

    if (selectedArtist != null) {
        val artistSongs = remember(selectedArtist, allSongs) {
            allSongs.filter { it.artist == selectedArtist?.name }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("artist_detail_view")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassIconButton(
                    onClick = { selectedArtist = null },
                    icon = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.padding(end = 16.dp)
                )

                Column {
                    Text(
                        text = selectedArtist?.name ?: "",
                        color = glassColors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${artistSongs.size} songs • ${selectedArtist?.albumCount ?: 1} albums",
                        color = glassColors.textSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                items(artistSongs, key = { it.id }) { song ->
                    val isSelected = currentSong?.id == song.id

                    SongRowItem(
                        song = song,
                        isSelected = isSelected,
                        playlists = playlists,
                        onSongSelect = { onSongSelect(song, artistSongs) },
                        onToggleFavorite = { onToggleFavorite(song) },
                        onAddToPlaylist = { playlistId -> onAddToPlaylist(playlistId, song.id) }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(1.dp)
                            .background(glassColors.glassBorder.copy(alpha = 0.15f))
                    )
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("artists_screen")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Artists",
                    color = glassColors.textPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${artists.size} featured artists",
                    color = glassColors.textSecondary,
                    fontSize = 13.sp
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 140.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(artists, key = { it.name }) { artist ->
                    GlassCard(
                        onClick = { selectedArtist = artist },
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
                                    .clip(CircleShape)
                                    .background(glassColors.glassBorderHighlight.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = glassColors.glassBorderHighlight,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = artist.name,
                                    color = glassColors.textPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "${artist.songCount} songs • ${artist.albumCount} albums",
                                    color = glassColors.textSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
