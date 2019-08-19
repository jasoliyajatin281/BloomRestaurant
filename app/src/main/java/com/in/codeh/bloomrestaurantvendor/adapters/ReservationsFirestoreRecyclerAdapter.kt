package com.`in`.codeh.bloomrestaurantvendor.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.`in`.codeh.bloomrestaurantvendor.R
import com.`in`.codeh.bloomrestaurantvendor.detail.ReservationDetail
import com.`in`.codeh.bloomrestaurantvendor.model.ReservationModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

public class ReservationsFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<ReservationModel>): FirestoreRecyclerAdapter<ReservationModel, ReservationsViewHolder>(options){

    var context: Context? = null
    override fun onBindViewHolder(reservationsViewHolder: ReservationsViewHolder, position: Int, reservationModel: ReservationModel) {
        var reservationID: String = reservationModel.reservationID
        reservationsViewHolder.setData(reservationModel.name,reservationModel.reservationNumber,reservationModel.quantity,reservationModel.confirmation,reservationModel.date,reservationModel.time)
        reservationsViewHolder.textViewChangeStatus.setOnClickListener { View ->
            val intent = Intent(context, ReservationDetail::class.java)
            intent.putExtra("reservationID",reservationID)
            context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reservation_item, parent, false)
        context = parent.context
        return ReservationsViewHolder(view)
    }

}

public class ReservationsViewHolder internal constructor(private val view: View): RecyclerView.ViewHolder(view){

    val textViewName = view.findViewById<TextView>(R.id.user_name_resact)
    val textViewReservationID = view.findViewById<TextView>(R.id.reservation_id_text)
    val textViewQuantity = view.findViewById<TextView>(R.id.peep_count_resact)
    val textViewStatus = view.findViewById<TextView>(R.id.reservation_status)
    val textViewChangeStatus = view.findViewById<TextView>(R.id.change_status)
    val textViewDate = view.findViewById<TextView>(R.id.date_resact)
    val textViewTime = view.findViewById<TextView>(R.id.time_resact)

    internal fun setData(name: String, reservationNumber: Int, quantity: Int, status: String, date: String, time: String){


        textViewName.setText("Guest Name: "+name)
        textViewReservationID.setText("Reservation ID: "+reservationNumber.toString())
        textViewQuantity.setText(quantity.toString())
        textViewStatus.setText(status)
        textViewDate.setText(date)
        textViewTime.setText(time)
    }
}

