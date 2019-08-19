package com.`in`.codeh.bloomrestaurantvendor.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.`in`.codeh.bloomrestaurantvendor.R
import com.`in`.codeh.bloomrestaurantvendor.model.OrdersModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrdersFirestoreAdapter internal constructor(options: FirestoreRecyclerOptions<OrdersModel>): FirestoreRecyclerAdapter<OrdersModel, OrderViewHolder>(options){

    var context: Context? = null
    var firebaseFirestore = FirebaseFirestore.getInstance()
    var name: String = ""
    lateinit var documentReference: DocumentReference
    lateinit var query: Query
    override fun onBindViewHolder(ordersViewHolder: OrderViewHolder, position: Int, ordersModel: OrdersModel) {
        var orderID: String = ordersModel.orderID
        var userUID: String = ordersModel.userUID
        documentReference = firebaseFirestore.collection("Users").document(userUID)
        documentReference.get().addOnCompleteListener { task ->
            val documentSnapshot = task.result
            name = documentSnapshot!!.get("Name").toString()
        }
        ordersViewHolder.setData(ordersModel.orderNumber,ordersModel.date, ordersModel.pickupTime, name)
        ordersViewHolder.textViewChangeStatus.setOnClickListener { View ->
            //val intent = Intent(context, ReservationDetail::class.java)
            //intent.putExtra("reservationID",reservationID)
            //context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        context = parent.context
        return OrderViewHolder(view)
    }


}
public class OrderViewHolder internal constructor(private val view: View): RecyclerView.ViewHolder(view){

    val textViewName = view.findViewById<TextView>(R.id.order_name_resact)
    val textViewReservationID = view.findViewById<TextView>(R.id.order_id_text)
    val textViewStatus = view.findViewById<TextView>(R.id.order_status)
    val textViewChangeStatus = view.findViewById<TextView>(R.id.change_status_order)
    val textViewDate = view.findViewById<TextView>(R.id.date_orderact)
    val textViewTime = view.findViewById<TextView>(R.id.time_orderact)

    internal fun setData( orderNumber: Int, date: Timestamp?, time: String, name: String){



        textViewName.setText(name)
        textViewReservationID.setText("order ID: "+ orderNumber.toString())
        textViewStatus.visibility = View.GONE
        textViewDate.setText(date?.toDate().toString())
        textViewTime.setText(time)
    }
}