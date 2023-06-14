package com.example.firebaseppb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private lateinit var userList: ArrayList<Mahasiswa>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        userRecyclerView = findViewById(R.id.recyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)


        userList = ArrayList()
        adapter = MahasiswaAdapter(userList)
        userRecyclerView.adapter = adapter


        database = FirebaseDatabase.getInstance().getReference("mahasiswa")


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                userList.clear()


                for (snapshot in dataSnapshot.children) {
                    val mahasiswa = snapshot.getValue(Mahasiswa::class.java)
                    mahasiswa?.let {
                        userList.add(it)
                    }
                }


                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}