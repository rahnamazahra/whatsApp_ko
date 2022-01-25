package com.rahnama.whatsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rahnama.whatsapp.databinding.FragmentUsersBinding



class Usersfragment: Fragment() {

    private var _binding: FragmentUsersBinding? = null
    var database: DatabaseReference? = null
    private val binding get() = _binding!!

    /***************************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    /**************************************************/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database =
            FirebaseDatabase.getInstance("https://whatsapp-a4e81-default-rtdb.firebaseio.com/").reference.child("Users")

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.usersListRecy.setHasFixedSize(true)
        binding.usersListRecy.layoutManager = linearLayoutManager
        binding.usersListRecy.adapter = UsersAdapter(database!!, requireContext())
    }
    /**************************************************/
}
