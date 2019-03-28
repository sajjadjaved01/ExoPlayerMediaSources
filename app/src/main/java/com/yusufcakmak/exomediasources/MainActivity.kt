package com.yusufcakmak.exomediasources

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnExtractor.setOnClickListener {
            val intent = Intent(this, ExtractorMediaSourceActivity::class.java)
            startActivity(intent)
        }

        btnClipping.setOnClickListener {
            val intent = Intent(this, ClippingMediaSourceActivity::class.java)
            startActivity(intent)
        }

        btnLooping.setOnClickListener {
            val intent = Intent(this, LoopingMediaSourceActivity::class.java)
            startActivity(intent)
        }

        btnMerging.setOnClickListener {
            val intent = Intent(this, MergingMediaSourceActivity::class.java)
            startActivity(intent)
        }

        btnConcatenating.setOnClickListener {
            val intent = Intent(this, ConcatenatingMediaSourceActivity::class.java)
            startActivity(intent)
        }
    }
}
