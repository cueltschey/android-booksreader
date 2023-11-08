package com.example.android_booksreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.android_booksreader.MainActivity
import com.example.android_booksreader.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        auth = Firebase.auth
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.login)
        val userIn = findViewById<EditText>(R.id.user_in)
        val passIn = findViewById<EditText>(R.id.pass_in)


        login.setOnClickListener{
            if(TextUtils.isEmpty(userIn.text.toString())){
                Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(passIn.text.toString())){
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            }
            val email = userIn.text.toString()
            val password = passIn.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}