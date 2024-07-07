package domain.impl

import android.content.Intent
import domain.model.PlayerState
import domain.model.Track
import domain.repository.PlayerInteractor
import domain.repository.PlayerRepository

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun preparePlayer(url: String) {
        playerRepository.preparePlayer(url)
    }

    override fun release() {
        playerRepository.release()
    }

    override fun getCurrentTime(): Int {
         return playerRepository.getCurrentTime()
    }

    override fun getCurrentTrack(intent: Intent): Track {
        return playerRepository.getCurrentTrack(intent)
    }

    override fun getCurrentState(): PlayerState {
        return playerRepository.getCurrentState()
    }
}