package com.example.dynamicfeature

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DfMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_df_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btn).setOnClickListener {
            com.example.myapplication.MainBlankFragment.newInstance("", "").show(supportFragmentManager, "dialog")
        }

        findViewById<Button>(R.id.btn1).setOnClickListener {
            com.example.mylibrary.LibBlankFragment.newInstance("", "").show(supportFragmentManager, "dialog")
        }
    }
}