package com.example.playlistmaker2

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView  = itemView.findViewById(R.id.trackTime)
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    fun bind (item : Track, listner: Listner) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.internet_error_dark)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
            .into(trackImage)
        trackName.text = item?.trackName
        artistName.text = item?.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime?.toLong())

        itemView.setOnClickListener {
            listner.onClick(item)
        }
    }
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}