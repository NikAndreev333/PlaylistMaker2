package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SearchActivity: AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var clearButton: ImageView
    private var request: String? = SEARCH_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editText = findViewById<EditText>(R.id.edit_text)
        clearButton = findViewById<ImageView>(R.id.clearSearchButton)
        clearButton.visibility = View.GONE

        val backToMain = findViewById<ImageButton>(R.id.backButtonSearchActivity)
        backToMain.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        clearButton.setOnClickListener {
            editText.setText("")
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