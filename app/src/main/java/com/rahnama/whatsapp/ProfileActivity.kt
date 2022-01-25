package com.rahnama.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rahnama.whatsapp.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

private lateinit var binding: ActivityProfileBinding
class ProfileActivity : AppCompatActivity() {
    var profileLink:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*********************************************************/

        if(intent.extras!=null){
            profileLink=intent.extras!!.getString("profileLink")
            Picasso.with(this).load(profileLink).placeholder(R.drawable.profile_img)
                .into(binding.profileImageUser)

        }
    }



}