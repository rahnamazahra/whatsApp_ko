package com.rahnama.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.rahnama.whatsapp.databinding.ActivityLoginBinding
import com.rahnama.whatsapp.databinding.ActivityMainBinding

private  lateinit var binding: ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

            binding.errorMsg.text =""
            binding.errorMsg.visibility=View.GONE

            auth= FirebaseAuth.getInstance()
        /******************************************************************************/
        binding.btnSignIn.setOnClickListener {
            val email=binding.editTextTextEmailAddress.text.toString()
            val password=binding.editTextTextPassword.text.toString()
            binding.errorMsg.text =""
            binding.errorMsg.visibility=View.GONE
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                loginUser(email,password)  //call function LoginUser
            }else{
                binding.errorMsg.text = "تمامی فیلدها باید پر شوند"
                binding.errorMsg.visibility=View.VISIBLE
            }
        }
        /******************************************************************************/
    }//End Oncreate
/******************************************************************************/
    private fun loginUser(email: String, password: String) {
       auth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener {
           task: Task<AuthResult>->
           if(task.isSuccessful){
               val intentDashbord= Intent(this,DashbordActivity::class.java)
               startActivity(intentDashbord)
               finish()
           }else
           {
               Toast.makeText(this,"متاسفیم، مشکلی پیش آمده است",Toast.LENGTH_LONG).show()
           }
       }
    }//End function
    /******************************************************************************/
}//End Class