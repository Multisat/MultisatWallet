package io.multisat.wallet

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.multisat.wallet.scripts.SecureStorage

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var secureStorage: SecureStorage

    private fun startAnimation(targetView: View, animatorResId: Int, onEnd: (() -> Unit)? = null) {
        val animator = AnimatorInflater.loadAnimator(this, animatorResId) as AnimatorSet
        animator.setTarget(targetView)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onEnd?.invoke()
            }
        })
        animator.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        secureStorage = SecureStorage(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val logoView = findViewById<ImageView>(R.id.logo_splash_screen)

            startAnimation(logoView, R.animator.rotate_and_expand) {
                Handler(Looper.getMainLooper()).postDelayed({
                    // Decide which activity to start after the delay
                    val intent = if (secureStorage.load("password") != null) {
                        Intent(this, LoginActivity::class.java)
                    } else {
                        Intent(this, RegisterActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }, 2000) // 2-second delay
            }

            // Start other animations without additional actions
            val appNameView = findViewById<TextView>(R.id.splash_screen_app_name)
            val sloganNameView = findViewById<TextView>(R.id.slogan_splash_screen)
            val versionAppView = findViewById<TextView>(R.id.app_version_splash_screen)

            startAnimation(appNameView, R.animator.slide_up_and_expand)
            startAnimation(sloganNameView, R.animator.slide_up_and_expand)
            startAnimation(versionAppView, R.animator.expand)

        }, 0) // Start the animation immediately
    }
}