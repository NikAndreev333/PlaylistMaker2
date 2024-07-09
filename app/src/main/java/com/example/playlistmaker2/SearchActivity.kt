package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R.id
import com.example.playlistmaker2.R.layout
import com.google.gson.Gson
import com.example.playlistmaker2.domain.model.Track
import com.example.playlistmaker2.presentation.ui.PlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity: AppCompatActivity(), Listner {
    private lateinit var editText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var trackList: ArrayList<Track>
    private lateinit var trackListAdapter: TrackAdapter
    private lateinit var trackRV: RecyclerView
    private lateinit var notFoundPlaceholder: LinearLayout
    private lateinit var noConnectionPlaceholder: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var trackHistoryList:ArrayList<Track>
    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var historyViewGroup: LinearLayout
    private lateinit var trackHistoryRV: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar
    private var request: String? = SEARCH_STRING
    private val baseItunesURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseItunesURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesAPI = retrofit.create(ITunesAPI::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchRunnable = Runnable { trackSearch() }

    companion object {
        const val SEARCH_KEY = "SEARCH_KEY"
        const val SEARCH_STRING = ""
        const val SEARCH_DELAY = 2000L
        const val CLICK_DELAY = 1000L
        const val TRACK_HISTORY_PREFS = "track_history_prefs"
        const val PLAYLIST_KEY = "playlist_key"
        const val TRACK_KEY = "track"
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)

        editText = findViewById<EditText>(id.edit_text)
        clearButton = findViewById<ImageView>(id.clearSearchButton)
        clearButton.visibility = View.GONE
        notFoundPlaceholder = findViewById<LinearLayout>(R.id.notFoundPlaceholder)
        noConnectionPlaceholder = findViewById<LinearLayout>(R.id.noConnectionPlaceholder)
        updateButton = findViewById<Button>(R.id.updateButton)
        trackRV = findViewById<RecyclerView>(R.id.trackRV)
        trackList = arrayListOf()
        trackListAdapter = TrackAdapter(trackList, this)
        trackHistoryRV = findViewById<RecyclerView>(R.id.trackHistoryRV)
        clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        historyViewGroup = findViewById<LinearLayout>(R.id.historyViewGroup)
        trackHistoryList = arrayListOf()
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val sharedPreferences = getSharedPreferences(TRACK_HISTORY_PREFS, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        trackHistoryAdapter = TrackAdapter(searchHistory.searchedTrackHistory, this)
        trackHistoryRV.layoutManager = LinearLayoutManager(this)
        trackHistoryRV.adapter = trackHistoryAdapter

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            trackHistoryAdapter.notifyDataSetChanged()
            historyViewGroup.isVisible = false
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            historyViewGroup.visibility = if (hasFocus && editText.text.isEmpty() && searchHistory.searchedTrackHistory.isNotEmpty())  View.VISIBLE else View.GONE
        }

        val backToMain = findViewById<ImageButton>(id.backButtonSearchActivity)
        backToMain.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        clearButton.setOnClickListener {
            editText.setText("")
            trackList.clear()
            trackRV.isVisible = false
            trackHistoryRV.isVisible = true
            notFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            historyViewGroup.isVisible = false
            trackListAdapter.notifyDataSetChanged()
            hideKeyboard()
            handler.removeCallbacksAndMessages(null)
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                historyViewGroup.visibility = if (editText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                searchDebounce()
                if (s.isNullOrEmpty())  handler.removeCallbacksAndMessages(null)
            }

            override fun afterTextChanged(s: Editable?) {
                request = s.toString()
            }

        }
        editText.addTextChangedListener(textWatcher)
        trackRV.layoutManager = LinearLayoutManager(this)
        trackRV.adapter = trackListAdapter
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId ==     EditorInfo.IME_ACTION_DONE) {
                trackSearch()
                true
            }
            false
        }
        updateButton.setOnClickListener {
            trackList.clear()
            noConnectionPlaceholder.isVisible = false
            trackSearch()
            trackListAdapter.notifyDataSetChanged()


        }
    }
    private fun showNotFoundPlaceholder() {
        notFoundPlaceholder.isVisible = true
    }
    private fun showNoConnectionPlaceholder () {
        noConnectionPlaceholder.isVisible = true
        notFoundPlaceholder.isVisible = false
    }
    private fun trackSearch() {

        if(editText.text.isNullOrEmpty()) {
            notFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
        }
        progressBar.visibility = View.VISIBLE
        trackRV.visibility = View.GONE
        trackHistoryRV.visibility = View.GONE
        iTunesAPI.search(editText.text.toString()).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                progressBar.visibility = View.GONE
                if (response.code() == 200){

                    if (response.body()?.results?.isNotEmpty() == true){
                        trackList.clear()
                        trackRV.isVisible = true
                        trackList.addAll(response.body()?.results!!)
                        trackListAdapter.notifyDataSetChanged()


                    } else {
                        showNotFoundPlaceholder()
                    }
                } else {
                    showNoConnectionPlaceholder()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showNoConnectionPlaceholder()
            }
        })
    }
    private fun hideKeyboard () {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        request = savedInstanceState.getString(SEARCH_KEY,SEARCH_STRING)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_KEY,request)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchHistory.saveTrack(track)
            trackHistoryAdapter.notifyDataSetChanged()
            val json = Gson().toJson(track)
            val player = Intent(this, PlayerActivity::class.java)
            player.putExtra(TRACK_KEY, json)
            startActivity(player)
        }

    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DELAY)
        }
        return current
    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(searchRunnable)
    }
}
interface Listner {
    fun onClick(track: Track) {
    }
}