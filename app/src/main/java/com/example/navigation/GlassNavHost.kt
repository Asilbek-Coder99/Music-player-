package com.example.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.presentation.MainViewModel
import com.example.presentation.albums.AlbumsScreen
import com.example.presentation.artists.ArtistsScreen
import com.example.presentation.favorites.FavoritesScreen
import com.example.presentation.home.HomeScreen
import com.example.presentation.nowplaying.NowPlayingScreen
import com.example.presentation.playlists.PlaylistsScreen
import com.example.presentation.search.SearchScreen
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.songs.SongsScreen
import com.example.ui.components.DynamicBackground
import com.example.ui.components.GlassBottomBar
import com.example.ui.components.GlassMiniPlayer
import com.example.ui.components.NavTab

object Screen {
    const val HOME = "home"
    const val SONGS = "songs"
    const val ALBUMS = "albums"
    const val ARTISTS = "artists"
    const val PLAYLISTS = "playlists"
    const val FAVORITES = "favorites"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
}

@Composable
fun GlassNavHost(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.HOME

    val allSongs by viewModel.allSongs.collectAsStateWithLifecycle()
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsStateWithLifecycle()
    val favoriteSongs by viewModel.favoriteSongs.collectAsStateWithLifecycle()
    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val playlistSongs by viewModel.selectedPlaylistSongs.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()

    var showNowPlayingScreen by remember { mutableStateOf(false) }

    val isTopLevelRoute = currentRoute in listOf(Screen.HOME, Screen.SONGS, Screen.ALBUMS, Screen.PLAYLISTS)

    DynamicBackground(currentSong = playerState.currentSong) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.HOME,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screen.HOME) {
                        HomeScreen(
                            recentlyPlayed = recentlyPlayed,
                            allSongs = allSongs,
                            playlists = playlists,
                            currentSong = playerState.currentSong,
                            isPlaying = playerState.isPlaying,
                            onSongSelect = { song, queue -> viewModel.playSong(song, queue) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { playlistId, songId -> viewModel.addSongToPlaylist(playlistId, songId) },
                            onOpenSearch = { navController.navigate(Screen.SEARCH) },
                            onOpenSettings = { navController.navigate(Screen.SETTINGS) },
                            onOpenFavorites = { navController.navigate(Screen.FAVORITES) }
                        )
                    }

                    composable(Screen.SONGS) {
                        SongsScreen(
                            songs = allSongs,
                            currentSong = playerState.currentSong,
                            playlists = playlists,
                            onSongSelect = { song, queue -> viewModel.playSong(song, queue) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { playlistId, songId -> viewModel.addSongToPlaylist(playlistId, songId) }
                        )
                    }

                    composable(Screen.ALBUMS) {
                        AlbumsScreen(
                            albums = albums,
                            allSongs = allSongs,
                            currentSong = playerState.currentSong,
                            playlists = playlists,
                            onSongSelect = { song, queue -> viewModel.playSong(song, queue) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { playlistId, songId -> viewModel.addSongToPlaylist(playlistId, songId) }
                        )
                    }

                    composable(Screen.PLAYLISTS) {
                        PlaylistsScreen(
                            playlists = playlists,
                            playlistSongs = playlistSongs,
                            currentSong = playerState.currentSong,
                            onSelectPlaylist = { id -> viewModel.loadPlaylistSongs(id) },
                            onCreatePlaylist = { name -> viewModel.createPlaylist(name) },
                            onRenamePlaylist = { id, name -> viewModel.renamePlaylist(id, name) },
                            onDeletePlaylist = { id -> viewModel.deletePlaylist(id) },
                            onRemoveSongFromPlaylist = { pId, sId -> viewModel.removeSongFromPlaylist(pId, sId) },
                            onSongSelect = { song, queue -> viewModel.playSong(song, queue) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                        )
                    }

                    composable(Screen.FAVORITES) {
                        FavoritesScreen(
                            favoriteSongs = favoriteSongs,
                            currentSong = playerState.currentSong,
                            playlists = playlists,
                            onSongSelect = { song, queue -> viewModel.playSong(song, queue) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { playlistId, songId -> viewModel.addSongToPlaylist(playlistId, songId) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.SEARCH) {
                        SearchScreen(
                            query = searchQuery,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            searchResults = searchResults,
                            currentSong = playerState.currentSong,
                            playlists = playlists,
                            onSongSelect = { song, queue -> viewModel.playSong(song, queue) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { playlistId, songId -> viewModel.addSongToPlaylist(playlistId, songId) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.SETTINGS) {
                        SettingsScreen(
                            themeMode = themeMode,
                            onThemeModeChange = { viewModel.setThemeMode(it) },
                            onRescanLibrary = { viewModel.rescanLibrary() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                // Overlay Controls: Mini Player + Floating Bottom Bar
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    val progressFraction = if (playerState.durationMs > 0) {
                        playerState.currentPositionMs.toFloat() / playerState.durationMs.toFloat()
                    } else 0f

                    Box {
                        if (playerState.currentSong != null && !showNowPlayingScreen) {
                            GlassMiniPlayer(
                                song = playerState.currentSong,
                                isPlaying = playerState.isPlaying,
                                progressFraction = progressFraction,
                                onTogglePlayPause = { viewModel.togglePlayPause() },
                                onNext = { viewModel.playNext() },
                                onOpenNowPlaying = { showNowPlayingScreen = true },
                                modifier = Modifier.padding(bottom = if (isTopLevelRoute) 72.dp else 12.dp)
                            )
                        }

                        if (isTopLevelRoute && !showNowPlayingScreen) {
                            GlassBottomBar(
                                currentRoute = currentRoute,
                                onTabSelected = { tab ->
                                    navController.navigate(tab.route) {
                                        popUpTo(Screen.HOME) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }

                // Now Playing Full Screen Modal
                AnimatedVisibility(
                    visible = showNowPlayingScreen,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    NowPlayingScreen(
                        playerState = playerState,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onSeekTo = { viewModel.seekTo(it) },
                        onNext = { viewModel.playNext() },
                        onPrevious = { viewModel.playPrevious() },
                        onToggleShuffle = { viewModel.toggleShuffle() },
                        onToggleRepeat = { viewModel.toggleRepeat() },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onClose = { showNowPlayingScreen = false }
                    )
                }
            }
        }
    }
}
