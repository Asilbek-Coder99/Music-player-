package com.example.domain.model

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val contentUri: String,
    val albumArtUri: String? = null,
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val lastPlayedMs: Long = 0,
    val trackNumber: Int = 0
) {
    val durationFormatted: String
        get() {
            val totalSeconds = durationMs / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format("%d:%02d", minutes, seconds)
        }
}

data class Album(
    val id: Long,
    val name: String,
    val artist: String,
    val songCount: Int,
    val albumArtUri: String? = null,
    val year: Int = 0
)

data class Artist(
    val name: String,
    val songCount: Int,
    val albumCount: Int = 1
)

data class Playlist(
    val id: Long,
    val name: String,
    val songCount: Int = 0,
    val coverUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}
