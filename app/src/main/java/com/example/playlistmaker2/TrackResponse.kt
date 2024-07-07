package com.example.playlistmaker2

import domain.model.Track

data class TrackResponse(val resultCount: Int, val results: ArrayList<Track> ) {

}
