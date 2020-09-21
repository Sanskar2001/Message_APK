package com.san22.clonewhatsapp

import android.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.FirebaseApp
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.san22.clonewhatsapp.Adapter.ScreenSlideAdpater
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val database by lazy {
        FirebaseFirestore.getInstance()
    }
    val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setContentView(toolbar)
       viewpager.adapter=ScreenSlideAdpater(this)
      TabLayoutMediator(tabs,viewpager,TabLayoutMediator.TabConfigurationStrategy{tab, position ->
          when(position)
          {
              0->{
                  tab.text="Chats"
              }
              1->{
                  tab.text="People"
              }
          }

      }).attach()


    }
}