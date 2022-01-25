package com.rahnama.whatsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rahnama.whatsapp.databinding.ActivityChatUserBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import android.widget.*
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


private lateinit var binding: ActivityChatUserBinding

class ChatUserActivity : AppCompatActivity() {

    /*********************************************************/
    var reciverUserId: String? = null
    var reciverUserName: String? = null
    var reciverprofileLink: String? = null
    var reciverState: String? = null
    var reciverDate: String? = null
    var reciverTime: String? = null

    var linearLayoutManager: LinearLayoutManager? = null
    var firebaseAdapter: FirebaseRecyclerAdapter<ChatMessageModel, messageViewHolder>? = null
    var database: DatabaseReference? = null
    var currentUser: FirebaseUser? = null

    var senderUser: String? = null
    var senderImageUrl: String? = null

    /*********************************************************/
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*********************************************************/
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowCustomEnabled(true)

        /*********************************************************/
        reciverUserId = intent.extras!!.getString("userId")
        reciverUserName = intent.extras!!.getString("Name")
        reciverprofileLink = intent.extras!!.getString("profileLink")
        reciverTime = intent.extras!!.getString("Time")
        reciverDate = intent.extras!!.getString("Date")
        /*********************************************************/
        currentUser = FirebaseAuth.getInstance().currentUser
        database =
            FirebaseDatabase.getInstance("https://whatsapp-a4e81-default-rtdb.firebaseio.com/").reference
        /*********************************************************/
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.stackFromEnd = true
       // linearLayoutManager!!.reverseLayout = true
        /*************************************************************/
        //get Current User Name
        database!!.child("Users").child(currentUser!!.uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                senderUser = snapshot.child("Name").value.toString()
                senderImageUrl = snapshot.child("thumb_image").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        /*********************************************************/
        val inflater: LayoutInflater =
            this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionbarView = inflater.inflate(R.layout.custom_bar, null)
        actionbarView.findViewById<TextView>(R.id.customProfileUsername).text = reciverUserName!!

        if (reciverState == "online") {
            actionbarView.findViewById<ImageView>(R.id.lastseen).visibility = View.VISIBLE
            actionbarView.findViewById<TextView>(R.id.customState).text = reciverState!!
        } else if (reciverState == "offline") {
            actionbarView.findViewById<ImageView>(R.id.lastseen).visibility = View.GONE
            actionbarView.findViewById<TextView>(R.id.customState).text = "last seen $reciverDate $reciverTime"

        }

        Picasso.with(this).load(reciverprofileLink).placeholder(R.drawable.profile_img)
            .into(actionbarView.findViewById<CircleImageView>(R.id.custombarProfileImageView))

        actionbarView.findViewById<CircleImageView>(R.id.custombarProfileImageView).setOnClickListener {
            val profileIntent = Intent(this,ProfileActivity::class.java)
            profileIntent.putExtra("profileLink", reciverprofileLink)
            startActivity(profileIntent)


        }

        actionbarView.findViewById<ImageButton>(R.id.btn_video_call).setOnClickListener {
            val intent_Video_call=Intent(this,VideoCallOut::class.java)
            intent_Video_call.putExtra("reciverUserId",reciverUserId)
            intent_Video_call.putExtra("reciver_name",reciverUserName)
            intent_Video_call.putExtra("profileLink",reciverprofileLink)
            intent_Video_call.putExtra("SenderUid",currentUser!!.uid)


            startActivity(intent_Video_call)

        }
        supportActionBar!!.setCustomView(actionbarView)

        /*********************************************************/
        firebaseAdapter = object : FirebaseRecyclerAdapter<ChatMessageModel, messageViewHolder>(
            ChatMessageModel::class.java,
            R.layout.chat_row_left,
            messageViewHolder::class.java,
            database!!.child("messages")
        ) {
            @RequiresApi(Build.VERSION_CODES.M)
            @SuppressLint("RtlHardcoded", "ResourceAsColor")
            override fun populateViewHolder(
                viewholder: messageViewHolder?,
                frind: ChatMessageModel?,
                position: Int) {
                val chatId: String? = getRef(position).key
                if ((frind!!.message != null) && ((frind.sender == senderUser && frind.reciver == reciverUserName) || (frind.sender == reciverUserName && frind.reciver == senderUser))) {
                    //binding.recyMessage.clearOnChildAttachStateChangeListeners()
                    viewholder!!.bindviewHolder(frind)
                    viewholder.itemView.visibility = View.VISIBLE

                    val isMe = frind.id!! == currentUser!!.uid
                    if (isMe) {

                        viewholder.Container!!.gravity=Gravity.RIGHT
                        viewholder.messagetext!!.gravity =
                            (Gravity.CENTER_VERTICAL or Gravity.RIGHT)
                        viewholder.messagetext!!.setTextColor(Color.parseColor("#FFFFFF"))
                        viewholder.time!!.setTextColor(Color.parseColor("#eeeeee"))
                        viewholder.msgContainer!!.gravity=Gravity.RIGHT
                        viewholder.msgContainer!!.setBackgroundResource(R.drawable.rounded_corner1)
                        viewholder.profileimageRight!!.visibility = View.VISIBLE
                        viewholder.profileimageLeft!!.visibility = View.GONE

                        Picasso.with(viewholder.profileimageRight!!.context)
                            .load(senderImageUrl).placeholder(R.drawable.profile_img)
                            .into(viewholder.profileimageRight)


                    } else {
                        viewholder.Container!!.gravity=Gravity.LEFT
                        viewholder.messagetext!!.setTextColor(Color.parseColor("#000000"))
                        viewholder.time!!.setTextColor(Color.parseColor("#656363"))
                        viewholder.msgContainer!!.gravity=Gravity.LEFT
                        viewholder.messagetext!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)
                        viewholder.profileimageRight!!.visibility = View.GONE
                        viewholder.profileimageLeft!!.visibility = View.VISIBLE
                        viewholder.msgContainer!!.setBackgroundResource(R.drawable.rounded_corner2)

                        Picasso.with(viewholder.profileimageRight!!.context)
                            .load(reciverprofileLink).placeholder(R.drawable.profile_img)
                            .into(viewholder.profileimageLeft)

                    }


/*************************************************************************************/
                        if (chatId != null) {

                               //init popup menu
                            val popupMenu = PopupMenu(
                                applicationContext,
                                viewholder.msgContainer!!
                            )

                            //inflate layout for popup menu
                            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

                            //handle popup menu item clicks
                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                //get id of clicked mennu item
                                val id = menuItem.itemId
                                //handle menu item clicks
                                if (id == R.id.menu_edit){
                                    //edit clicked

                                }

                                else if (id == R.id.menu_delete){
                                    //delete clicked
                                    database!!.child("Users").child("messages").child(chatId).addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            snapshot.ref.removeValue()
                                            Toast.makeText(applicationContext,"deleted",Toast.LENGTH_LONG).show()
                                        }

                                        override fun onCancelled(p0: DatabaseError) {}
                                    })

                                }


                                false
                            }

                            //handle button click: show popup menu
                            viewholder.msgContainer!!.setOnClickListener {
                                popupMenu.show()
                            }



                        }


                }
            }
        }

        /*********************************************************/
        val formatDate: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = formatDate.format(Date())

        val formatter = SimpleDateFormat("hh:mm a")
        val time = formatter.format(Date())

        binding.recyMessage.layoutManager = linearLayoutManager
        binding.recyMessage.adapter = firebaseAdapter
        binding.sendButton.setOnClickListener {
            if (reciverUserName != "") {

                val meID = currentUser!!.uid
                val chatMessageModel = ChatMessageModel(
                    meID,
                    reciverUserName!!,
                    senderUser!!,
                    binding.messageEdt.text.toString().trim(),
                    date,
                    time

                    )
                database!!.child("messages").push().setValue(chatMessageModel)
                binding.messageEdt.setText("")
            }
        }
        /**************************************************************/

    }


    /**************************************************************/
    class messageViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var messagetext: TextView? = null
        var profileimageLeft: CircleImageView? = null
        var profileimageRight: CircleImageView? = null
        var msgContainer: LinearLayout? = null
        var Container: LinearLayout? = null
        var time:TextView?=null

        @SuppressLint("SetTextI18n")
        fun bindviewHolder(chatMessageModel: ChatMessageModel) {
            messagetext = itemView.findViewById(R.id.messageChat)
            profileimageLeft = itemView.findViewById(R.id.messageprofileReciver)
            profileimageRight = itemView.findViewById(R.id.messageprofileSender)
            msgContainer = itemView.findViewById(R.id.msgContainer)
            Container = itemView.findViewById(R.id.root_chat)
            time=itemView.findViewById(R.id.timeSendChat)

            messagetext!!.text = chatMessageModel.message
            time!!.text = chatMessageModel.time
        }


    }
    /*********************************************************/

}