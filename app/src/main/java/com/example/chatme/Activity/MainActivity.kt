package com.example.chatme.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatme.Fragment.GetUserNumber
import com.example.chatme.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, GetUserNumber())
            .commit()
    }
}