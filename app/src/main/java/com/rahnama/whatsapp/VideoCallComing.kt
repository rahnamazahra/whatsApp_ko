package com.rahnama.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rahnama.whatsapp.databinding.ActivityVideoCallComingBinding
import com.rahnama.whatsapp.databinding.ActivityVideoCallOutBinding

private lateinit var binding:ActivityVideoCallComingBinding
class VideoCallComing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVideoCallComingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*********************************************************/

    }
}