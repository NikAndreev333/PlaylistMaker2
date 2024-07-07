package com.example.playlistmaker2.data

import android.media.MediaPlayer
import com.example.playlistmaker2.domain.api.PlayerRepository
import com.example.playlistmaker2.domain.model.PlayerState

class PlayerRepositoryImpl (private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private var playerState: PlayerState = PlayerState.STATE_DEFAULT

    companion object {
        const val TRACK_KEY = "track"
    }

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentTime() : Int{
        return mediaPlayer.currentPosition
    }

    override fun getCurrentState(): PlayerState {
        return playerState
    }
}