package com.rahnama.whatsapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rahnama.whatsapp.databinding.ActivityDashbordBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private  lateinit var binding: ActivityDashbordBinding
class DashbordActivity : AppCompatActivity() {
    var auth: FirebaseAuth?=null
    var CurrentUser: FirebaseUser?=null
     var databaseReference: DatabaseReference?=null
    /******************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashbordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        CurrentUser=auth!!.currentUser

        val sectionAdapter:ManageViewPagerAdapter = ManageViewPagerAdapter(supportFragmentManager)
        binding.MainTab.setTabTextColors(Color.WHITE,Color.GREEN)
        binding.MainTab.setSelectedTabIndicatorColor(Color.GREEN)
        binding.viepager.adapter=sectionAdapter
        binding.MainTab.setupWithViewPager(binding.viepager)
    }
/************************************************************************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       super.onCreateOptionsMenu(menu)
          menuInflater.inflate(R.menu.menu,menu)
        return true
    }
/***********************************************************************/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         super.onOptionsItemSelected(item)
         if(item.itemId==R.id.LogoutItem){
             updateUserState("offline")
             FirebaseAuth.getInstance().signOut()
             startActivity(Intent(this,MainActivity::class.java))
             finish()
         }
        else if(item.itemId==R.id.SettingItem){
             startActivity(Intent(this,SettingActivity::class.java))
        }
        return true
    }
    /***************************************************/
    override fun onStart() {
        super.onStart()

        updateUserState("online")

    }//End On Start

    /***************************************************/
    override fun onStop() {
        super.onStop()

            updateUserState("offline")


    }//End OnStop
    /***************************************************/
    override fun onDestroy() {
        super.onDestroy()

        updateUserState("offline")
    }//End OnDestroy
    /***************************************************/
    @SuppressLint("SimpleDateFormat")
    fun updateUserState(state:String){
        val formatDate: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = formatDate.format(Date())

        val formatter = SimpleDateFormat("hh:mm:ss a")
        val time = formatter.format(Date())

        val userId=CurrentUser!!.uid
        databaseReference= FirebaseDatabase.getInstance("https://whatsapp-a4e81-default-rtdb.firebaseio.com/").reference

        val updateObject=HashMap<String,Any>()
        updateObject["/Users/$userId/State"] = state
        updateObject["/Users/$userId/Date"] = date
        updateObject["/Users/$userId/Time"] = time
        databaseReference!!.updateChildren(updateObject)

    }
    /***************************************************/
}
