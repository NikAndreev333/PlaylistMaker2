package domain.repository

import android.content.Intent
import domain.model.PlayerState
import domain.model.Track

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentTime() : Int
    fun getCurrentTrack(intent: Intent) : Track
    fun getCurrentState(): PlayerState
}