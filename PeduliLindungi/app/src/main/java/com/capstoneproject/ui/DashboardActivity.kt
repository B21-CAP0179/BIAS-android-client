package com.capstoneproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import com.capstoneproject.R
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val rl_screening:RelativeLayout = findViewById(R.id.rl_screening)
        val rl_history:RelativeLayout = findViewById(R.id.rl_history)
        val rl_about_us:RelativeLayout = findViewById(R.id.rl_about_us)
        val rl_logout:RelativeLayout = findViewById(R.id.rl_logout)


        rl_screening.setOnClickListener {

        }

        rl_history.setOnClickListener {

        }

        rl_about_us.setOnClickListener {
            startActivity(Intent(applicationContext, AboutActivity::class.java))
        }

        rl_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            Toast.makeText(this,"Logout success.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}