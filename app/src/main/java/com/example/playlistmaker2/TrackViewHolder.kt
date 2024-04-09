package com.example.playlistmaker2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackName: TextView = itemView.findViewById(R.id.trackName)
    val artistName: TextView = itemView.findViewById(R.id.artistName)
    val trackTime: TextView  = itemView.findViewById(R.id.trackTime)
    val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    fun bind (item : Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackImage)
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
    }
}