package com.san22.clonewhatsapp

import android.app.ActivityOptions
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
class SplashActivity : AppCompatActivity() {
    private val auth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window)
        {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Fade()
            enterTransition = Fade()

            // shows the transition for 2 seconds
            exitTransition.duration = 1000
        }

        setContentView(R.layout.activity_splash)

        val mycounter=object :CountDownTimer(750,1)
        {
            override fun onTick(p0: Long) {
               //nothing
            }

            override fun onFinish() {
                direct()
            }

        }.start()



    }

    private fun direct() {
        if (auth.currentUser==null)
        { startActivity(Intent(this,LoginActivity::class.java).addFlags(FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
        else
        {
            startActivity(Intent(this,MainActivity::class.java).addFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

        }
    }
}