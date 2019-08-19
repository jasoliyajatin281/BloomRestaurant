package com.`in`.codeh.bloomrestaurantvendor

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.checkerframework.checker.units.qual.Length
import java.lang.Exception

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{


    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        if (item.itemId == R.id.nav_item_two){
            val intent = Intent(this, ScannedBarCode::class.java)
            startActivity(intent)
            drawer.closeDrawers()
        }else if (item.itemId == R.id.nav_item_one){
            drawer.closeDrawer(GravityCompat.START)
        }

//        when (item.itemId) {
//            R.id.nav_item_one -> closeOptionsMenu()
//            R.id.nav_item_two -> startActivity(intent)
//            R.id.nav_item_three -> Toast.makeText(this, "Clicked item three", Toast.LENGTH_SHORT).show()
//            R.id.nav_item_four -> Toast.makeText(this, "Clicked item four", Toast.LENGTH_SHORT).show()
//        }
        return true
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    internal var btnScan: Button? = null
    internal var btnReservations: Button? = null
    internal var btnOrders: Button? = null

    private var mAuth = FirebaseAuth.getInstance()
    
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    internal var qrScanIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btnReservations = findViewById(R.id.reservation_button)
        btnReservations!!.setOnClickListener {
            val intent = Intent(this,ReservationSetter::class.java)
            startActivity(intent)
        }
        btnOrders = findViewById(R.id.orders_button)
        btnOrders!!.setOnClickListener {
            val intent = Intent(this,ManageOrders::class.java)
            val activityOptions: ActivityOptions = ActivityOptions.makeCustomAnimation(this, R.anim.fui_slide_in_right,R.anim.fui_slide_out_left)
            startActivity(intent, activityOptions.toBundle())
        }

        btnScan = findViewById(R.id.scancard)
        btnScan!!.setOnClickListener { kotlin.run { var integrator = IntentIntegrator(this@MainActivity)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(true)
            integrator.setCameraId(0)
            integrator.initiateScan() }}

        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()
        mAuth.addAuthStateListener {
            if (mAuth.currentUser == null){
                val intent = Intent(this@MainActivity, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val options = ActivityOptions.makeCustomAnimation(this,R.anim.fui_slide_in_right,R.anim.fui_slide_out_left)
                startActivity(intent, options.toBundle())
                finish()
            }
        }



    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result != null){
            if(result.contents == null){
                Toast.makeText(this, "No result", Toast.LENGTH_LONG).show()
            }else {
                try {
                    val obj = result.contents
                    val intent = Intent(this,ScannedBarCode::class.java)
                    intent.putExtra("code",obj)
                    startActivity(intent)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        toggle.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
