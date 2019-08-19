package com.`in`.codeh.bloomrestaurantvendor.detail

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.`in`.codeh.bloomrestaurantvendor.R
import com.`in`.codeh.bloomrestaurantvendor.model.ReservationModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import org.w3c.dom.Text
import java.util.*

class ReservationDetail : AppCompatActivity(), EventListener<DocumentSnapshot> {
    override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
        if(p0!!.exists())
        onReservationLoaded(p0.toObject(ReservationModel::class.java))
    }


    lateinit var reservationID: String
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var documentReference: DocumentReference
    lateinit var firebaseAuth: FirebaseAuth
    var currentUserID: String? = null

    var reservationModel: ReservationModel? = null


    internal var reservationNumber: TextView? = null
    internal var reservationStatusData: TextView? = null
    internal var reservationName: TextView? = null
    internal var reservationDate: TextView? = null
    internal var reservationTime: TextView? = null
    internal var reservationQuantity: TextView? = null
    internal var reservationBookedTime: TextView? = null
    internal var reservationIDText: TextView? = null
    internal var reservationStatusSpinner: Spinner? = null
    internal var changeStatusButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_detail)

        reservationNumber = findViewById(R.id.reservation_number_info)
        reservationStatusData = findViewById(R.id.reservation_status_data)
        reservationName = findViewById(R.id.res_spot_name_info)
        reservationDate = findViewById(R.id.res_date_info)
        reservationTime = findViewById(R.id.res_time_info)
        reservationQuantity = findViewById(R.id.res_foodiecount_info)
        reservationBookedTime = findViewById(R.id.res_booked_time)
        reservationStatusSpinner = findViewById(R.id.res_status_info_spinner)
        changeStatusButton = findViewById(R.id.change_status_button)
        reservationIDText = findViewById(R.id.reservation_id_info)

        changeStatusButton!!.setOnClickListener { changeReservationStatus() }

        reservationID = intent.getStringExtra("reservationID")

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser != null){
            currentUserID = firebaseAuth.currentUser!!.uid
        }
        documentReference = firebaseFirestore.collection("Reservations").document(reservationID)

        documentReference.addSnapshotListener(this)

    }

    fun changeReservationStatus(){

        val spinnerSelected = reservationStatusSpinner?.selectedItem.toString()

        if(spinnerSelected != "status") {

            val alertDialogInternet = AlertDialog.Builder(this@ReservationDetail)
            alertDialogInternet.setMessage("Are you sure you want to change Reservation Status?")
            alertDialogInternet.setTitle("Change Status?").setCancelable(false).setPositiveButton(
                "Yes"
            ) { dialog, which ->

                val firebaseFirestoreChange = FirebaseFirestore.getInstance()
                val documentRefChange = firebaseFirestoreChange.collection("Reservations").document(reservationID)
                documentRefChange.update("confirmation", spinnerSelected).addOnSuccessListener {
                    Toast.makeText(this,"Updated Successfully!!",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    finish()
                }

            }.setNegativeButton(
                "No"
            ) { dialog, which -> dialog.dismiss() }
            val dialog = alertDialogInternet.create()
            dialog.show()



        }else {
            Toast.makeText(this,"Please select correct value",Toast.LENGTH_SHORT).show()
        }

    }

    fun onReservationLoaded(reservationModel: ReservationModel?){

        this.reservationModel = reservationModel

        reservationNumber?.text = reservationModel!!.reservationNumber.toString()
        reservationIDText?.text = reservationModel.reservationID
        reservationName?.text = reservationModel.name
        reservationStatusData?.text = reservationModel.confirmation
        reservationDate?.text = reservationModel.date
        reservationTime?.text = reservationModel.time
        reservationQuantity?.text = reservationModel.quantity.toString()
        var timestamp: Timestamp? = reservationModel.timestamp
        reservationBookedTime?.text = reservationModel.timestamp?.toDate().toString()

    }


}
