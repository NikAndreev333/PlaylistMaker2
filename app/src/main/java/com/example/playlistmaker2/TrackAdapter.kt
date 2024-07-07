package com.example.playlistmaker2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import domain.model.Track

class TrackAdapter (private val trackList: ArrayList<Track>, val listner: Listner) : RecyclerView.Adapter<TrackViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position], listner)

    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}