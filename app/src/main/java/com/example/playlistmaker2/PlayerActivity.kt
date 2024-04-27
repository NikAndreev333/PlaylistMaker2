package com.example.playlistmaker2

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity(): AppCompatActivity() {

    var trackPoster = findViewById<ImageView> (R.id.trackPoster)
    var trackAlbumName = findViewById<TextView>(R.id.trackAlbumName)
    var artistName = findViewById<TextView>(R.id.artist_name)
    var timer = findViewById<TextView>(R.id.timer)
    var trackTime = findViewById<TextView>(R.id.trackTime)
    var trackAlbum = findViewById<TextView>(R.id.trackAlbum)
    var trackYear = findViewById<TextView>(R.id.trackYear)
    var trackGenre = findViewById<TextView>(R.id.trackGenre)
    var trackCountry = findViewById<TextView>(R.id.trackCountry)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.backButtonPlayerActivity)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val json:String? = intent.getStringExtra(PLAYLIST_KEY)
        val clickedTrack: Track = Gson().fromJson(json, Track::class.java)
        trackBind(clickedTrack)


    }
    private fun trackBind(track: Track) {
        val url = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(trackPoster)
        trackAlbumName.text = track.collectionName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        trackAlbum.text= track.collectionName
        trackYear.text = track.releaseDate
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
    }
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}