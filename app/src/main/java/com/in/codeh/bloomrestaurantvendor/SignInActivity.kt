package com.`in`.codeh.bloomrestaurantvendor

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    lateinit var mDatabase: FirebaseFirestore
     var firebaseAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    var authListener = FirebaseAuth.AuthStateListener {  }
    private var signInbtn: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        mDatabase = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()


        authListener = FirebaseAuth.AuthStateListener {
            if(firebaseAuth.currentUser != null){
                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val options = ActivityOptions.makeCustomAnimation(this,R.anim.fui_slide_in_right,R.anim.fui_slide_out_left)
                startActivity(intent, options.toBundle())
                finish()
            }
        }


        signInbtn = findViewById(R.id.sign_in_imageview)
        signInbtn!!.setOnClickListener { signin() }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        if(authListener !=null){
            firebaseAuth.removeAuthStateListener(authListener)
        }
    }
    private fun signin(){
        val emailtext = findViewById<AutoCompleteTextView>(R.id.login_email)
        val passwordText = findViewById<EditText>(R.id.login_password)

        var email = emailtext.text.toString()
        var password = passwordText.text.toString()

        if(!email.isEmpty() || !password.isEmpty()){

            if(email.toLowerCase() == "admin@bloom.ca" && password.toLowerCase() == "admin123"){
                firebaseAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    task ->

                    if (task.isSuccessful){
                        firebaseAuth.addAuthStateListener(authListener)
                    }
                    else {
                        Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    }
}
