package com.dsm.tud

// LoadingScreenActivity.kt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dsm.tud.MainActivity
import com.dsm.tud.R

class LoadingScreenActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    private lateinit var loadingImage: ImageView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        progressBar = findViewById(R.id.progress_bar)
        loadingText = findViewById(R.id.loading_text)
        loadingImage = findViewById(R.id.loading_image)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        loadingImage.startAnimation(fadeIn)
        progressBar.startAnimation(fadeIn)
        loadingText.startAnimation(fadeIn)

        handler.postDelayed({
            startActivity(Intent(this@LoadingScreenActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 3000)
    }
}
