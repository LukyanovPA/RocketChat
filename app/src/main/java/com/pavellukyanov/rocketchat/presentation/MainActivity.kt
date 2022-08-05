package com.pavellukyanov.rocketchat.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pavellukyanov.rocketchat.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState != null) {
            Timber.d("Smotrim popali")
            vm.checkAuth()
            vm.testAuth().observe(this, ::testCheck)
//        }
    }

    private fun testCheck(str: String) {
        if (str.isNotEmpty()) {
            Toast.makeText(this, "Логин заебиська!", Toast.LENGTH_LONG).show()
        }
    }
}