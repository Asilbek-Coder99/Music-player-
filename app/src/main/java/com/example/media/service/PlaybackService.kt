package com.example.media.service

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.media.player.MusicPlayerEngine

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    companion object {
        var playerEngine: MusicPlayerEngine? = null
    }

    override fun onCreate() {
        super.onCreate()
        val engine = playerEngine ?: MusicPlayerEngine(this).also { playerEngine = it }
        
        mediaSession = MediaSession.Builder(this, engine.exoPlayer)
            .setId("GlassicMediaSession")
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player?.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
