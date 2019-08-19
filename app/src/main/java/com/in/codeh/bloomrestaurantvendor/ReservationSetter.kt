package com.`in`.codeh.bloomrestaurantvendor

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.`in`.codeh.bloomrestaurantvendor.fragments.CompletedFragment
import com.`in`.codeh.bloomrestaurantvendor.fragments.PendingFragment
import com.`in`.codeh.bloomrestaurantvendor.fragments.ReservedFragment
import com.`in`.codeh.bloomrestaurantvendor.model.ReservationModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.activity_reservation_setter.*
import kotlinx.android.synthetic.main.content_reservation_setter.*


class ReservationSetter : AppCompatActivity() {


    private var content: FrameLayout? = null


    lateinit var reference: DatabaseReference
    lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_setter)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        reservation_navigation_tabs.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = PendingFragment()
        addFragment(fragment)
        firebaseFirestore = FirebaseFirestore.getInstance()




    }



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.bottom_action_pending -> {
                val fragment = PendingFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_action_booked -> {
                val fragment = ReservedFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_action_completed -> {
                val fragment = CompletedFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

}
