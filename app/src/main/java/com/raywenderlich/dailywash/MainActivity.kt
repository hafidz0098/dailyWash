package com.raywenderlich.dailywash

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.riwayat ->{
                    val intent = Intent(this, OrderActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> true
            }
        }

        val cb = findViewById<ImageView>(R.id.cucibasahimg)
        cb.setOnClickListener{
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        val ck = findViewById<ImageView>(R.id.cucikeringimg)
        ck.setOnClickListener{
            val intent = Intent(this, InputActivity2::class.java)
            startActivity(intent)
        }

    }

}