package com.example.firebaseppb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseppb.Fitur.Edit
import com.example.firebaseppb.Fitur.Insert
import com.example.firebaseppb.MahasiswaAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private lateinit var userList: ArrayList<Mahasiswa>
    private lateinit var database: DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val insertButton: Button = findViewById(R.id.insertButton)
        insertButton.setOnClickListener {
            insertMahasiswa()
        }

        userRecyclerView = findViewById(R.id.recyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()
        adapter = MahasiswaAdapter(userList, this, ::editMahasiswa, ::deleteMahasiswa)
        userRecyclerView.adapter = adapter
        // Get Firebase database reference
        database =
            FirebaseDatabase.getInstance().getReference("mahasiswa")
        // Get data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear previous data from userList
                userList.clear()
                // Loop through each data item and add it to
                userList
                for (snapshot in dataSnapshot.children) {
                    val nim =
                        snapshot.child("nim").getValue(String::class.java)
                    val mahasiswa =
                        snapshot.getValue(Mahasiswa::class.java)
                    mahasiswa?.let {
                        it.nim = nim
                        it.nim?.let { nim -> // Check if nim is not

                            it.nim = nim
                            userList.add(it)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error when accessing Firebase
            }
        })
    }
    private fun editMahasiswa(mahasiswa: Mahasiswa) {
        val intent = Intent(this, Edit::class.java)
        intent.putExtra("nama", mahasiswa.nama)
        intent.putExtra("nim", mahasiswa.nim)
        intent.putExtra("telp", mahasiswa.telp)
        startActivity(intent)
    }
    private fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        // Delete the mahasiswa data from Firebase
        val userId = mahasiswa.nim
        userId?.let {
            database.child(it).removeValue()
        }
    }
    private fun insertMahasiswa() {
        val intent = Intent(this, Insert::class.java)
        startActivity(intent)
    }
}