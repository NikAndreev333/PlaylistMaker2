package com.example.playlistmaker2
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backToMain = findViewById<ImageButton>(R.id.back_Button)
        backToMain.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val shareButton = findViewById<ImageView>(R.id.appShareButton)
        shareButton.setOnClickListener {
            appShare()
        }
        val supportButton = findViewById<ImageView>(R.id.supportButton)
        supportButton.setOnClickListener{
            writeToSupport()
        }
        val agreementButton = findViewById<ImageView>(R.id.userAgreementButton)
        agreementButton.setOnClickListener{
            openUserAgreement()
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        if ((applicationContext as App).darkTheme) {
            themeSwitcher.setChecked(true);
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }


    }
    private fun openUserAgreement () {
        val url = getString(R.string.user_agreement_url)
        val openBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(openBrowser)
    }
    private fun appShare () {
        val share = Intent(Intent.ACTION_SEND)
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_share_url))
        share.setType("text/plain")
        startActivity(Intent.createChooser(share, "ShareApp"))
    }
    private fun writeToSupport () {
        val myEmail = getString(R.string.myEmail)
        val text = getString(R.string.supportMessageText)
        val subject = getString(R.string.supportMessageSubject)
        val email = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        startActivity(email)
    }

}