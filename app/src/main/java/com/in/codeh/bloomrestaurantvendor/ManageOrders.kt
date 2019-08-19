package com.`in`.codeh.bloomrestaurantvendor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.`in`.codeh.bloomrestaurantvendor.adapters.OrdersFirestoreAdapter
import com.`in`.codeh.bloomrestaurantvendor.adapters.ReservationsFirestoreRecyclerAdapter
import com.`in`.codeh.bloomrestaurantvendor.model.OrdersModel
import com.`in`.codeh.bloomrestaurantvendor.model.ReservationModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_reservation_setter.*

class ManageOrders : AppCompatActivity() {

    lateinit var reference: DatabaseReference
    lateinit var firebaseFirestore: FirebaseFirestore

    lateinit var recyclerView: RecyclerView

    lateinit var query: Query

    private var adapter: OrdersFirestoreAdapter? = null



    private var mAuth = FirebaseAuth.getInstance()

    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_orders)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        recyclerView = findViewById(R.id.recyclerview_orders)
        recyclerView.layoutManager = LinearLayoutManager(this)
        firebaseFirestore = FirebaseFirestore.getInstance()
        query = firebaseFirestore.collection("Orders").orderBy("date",Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<OrdersModel>().setQuery(query, OrdersModel::class.java).build()

        mAuth = FirebaseAuth.getInstance()



        firebaseUser = mAuth.currentUser

        adapter = OrdersFirestoreAdapter(options)
        recyclerView.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}
