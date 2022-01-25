package com.rahnama.whatsapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.rahnama.whatsapp.databinding.ActivityCreateAccountBinding
import com.google.firebase.database.DatabaseReference;
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


private  lateinit var binding: ActivityCreateAccountBinding
class CreateAccountActivity : AppCompatActivity() {
    private var auth: FirebaseAuth?=null
    private var database :DatabaseReference?=null
    /*************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        binding.errorMsg.text =""
        binding.errorMsg.visibility= View.GONE
        /*************************************************************************************/
        binding.btnCreateAccount.setOnClickListener {

            //get text input
            val email=binding.editTextTextEmailAddress.text.toString().trim()
            val password=binding.editTextTextPassword.text.toString().trim()
            val displayName=binding.displayName.text.toString().trim()

            //do not show error message
            binding.errorMsg.text =""
            binding.errorMsg.visibility= View.GONE

            //if is not empty
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                CreateAccount(email,password,displayName)  //call function  CreateAccount
            }
            else{
                // if empty input show error
                binding.errorMsg.text="تمامی فیلدها باید پر شوند"
                binding.errorMsg.visibility= View.VISIBLE
            }
        }//End OnclickListener btn Create Account
        /*************************************************************************************/

    }  // End oncreate
/*************************************************************************************/
@SuppressLint("SimpleDateFormat")
fun CreateAccount(email:String, password:String, displayName:String){

           auth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
               task: Task<AuthResult>->
               if(task.isSuccessful){

                   val  CurrentUser=auth!!.currentUser
                   val userId=CurrentUser!!.uid

                   val formatDate: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                   val today = formatDate.format(Date())

                   val formatter = SimpleDateFormat("hh:mm:ss a")
                   val time = formatter.format(Date())

                   database=FirebaseDatabase.getInstance("https://whatsapp-a4e81-default-rtdb.firebaseio.com/").reference
                       .child("Users").child(userId)

                   val UserObject=HashMap<String,String>()
                   UserObject.put("Name",displayName)
                   UserObject.put("About","من از گفتمان استفاده می کنم")
                   UserObject.put("phoneNumber","defualt")
                   UserObject.put("image","defualt")
                   UserObject.put("thumb_image","defualt")
                   UserObject.put("State","online")
                   UserObject.put("Date", today.toString())
                   UserObject.put("Time",time.toString())

                   database!!.setValue(UserObject).addOnCompleteListener { task:Task<Void>->
                       if(task.isSuccessful){
                           notification()
                           val intentDashbord=Intent(this,DashbordActivity::class.java)
                           startActivity(intentDashbord)
                           finish()

                       }
                   }



               }else{

                   Toast.makeText(this, "Error: "+ task.exception.toString(), Toast.LENGTH_LONG).show();
               }

           }
    }

    /***********************************************************************/
    fun notification(){
        val channelId="notification_channel_welcome"
        val channelName="com.rahnama.whatsapp"

        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.O){

            val notificationChannel= NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH)
            val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
        val notificationBuilder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.chat_icon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentText("ورود شما رو به اپلیکیشن گفتمان تبریک می گوییم")

        val notificationManagerCompat: NotificationManagerCompat =NotificationManagerCompat.from(this)
            notificationManagerCompat.notify(0,notificationBuilder.build())


    }


    /***********************************************************************/
}