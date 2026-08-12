package com.example.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.MusicDatabase
import com.example.data.media.MediaScanner
import com.example.data.repository.MusicRepository
import com.example.domain.model.Album
import com.example.domain.model.Artist
import com.example.domain.model.Playlist
import com.example.domain.model.Song
import com.example.domain.model.ThemeMode
import com.example.media.player.MusicPlayerEngine
import com.example.media.service.PlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MusicDatabase.getDatabase(application)
    private val scanner = MediaScanner(application)
    val repository = MusicRepository(scanner, database.musicDao())

    val playerEngine: MusicPlayerEngine = PlaybackService.playerEngine
        ?: MusicPlayerEngine(application).also { PlaybackService.playerEngine = it }

    val playerState = playerEngine.playerState

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPlaylistSongs = MutableStateFlow<List<Song>>(emptyList())
    val selectedPlaylistSongs: StateFlow<List<Song>> = _selectedPlaylistSongs.asStateFlow()

    val allSongs: StateFlow<List<Song>> = repository.allSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteSongs: StateFlow<List<Song>> = repository.favoriteSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentlyPlayed: StateFlow<List<Song>> = repository.recentlyPlayed.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val albums: StateFlow<List<Album>> = repository.albums.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val artists: StateFlow<List<Artist>> = repository.artists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val playlists: StateFlow<List<Playlist>> = repository.playlists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val searchResults: StateFlow<List<Song>> = combine(
        allSongs,
        _searchQuery
    ) { songs, query ->
        if (query.isBlank()) emptyList()
        else {
            val q = query.trim().lowercase()
            songs.filter {
                it.title.lowercase().contains(q) ||
                it.artist.lowercase().contains(q) ||
                it.album.lowercase().contains(q)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Record play counts when songs finish/transition
        playerEngine.onSongChangedListener = { song ->
            viewModelScope.launch {
                repository.recordSongPlayed(song.id)
            }
        }

        // Initial library scan
        rescanLibrary()
    }

    fun rescanLibrary() {
        viewModelScope.launch {
            repository.scanAndSyncMedia()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun playSong(song: Song, queue: List<Song> = allSongs.value) {
        val index = queue.indexOfFirst { it.id == song.id }.let { if (it >= 0) it else 0 }
        playerEngine.setQueueAndPlay(queue, index)
    }

    fun togglePlayPause() = playerEngine.togglePlayPause()
    fun seekTo(positionMs: Long) = playerEngine.seekTo(positionMs)
    fun playNext() = playerEngine.playNext()
    fun playPrevious() = playerEngine.playPrevious()
    fun toggleShuffle() = playerEngine.toggleShuffle()
    fun toggleRepeat() = playerEngine.toggleRepeat()

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            repository.toggleFavorite(song)
        }
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                repository.createPlaylist(name.trim())
            }
        }
    }

    fun renamePlaylist(id: Long, newName: String) {
        viewModelScope.launch {
            if (newName.isNotBlank()) {
                repository.renamePlaylist(id, newName.trim())
            }
        }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            repository.deletePlaylist(id)
        }
    }

    fun addSongToPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            repository.removeSongFromPlaylist(playlistId, songId)
        }
    }

    fun loadPlaylistSongs(playlistId: Long) {
        viewModelScope.launch {
            repository.getSongsForPlaylist(playlistId).collect {
                _selectedPlaylistSongs.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Do not immediately release playerEngine so background playback survives ViewModel re-creations
    }
}
