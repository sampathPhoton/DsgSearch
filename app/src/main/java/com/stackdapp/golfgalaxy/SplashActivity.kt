package com.stackdapp.golfgalaxy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.stackdapp.golfgalaxy.search.ui.main.model.view.ProductSearchActivity

class SplashActivity : AppCompatActivity() {
    companion object {
        const val SPLASH_TIME_OUT: Long = 2000
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, ProductSearchActivity::class.java))
        }, SPLASH_TIME_OUT)
    }
}