package com.example.playlistmaker2

import android.media.MediaPlayer
import data.PlayerRepositoryImpl
import domain.impl.PlayerInteractorImpl
import domain.repository.PlayerInteractor
import domain.repository.PlayerRepository

object Creator {
    private fun providePlayerRepository() : PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
    }

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository())
    }
}