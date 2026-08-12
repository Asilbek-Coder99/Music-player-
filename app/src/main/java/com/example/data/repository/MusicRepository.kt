package com.example.data.repository

import com.example.data.database.MusicDao
import com.example.data.database.PlaylistEntity
import com.example.data.database.PlaylistSongCrossRef
import com.example.data.database.SongEntity
import com.example.data.media.MediaScanner
import com.example.domain.model.Album
import com.example.domain.model.Artist
import com.example.domain.model.Playlist
import com.example.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MusicRepository(
    private val mediaScanner: MediaScanner,
    private val musicDao: MusicDao
) {

    val allSongs: Flow<List<Song>> = combine(
        musicDao.getAllSongs(),
        musicDao.getFavoriteSongs()
    ) { dbSongs, favorites ->
        val favoriteIds = favorites.map { it.id }.toSet()
        dbSongs.map { entity ->
            Song(
                id = entity.id,
                title = entity.title,
                artist = entity.artist,
                album = entity.album,
                durationMs = entity.durationMs,
                contentUri = entity.contentUri,
                albumArtUri = entity.albumArtUri,
                isFavorite = favoriteIds.contains(entity.id),
                playCount = entity.playCount,
                lastPlayedMs = entity.lastPlayedMs
            )
        }
    }.flowOn(Dispatchers.IO)

    val favoriteSongs: Flow<List<Song>> = musicDao.getFavoriteSongs().map { entities ->
        entities.map { entity ->
            Song(
                id = entity.id,
                title = entity.title,
                artist = entity.artist,
                album = entity.album,
                durationMs = entity.durationMs,
                contentUri = entity.contentUri,
                albumArtUri = entity.albumArtUri,
                isFavorite = true,
                playCount = entity.playCount,
                lastPlayedMs = entity.lastPlayedMs
            )
        }
    }.flowOn(Dispatchers.IO)

    val recentlyPlayed: Flow<List<Song>> = musicDao.getRecentlyPlayedSongs().map { entities ->
        entities.map { entity ->
            Song(
                id = entity.id,
                title = entity.title,
                artist = entity.artist,
                album = entity.album,
                durationMs = entity.durationMs,
                contentUri = entity.contentUri,
                albumArtUri = entity.albumArtUri,
                isFavorite = entity.isFavorite,
                playCount = entity.playCount,
                lastPlayedMs = entity.lastPlayedMs
            )
        }
    }.flowOn(Dispatchers.IO)

    val albums: Flow<List<Album>> = allSongs.map { songList ->
        songList.groupBy { it.album }
            .map { (albumName, songs) ->
                val sampleSong = songs.firstOrNull()
                Album(
                    id = albumName.hashCode().toLong(),
                    name = albumName,
                    artist = sampleSong?.artist ?: "Various Artists",
                    songCount = songs.size,
                    albumArtUri = sampleSong?.albumArtUri
                )
            }.sortedBy { it.name }
    }.flowOn(Dispatchers.IO)

    val artists: Flow<List<Artist>> = allSongs.map { songList ->
        songList.groupBy { it.artist }
            .map { (artistName, songs) ->
                val albumCount = songs.map { it.album }.distinct().size
                Artist(
                    name = artistName,
                    songCount = songs.size,
                    albumCount = albumCount
                )
            }.sortedBy { it.name }
    }.flowOn(Dispatchers.IO)

    val playlists: Flow<List<Playlist>> = musicDao.getAllPlaylists().map { entities ->
        entities.map { entity ->
            Playlist(
                id = entity.id,
                name = entity.name,
                coverUri = entity.coverUri,
                createdAt = entity.createdAt
            )
        }
    }.flowOn(Dispatchers.IO)

    suspend fun scanAndSyncMedia() = withContext(Dispatchers.IO) {
        try {
            val scannedSongs = mediaScanner.scanLocalMedia()
            val songEntities = scannedSongs.map { song ->
                SongEntity(
                    id = song.id,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    durationMs = song.durationMs,
                    contentUri = song.contentUri,
                    albumArtUri = song.albumArtUri,
                    isFavorite = song.isFavorite,
                    playCount = song.playCount,
                    lastPlayedMs = song.lastPlayedMs
                )
            }
            musicDao.insertSongs(songEntities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun toggleFavorite(song: Song) = withContext(Dispatchers.IO) {
        musicDao.setFavorite(song.id, !song.isFavorite)
    }

    suspend fun recordSongPlayed(songId: Long) = withContext(Dispatchers.IO) {
        musicDao.recordPlay(songId)
    }

    suspend fun createPlaylist(name: String): Long = withContext(Dispatchers.IO) {
        musicDao.createPlaylist(PlaylistEntity(name = name))
    }

    suspend fun renamePlaylist(playlistId: Long, newName: String) = withContext(Dispatchers.IO) {
        musicDao.renamePlaylist(playlistId, newName)
    }

    suspend fun deletePlaylist(playlistId: Long) = withContext(Dispatchers.IO) {
        musicDao.deletePlaylist(playlistId)
    }

    suspend fun addSongToPlaylist(playlistId: Long, songId: Long) = withContext(Dispatchers.IO) {
        musicDao.addSongToPlaylist(PlaylistSongCrossRef(playlistId = playlistId, songId = songId))
    }

    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) = withContext(Dispatchers.IO) {
        musicDao.removeSongFromPlaylist(playlistId, songId)
    }

    fun getSongsForPlaylist(playlistId: Long): Flow<List<Song>> {
        return musicDao.getSongsForPlaylist(playlistId).map { entities ->
            entities.map { entity ->
                Song(
                    id = entity.id,
                    title = entity.title,
                    artist = entity.artist,
                    album = entity.album,
                    durationMs = entity.durationMs,
                    contentUri = entity.contentUri,
                    albumArtUri = entity.albumArtUri,
                    isFavorite = entity.isFavorite,
                    playCount = entity.playCount,
                    lastPlayedMs = entity.lastPlayedMs
                )
            }
        }.flowOn(Dispatchers.IO)
    }
}
