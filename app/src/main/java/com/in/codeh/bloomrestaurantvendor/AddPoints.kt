package com.`in`.codeh.bloomrestaurantvendor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.HashMap

class AddPoints : AppCompatActivity() {

    internal var totalAmountSpent: EditText? = null
    internal var pointsAdded: TextView? = null
    internal var date: TextView? = null
    internal var receiptNumber: EditText? = null

    internal var userName: TextView? = null
    internal var cardID: TextView? = null

    internal var receiptImage: Button? = null

    internal var buttonUpdatePoints: Button? = null

    var userUiD = ""
    var name: String? = null

    var amount: Int? = null

    var previousPoints: Int? = null

    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var documentReference: DocumentReference

    val REQUEST_IMAGE_CAPTURE = 1


    private val PERMISSION_REQUEST_CODE: Int = 101

    private var mCurrentPhotoPath: String? = null;

    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_points)
        userUiD = intent.getStringExtra("userUID")
        totalAmountSpent = findViewById(R.id.points_amount_spent)
        date = findViewById(R.id.points_date)
        receiptNumber = findViewById(R.id.points_receipt_number)
        userName = findViewById(R.id.points_user)
        cardID = findViewById(R.id.points_card_id)
        receiptImage = findViewById(R.id.points_receipt_image)

        receiptImage?.setOnClickListener {
            //takePicture()
        }

        buttonUpdatePoints = findViewById(R.id.button_update_points)

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

        buttonUpdatePoints?.setOnClickListener {

            val receiptNumberValue = receiptNumber?.text.toString()


            if(TextUtils.isEmpty(totalAmountSpent!!.text) || TextUtils.isEmpty(receiptNumber!!.text) ){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else {
                amount = totalAmountSpent!!.text.toString().toInt()
                var roundedamount = round(amount!!)
                val pointMap = HashMap<String, Any>()
                pointMap.put("Total Amount",amount!!)
                pointMap.put("Points Added",roundedamount)
                pointMap.put("Receipt Number", receiptNumberValue)
                pointMap.put("Date",Timestamp.now())
                pointMap.put("Option","Added")
                //pointMap.put("photoUri",uri!!.lastPathSegment)
                firebaseFirestore.collection("Users").document(userUiD).collection("PointsHistory").document().set(pointMap)
                    .addOnCompleteListener {
                   task ->
                        if(task.isSuccessful){
                            var totalpoints = (previousPoints?.plus(roundedamount))
                            firebaseFirestore.collection("Users").document(userUiD).update("Points",totalpoints)
                                .addOnCompleteListener {
                                task ->
                                    if(task.isSuccessful){
                                        Toast.makeText(this,"Points Added Successfully",Toast.LENGTH_SHORT).show()
                                        super.onBackPressed()
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
        return if (n - a > b - n) b else a
    }
}
