package com.rahnama.whatsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rahnama.whatsapp.databinding.FragmentChatsBinding



class Chatsfragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    var database: DatabaseReference? = null
    private var auth: FirebaseAuth?=null
    private var currentUser: FirebaseUser?=null

    private val binding get() = _binding!!

    /***************************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    /**************************************************/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database =
            FirebaseDatabase.getInstance("https://whatsapp-a4e81-default-rtdb.firebaseio.com/").reference.child(
                "Users"
            )


        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.ChatsListRecy.setHasFixedSize(true)
        binding.ChatsListRecy.layoutManager = linearLayoutManager
        binding.ChatsListRecy.adapter = UsersAdapter(database!!, requireContext())
    }
    /**************************************************/
}