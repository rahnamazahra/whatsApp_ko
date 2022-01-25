package com.rahnama.whatsapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rahnama.whatsapp.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var authListener: FirebaseAuth.AuthStateListener? = null
    var CurrentUser: FirebaseUser? = null
/*********************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->
            CurrentUser = firebaseAuth.currentUser
            if (CurrentUser != null) {

                val dashbordIntent = Intent(this, DashbordActivity::class.java)
                startActivity(dashbordIntent)
                finish()

            }

        }

        binding.btnSignIn.setOnClickListener {
            val signinIntent = Intent(this, LoginActivity::class.java)
            startActivity(signinIntent)
        }

        binding.goNewAccount.setOnClickListener {
            val signinIntent = Intent(this, CreateAccountActivity::class.java)
            startActivity(signinIntent)
        }

    }//End Oncreate

    /***************************************************/
    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener { this.authListener!! }

    }//End On Start

    /***************************************************/
    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener { this.authListener!! }
        }

    }//End OnStop
    /***************************************************/

} //End Class
