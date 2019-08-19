package com.`in`.codeh.bloomrestaurantvendor.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.`in`.codeh.bloomrestaurantvendor.R
import com.`in`.codeh.bloomrestaurantvendor.adapters.ReservationsFirestoreRecyclerAdapter
import com.`in`.codeh.bloomrestaurantvendor.detail.ReservationDetail
import com.`in`.codeh.bloomrestaurantvendor.model.ReservationModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PendingFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var firestore: FirebaseFirestore
    lateinit var documentReference: DocumentReference
    lateinit var query: Query

    private var adapter: ReservationsFirestoreRecyclerAdapter? = null

    private var mAuth = FirebaseAuth.getInstance()

    private var firebaseUser: FirebaseUser? = null

    private var pending: String = "Pending"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_pending,container,false)
        val activity = activity as Context
        recyclerView = view.findViewById(R.id.recyclerview_pending)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        firestore = FirebaseFirestore.getInstance()
        query = firestore!!.collection("Reservations").whereEqualTo("confirmation",pending).orderBy("timestamp",Query.Direction.ASCENDING);
        val options = FirestoreRecyclerOptions.Builder<ReservationModel>().setQuery(query,ReservationModel::class.java).build()

        mAuth = FirebaseAuth.getInstance()



        firebaseUser = mAuth.currentUser

        adapter = ReservationsFirestoreRecyclerAdapter(options)
        recyclerView.adapter = adapter
        return view

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
