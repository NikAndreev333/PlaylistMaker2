package com.example.playlistmaker2.domain.api

import com.example.playlistmaker2.domain.model.PlayerState

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentTime() : Int
    fun getCurrentState(): PlayerState
}