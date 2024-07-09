package com.example.playlistmaker2

import android.media.MediaPlayer
import com.example.playlistmaker2.data.PlayerRepositoryImpl
import com.example.playlistmaker2.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker2.domain.api.PlayerInteractor
import com.example.playlistmaker2.domain.api.PlayerRepository

object Creator {
    private fun providePlayerRepository() : PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
    }

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository())
    }
}