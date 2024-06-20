package com.example.playlistmaker2

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private lateinit var trackPoster: ImageView
    private lateinit var trackAlbumName: TextView
    private lateinit var artistName: TextView
    private lateinit var timer: TextView
    private lateinit var trackTime : TextView
    private lateinit var trackAlbum : TextView
    private lateinit var trackYear : TextView
    private lateinit var trackGenre : TextView
    private lateinit var trackCountry : TextView
    private lateinit var clickedTrack : Track
    private lateinit var playButton: ImageButton

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())


    private companion object {
        const val TRACK_KEY = "track"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_DELAY = 400L

    }

    private val trackTimer = object : Runnable {
        override fun run() {
            val time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            timer.text = time
            handler.postDelayed(this, TIME_DELAY)
        }
        
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.backButtonPlayerActivity)
        backButton.setOnClickListener {
            handler.removeCallbacksAndMessages(trackTimer)
            onBackPressedDispatcher.onBackPressed()
        }

         trackPoster = findViewById<ImageView> (R.id.trackPoster)
         trackAlbumName = findViewById<TextView>(R.id.trackAlbumName)
         artistName = findViewById<TextView>(R.id.artist_name)
         timer = findViewById<TextView>(R.id.timer)
         trackTime = findViewById<TextView>(R.id.trackTime)
         trackAlbum = findViewById<TextView>(R.id.trackAlbum)
         trackYear = findViewById<TextView>(R.id.trackYear)
         trackGenre = findViewById<TextView>(R.id.trackGenre)
         trackCountry = findViewById<TextView>(R.id.trackCountry)
         playButton = findViewById<ImageButton>(R.id.playButton)

        val json = intent?.getStringExtra(TRACK_KEY)
        clickedTrack = Gson().fromJson(json, Track::class.java)


        val url = clickedTrack?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(trackPoster)


        trackAlbumName.text = clickedTrack?.collectionName
        artistName.text = clickedTrack?.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(clickedTrack?.trackTime?.toLong())
        trackAlbum.text= clickedTrack?.collectionName
        trackYear.text = clickedTrack?.releaseDate?.substring(0, 4)
        trackGenre.text = clickedTrack?.primaryGenreName
        trackCountry.text = clickedTrack?.country

        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pause()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(clickedTrack?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {

            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler?.removeCallbacksAndMessages(null)
            timer.text = getString(R.string.trackTime)
            playButton.setImageResource(R.drawable.play)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler?.post(trackTimer)
    }

    private fun pause() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler?.removeCallbacksAndMessages(null)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}


