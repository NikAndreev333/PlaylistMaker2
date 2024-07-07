package com.example.playlistmaker2

import android.content.SharedPreferences
import com.google.gson.Gson
import com.example.playlistmaker2.domain.model.Track


const val TRACK_HISTORY_KEY = "track_history_key"

class SearchHistory (private val sharedPreferences: SharedPreferences) {


    var searchedTrackHistory: ArrayList<Track> = arrayListOf()
    private val trackHistorySize = 10
    private val json = sharedPreferences.getString(TRACK_HISTORY_KEY, null)

    init {
        if (!json.isNullOrEmpty()) {
            val array: Array<Track> = Gson().fromJson(json, Array<Track>::class.java)
            array.forEach {
                searchedTrackHistory.add(it)
            }
        }
    }

    fun saveTrack(track: Track,) {
        searchedTrackHistory.removeIf {it.trackId == track.trackId}
        if (searchedTrackHistory.size >= trackHistorySize) {
            searchedTrackHistory.removeAt(trackHistorySize - 1)
        }
        searchedTrackHistory.add(0, track )
        writeToSH(searchedTrackHistory)

    }

    fun clearHistory() {
        searchedTrackHistory.clear()
        sharedPreferences.edit()
            .putString(TRACK_HISTORY_KEY, "")
            .apply()
    }

    fun writeToSH(searchedTrackHistory: ArrayList<Track>) {
        val json = Gson().toJson(searchedTrackHistory)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY_KEY, json)
            .apply()
    }

}
