package com.sgault.project

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.sgault.project.databinding.ActivityViewWebsiteBinding
import androidx.databinding.DataBindingUtil
class ViewWebsite : AppCompatActivity() {
    private lateinit var binding: ActivityViewWebsiteBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewWebsiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.domain.setText(intent.getStringExtra("domain"))
        binding.numPages.setText(intent.getStringExtra("pages"))
        binding.numUrls.setText(intent.getStringExtra("urls"))
        binding.numHosts.setText(intent.getStringExtra("hosts"))
        binding.percentPages.setText("%"+intent.getStringExtra("percentPages"))
        binding.percentUrls.setText("%"+intent.getStringExtra("percentUrls"))
        binding.rank.setText(intent.getIntExtra("rank", 0).toString() +"/500" )


    }

    fun endActivity(view: View) {
        finish()
    }
}