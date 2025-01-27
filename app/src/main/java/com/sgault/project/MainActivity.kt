package com.sgault.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sgault.project.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
  private lateinit var binding: ActivityMainBinding
  private final val ioScope by lazy { CoroutineScope(Dispatchers.IO) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        readFile(this)
        listViewSetup()

     
    }
    fun generateRandomString(view: View){
        ioScope.launch {

            var randomStrings = arrayOf("wiki",".com",".ca",".edu",".io","s","a","b")
            val randomNum = (0..7).random()
            Log.d("num", randomNum.toString())
            binding.searchBar.setText(randomStrings[randomNum])

        }

    }
    fun searchBar(view:View){
            viewModel.domains = mutableListOf<String>()
            if(binding.searchBar.text.equals("")){
                for (website in viewModel.websites){
                    viewModel.addDomain(website.domain)
                }
            }
            else
            {
                for (website in viewModel.websites){
                    if(website.domain.contains(binding.searchBar.text.toString())){
                        viewModel.addDomain(website.domain)
                    }
                }
            }
            listViewSetup()


    }

    fun listViewSetup(){
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, viewModel.domains)
        binding.ListView.adapter = arrayAdapter
        binding.ListView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            viewModel.selectWebsite(selectedItem)
            Log.d("selected",viewModel.selectedWebsite.value!!.domain)
            Log.d("selected pages",viewModel.selectedWebsite.value!!.pages)
            val intent = Intent(this, ViewWebsite::class.java)
            intent.putExtra("domain", viewModel.selectedWebsite.value!!.domain)
            intent.putExtra("pages", viewModel.selectedWebsite.value!!.pages)
            intent.putExtra("urls", viewModel.selectedWebsite.value!!.numUrls)
            intent.putExtra("hosts", viewModel.selectedWebsite.value!!.hosts)
            intent.putExtra("percentPages", viewModel.selectedWebsite.value!!.percentPages)
            intent.putExtra("percentUrls", viewModel.selectedWebsite.value!!.percentUrls)
            intent.putExtra("rank", viewModel.getRank())
            startActivity(intent)
        }
    }
    fun readFile(context: Context):String?{

        val csvString: String
        try {
            csvString = context.assets.open("domains-top-500.csv").bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        for (line in csvString.split("\n")){
            try{
                var website = line.split(",")
                viewModel.addWebsite(website[0], website[1], website[2], website[3], website[4], website[5])
                viewModel.addDomain(website[0])
            }
            catch (e:Exception){
                Log.i("error", e.toString())
                break
            }

        }

        viewModel.websites.removeAt(0)//removing header line from each
        viewModel.domains.removeAt(0)
        return csvString
    }

    fun showDetails(view: View){
        ioScope.launch{
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            startActivity(intent)
        }

    }


}
