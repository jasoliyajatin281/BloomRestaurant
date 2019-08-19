package com.`in`.codeh.bloomrestaurantvendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ScannedBarCode : AppCompatActivity() {

    var userUiD = ""

    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var documentReference: DocumentReference

    internal var addPointsBtn: Button? = null

    internal var doneButton: Button? = null

    internal var redeemButton: Button? = null;

    var points: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_bar_code)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        userUiD = intent.getStringExtra("code")


        firebaseFirestore = FirebaseFirestore.getInstance()
        documentReference = firebaseFirestore.collection("Users").document(userUiD)

        addPointsBtn = findViewById(R.id.add_points)
        doneButton = findViewById(R.id.scannedcard_done_button)

        updateView()

        addPointsBtn!!.setOnClickListener {

            if(userUiD != "") {
                val intent = Intent(this@ScannedBarCode, AddPoints::class.java)
                intent.putExtra("userUID", userUiD)
                startActivity(intent)
            }
        }

        doneButton?.setOnClickListener {
            startActivity(Intent(this@ScannedBarCode, MainActivity::class.java))
            finish()
        }
    }

    fun updateView(){
        documentReference.get().addOnCompleteListener {
                task ->
            if(task.isSuccessful) {
                var documentSnapshot = task.getResult()
                var name = documentSnapshot?.get("Name")
                points = documentSnapshot?.get("Points")
                val textViewName = findViewById<TextView>(R.id.scanned_name)
                val textViewPoints = findViewById<TextView>(R.id.scanned_points)
                textViewName.text = "Name: "+ name
                textViewPoints.text = "Points: " + points
            }
        }
        redeemButton = findViewById(R.id.redeem_points)
        redeemButton!!.setOnClickListener {
            if(points.toString().toInt() >= 10){
                val intent = Intent(this@ScannedBarCode, RedeemPoints::class.java)
                intent.putExtra("userUID", userUiD)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Insufficient Points",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateView()
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }
}
