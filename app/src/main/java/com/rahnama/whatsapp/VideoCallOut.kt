package com.rahnama.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rahnama.whatsapp.databinding.ActivityVideoCallOutBinding
import com.squareup.picasso.Picasso


private lateinit var binding: ActivityVideoCallOutBinding
class VideoCallOut : AppCompatActivity(){
    var reciverUserID:String?=null
    var reciverUserName:String?=null
    var reciverprofileLink: String? = null
    var SenderUid:String?=null
    /******************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVideoCallOutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*********************************************************/

        if(intent!=null){
            reciverUserID=intent.getStringExtra("reciverUserId")
            reciverUserName=intent.getStringExtra("reciver_name")
            reciverprofileLink = intent.extras!!.getString("profileLink")
            SenderUid=intent.extras!!.getString("SenderUid")

            binding.usernameVideoOutingCall.text = reciverUserName
            Picasso.with(this).load(reciverprofileLink).placeholder(R.drawable.profile_img)
                .into(binding.imageUserVideoOutingCall)
        }
        /***********************************************************/

        sendCallInvitation()  //call function for call
    } //End Oncreate
    /***********************************************************/
    private fun sendCallInvitation() {

        Handler(Looper.getMainLooper()).postDelayed({

        },1000)

    }//End

    /***********************************************************/
}