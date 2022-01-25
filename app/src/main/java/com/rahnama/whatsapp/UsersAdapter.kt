package com.rahnama.whatsapp

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.rahnama.whatsapp.databinding.ActivityChatUserBinding
import com.rahnama.whatsapp.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

private lateinit var binding: ActivityProfileBinding
class UsersAdapter(databaseQuery: DatabaseReference, var context: Context) :
    FirebaseRecyclerAdapter<UserModel, UsersAdapter.ViewHolder>(
        UserModel::class.java,
        R.layout.users_contact_row,
        ViewHolder::class.java,
        databaseQuery
    ) {
    /************************************************************************************************/
    override fun populateViewHolder(viewholder: ViewHolder?, user: UserModel?, position: Int) {
        val userId: String? = getRef(position).key
        viewholder!!.bindView(user!!, context, userId)

        viewholder.itemView.setOnClickListener {

            val ChatUserintent = Intent(context, ChatUserActivity::class.java)
            ChatUserintent.putExtra("userId", userId)
            ChatUserintent.putExtra("profileLink", viewholder.userProfileImageText)
            ChatUserintent.putExtra("Name", viewholder.userNameText)
            ChatUserintent.putExtra("State", viewholder.state)
            ChatUserintent.putExtra("Time", viewholder.time)
            ChatUserintent.putExtra("Date", viewholder.date)

            context.startActivity(ChatUserintent)
        }

    }

    /************************************************************************************************/
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var userNameText: String? = null
        var userAboutText: String? = null
        var userProfileImageText: String? = null
        var state: String? = null
        var time: String? = null
        var date: String? = null

        /*******************************/
        fun bindView(user: UserModel, context: Context, userId: String?) {

            val userName = itemView.findViewById<TextView>(R.id.user_row_username)
            val userAbout = itemView.findViewById<TextView>(R.id.user_row_about)
            val userProfileImage = itemView.findViewById<CircleImageView>(R.id.profileUser)


            userNameText = user.Name
            userAboutText = user.About
            userProfileImageText = user.thumb_image
            state = user.State
            time = user.Time
            date = user.Date

            userName.text = userNameText
            userAbout.text = userAboutText
            Picasso.with(context).load(userProfileImageText).placeholder(R.drawable.profile_img)
                .into(userProfileImage)

            userProfileImage.setOnClickListener {
                val profileIntent = Intent(context,ProfileActivity::class.java)
                profileIntent.putExtra("profileLink", userProfileImageText)

                context.startActivity(profileIntent)


            }

        }
    }

}