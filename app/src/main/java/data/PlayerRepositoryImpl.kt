package data

import android.content.Intent
import android.media.MediaPlayer
import com.google.gson.Gson
import domain.model.PlayerState
import domain.model.Track
import domain.repository.PlayerRepository
import presentation.PlayerActivity

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

    override fun getCurrentTrack(intent: Intent): Track {
        val json = intent?.getStringExtra(PlayerActivity.TRACK_KEY)
        return Gson().fromJson(json, Track::class.java)
    }

    override fun getCurrentState(): PlayerState {
        return playerState
    }
}