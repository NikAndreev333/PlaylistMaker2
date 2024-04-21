package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R.id
import com.example.playlistmaker2.R.layout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity: AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var trackList: ArrayList<Track>
    private lateinit var trackListAdapter: TrackAdapter
    private lateinit var trackRV: RecyclerView
    private lateinit var notFoundPlaceholder: LinearLayout
    private lateinit var noConnectionPlaceholder: LinearLayout
    private lateinit var updateButton: Button
    private var request: String? = SEARCH_STRING
    private val baseItunesURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseItunesURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesAPI = retrofit.create(ITunesAPI::class.java)

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
        trackListAdapter = TrackAdapter(trackList)




        val backToMain = findViewById<ImageButton>(id.backButtonSearchActivity)
        backToMain.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        clearButton.setOnClickListener {
            editText.setText("")
            trackList.clear()
            trackRV.isVisible = false
            trackListAdapter.notifyDataSetChanged()
            hideKeyboard()
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
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
    }
    private fun trackSearch() {
        iTunesAPI.search(editText.text.toString()).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200){

                    if (response.body()?.results?.isNotEmpty() == true){
                        trackList.clear()
                        trackRV.isVisible = true
                        trackList.addAll(response.body()?.results!!)
                        if (trackList.isNotEmpty()) {
                            val text = "Пора покормить кота!"
                            val duration = Toast.LENGTH_SHORT

                            val toast = Toast.makeText(applicationContext, text, duration)
                            toast.show()
                        }
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
    companion object {
        const val SEARCH_KEY = "SEARCH_KEY"
        const val SEARCH_STRING = ""
    }

}