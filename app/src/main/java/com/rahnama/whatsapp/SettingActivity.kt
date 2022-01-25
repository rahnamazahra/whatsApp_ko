package com.rahnama.whatsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rahnama.whatsapp.databinding.ActivitySettingBinding
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File

private  lateinit var binding: ActivitySettingBinding
class SettingActivity : AppCompatActivity() {
    private var auth: FirebaseAuth?=null
    private var databaseReference:DatabaseReference?=null
    var CurrentUser:FirebaseUser?=null
    var storage:StorageReference?=null
    var Name:String?=null
    var About:String?=null
    var phoneNumber:String?=null
    var thumb_image:String?=null
    var image:String?=null
    var GalleryId:Int=1

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
 /*************************************************************************/
        storage=FirebaseStorage.getInstance().reference
        CurrentUser=FirebaseAuth.getInstance().currentUser
        val userId=CurrentUser!!.uid

        databaseReference=FirebaseDatabase.getInstance("https://whatsapp-a4e81-default-rtdb.firebaseio.com/").reference


/*************************************************************************/
        databaseReference!!.child("Users")
            .child(userId).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Name=snapshot.child("Name").value.toString()
                About=snapshot.child("About").value.toString()
                phoneNumber=snapshot.child("phoneNumber").value.toString()
                image=snapshot.child("image").value.toString()
                thumb_image=snapshot.child("thumb_image").value.toString()

                binding.displayNameUser.text=Name
                binding.phoneUser.text=phoneNumber
                binding.AboutUser.text=About

                if(image!! != "defualt"){
                    Picasso.with(applicationContext).load(image).into(binding.profileImage)
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
/*************************************************************************/
        binding.changeName.setOnClickListener {
            val AlertDialog=AlertDialog.Builder(this)
            val layoutAlert=layoutInflater.inflate(R.layout.edit_info_profile,null)
            val name=layoutAlert.findViewById<EditText>(R.id.edit_Name)
            AlertDialog.setView(layoutAlert)
            AlertDialog.setCancelable(false)
            AlertDialog.setPositiveButton("ذخیره") { _, _ ->
                name.setText(Name)
                val newName = name.text.toString().trim()


                databaseReference!!.child("Users").child(userId).child("Name").setValue(newName)
                .addOnCompleteListener { task: Task<Void> ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "updated", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "No update", Toast.LENGTH_LONG).show()

                    }
                }
            }
            AlertDialog.setNegativeButton("بیخیال"){
                    _,_ ->


            }


            val Alert=AlertDialog.create()
            Alert.show()

        }
 /******************************************************/
        binding.changePhone.setOnClickListener {  val AlertDialog=AlertDialog.Builder(this)
            val layoutAlert=layoutInflater.inflate(R.layout.edit_info_profile,null)
            val edittext=layoutAlert.findViewById<EditText>(R.id.edit_Name)
            AlertDialog.setView(layoutAlert)
            AlertDialog.setCancelable(false)
            AlertDialog.setPositiveButton("ذخیره"){
                    _,_ ->
                edittext.setText(phoneNumber)
                val newvalue=edittext.text.toString().trim()

                databaseReference!!.child("Users").child(userId).child("phoneNumber").setValue(newvalue)



            }

            AlertDialog.setNegativeButton("بیخیال"){
                    _,_ ->


            }
            val Alert=AlertDialog.create()
            Alert.show()
        }
/*****************************************************************/
        binding.changeAbout.setOnClickListener {  val AlertDialog=AlertDialog.Builder(this)
            val layoutAlert=layoutInflater.inflate(R.layout.edit_info_profile,null)
            val edittext=layoutAlert.findViewById<EditText>(R.id.edit_Name)
            AlertDialog.setView(layoutAlert)
            AlertDialog.setCancelable(false)
            AlertDialog.setPositiveButton("ذخیره"){
                    _,_ ->
                edittext.setText(About)
                val newvalue=edittext.text.toString().trim()
                databaseReference!!.child("Users").child(userId).child("About").setValue(newvalue)



            }
            AlertDialog.setNegativeButton("بیخیال"){
                    _,_ ->


            }
            val Alert=AlertDialog.create()
            Alert.show()
        }
/*******************************************************************/
        binding.profileImage.setOnClickListener {
            val intent= Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"choos image"),GalleryId)
        }
 /*******************************************************************/
    } //end Oncreat
 /*******************************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) :Unit {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GalleryId && resultCode == RESULT_OK) {
            val image: Uri? = data!!.data
            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }
     //import com.theartofdev.edmodo.cropper.CropImage
        //return after crop and send request code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                val Userid = CurrentUser!!.uid

                val thumbFile = File(resultUri.path)
                val bitmap: Bitmap = Compressor(this)
                    .setMaxHeight(200) //Set height and width
                    .setMaxWidth(200)
                    .setQuality(100) // Set Quality
                    .compressToBitmap(thumbFile)

                val byteArray = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)

                val bytes: ByteArray = byteArray.toByteArray()

                val imagemainpath = storage!!.child("chat_profile_image")
                    .child(Userid + ".jpg")

                val thumbimagepath = storage!!.child("chat_profile_image")
                    .child("thumbs")
                    .child(Userid + ".jpg")

                imagemainpath.putFile(resultUri)
                    .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                        if (task.isSuccessful) {
                            imagemainpath.downloadUrl.addOnSuccessListener {
                                uri:Uri? ->
                                if(uri!=null){
                                    val downloadUri=uri.toString()

                                    val uploadTask:UploadTask=thumbimagepath.putBytes(bytes)
                                    uploadTask.addOnCompleteListener { task:Task<UploadTask.TaskSnapshot>->
                                        thumbimagepath.downloadUrl.addOnSuccessListener {
                                                uri:Uri? ->
                                            if(uri!=null){
                                                val downloadthumbUri=uri.toString()
                                                if(task.isSuccessful){
                                                    val updateObject=HashMap<String,Any>()
                                                    updateObject["/Users/$Userid/image"] =
                                                        downloadUri
                                                    updateObject["/Users/$Userid/thumb_image"] =
                                                        downloadthumbUri
                                                    databaseReference!!.updateChildren(updateObject).addOnCompleteListener {
                                                        task:Task<Void>->
                                                        if(task.isSuccessful){
                                                            Toast.makeText(this,"succes",Toast.LENGTH_LONG).show()

                                                        }
                                                    }

                                                    }
                                                }

                                            }
                                    }

                                }
                            }


                        }
                    }
            }
        }
    }
}
