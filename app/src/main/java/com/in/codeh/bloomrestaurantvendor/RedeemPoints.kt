package com.`in`.codeh.bloomrestaurantvendor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RedeemPoints : AppCompatActivity() {

    internal var totalAmountSpent: EditText? = null
    internal var date: TextView? = null
    internal var receiptNumber: EditText? = null

    internal var userName: TextView? = null
    internal var cardID: TextView? = null


    internal var buttonRedeemPoints: Button? = null

    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var documentReference: DocumentReference


    var userUiD = ""
    var name: String? = null

    var amount: Int? = null

    var previousPoints: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem_points)

        userUiD = intent.getStringExtra("userUID")
        totalAmountSpent = findViewById(R.id.points_redeem_spent)
        date = findViewById(R.id.redeem_date)
        receiptNumber = findViewById(R.id.redeem_receipt_number)
        userName = findViewById(R.id.redeem_user)
        cardID = findViewById(R.id.redeem_card_id)

        buttonRedeemPoints = findViewById(R.id.button_redeem_points)

        firebaseFirestore = FirebaseFirestore.getInstance()
        documentReference = firebaseFirestore.collection("Users").document(userUiD)
        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                var documentSnapshot =  task.getResult()
                name =  documentSnapshot?.get("Name").toString()
                previousPoints = documentSnapshot?.get("Points").toString().toInt()

                userName!!.text = name
            }
        }

        var currentTime = Timestamp.now().toDate().toString()
        date?.text = currentTime

        cardID?.text = userUiD

        buttonRedeemPoints?.setOnClickListener {

            val receiptNumberValue = receiptNumber?.text.toString()


            if(TextUtils.isEmpty(totalAmountSpent!!.text) || TextUtils.isEmpty(receiptNumber!!.text)){
                Toast.makeText(this,"Please fill all the details", Toast.LENGTH_SHORT).show()
            }else {
                amount = totalAmountSpent!!.text.toString().toInt()
                var roundedamount = (round(amount!!))
                var totalpoints = (previousPoints?.minus(roundedamount))
                val pointMap = HashMap<String, Any>()
                pointMap.put("Total Amount",amount!!)
                pointMap.put("Points Redeem",roundedamount)
                pointMap.put("Receipt Number", receiptNumberValue)
                pointMap.put("Date",Timestamp.now())
                pointMap.put("Option","Redeem")
                firebaseFirestore.collection("Users").document(userUiD).collection("PointsHistory").document().set(pointMap)
                    .addOnCompleteListener {
                            task ->
                        if(task.isSuccessful){

                            firebaseFirestore.collection("Users").document(userUiD).update("Points",totalpoints)
                                .addOnCompleteListener {
                                        task ->
                                    if(task.isSuccessful){
                                        Toast.makeText(this,"Points Redeemed Successfully", Toast.LENGTH_SHORT).show()
                                        onBackPressed()
                                        finish()
                                    }
                                }
                        }
                    };


            }
        }


    }

    fun round(n: Int): Int {
        // Smaller multiple
        val a = n / 10 * 10

        // Larger multiple
        val b = a + 10

        // Return of closest of two
        return if (n - a > b - n) b /10 else a / 10
    }
}
