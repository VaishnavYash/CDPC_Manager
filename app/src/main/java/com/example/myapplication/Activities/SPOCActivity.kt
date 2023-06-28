package com.example.myapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.Adapters.SPOCAdapter
import com.example.myapplication.LoginSQL
import com.example.myapplication.SuperUsersObjects

class SPOCActivity : AppCompatActivity() {

    lateinit var spocAdapter: SPOCAdapter
    var recyclerView: RecyclerView? =null
    val superDataset = arrayListOf<SuperUsersObjects>()
    lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spocactivity)

        supportActionBar?.title = "SPOC Portal"

        recyclerView = findViewById(R.id.spoc_recycler)
        val layoutManager = LinearLayoutManager(this)

        recyclerView?.layoutManager = layoutManager
        spocAdapter = SPOCAdapter(this, superDataset)
        recyclerView?.adapter = spocAdapter

        //TABLE
        val intent = intent
        email = intent.getStringExtra("key").toString().trim()
        spocAdapter.filterListByEmail(email)


    }

}