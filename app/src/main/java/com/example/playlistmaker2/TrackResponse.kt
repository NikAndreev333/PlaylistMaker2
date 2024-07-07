package com.example.playlistmaker2

import com.example.playlistmaker2.domain.model.Track

data class TrackResponse(val resultCount: Int, val results: ArrayList<Track> ) {

}
