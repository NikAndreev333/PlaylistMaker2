package com.example.playlistmaker2.domain.impl

import com.example.playlistmaker2.domain.api.PlayerInteractor
import com.example.playlistmaker2.domain.api.PlayerRepository
import com.example.playlistmaker2.domain.model.PlayerState

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


    override fun getCurrentState(): PlayerState {
        return playerRepository.getCurrentState()
    }
}