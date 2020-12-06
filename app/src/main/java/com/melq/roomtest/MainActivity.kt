package com.melq.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val job = Job()
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        val userDao = database.userDao()
        val userList: ArrayList<User> = ArrayList()
        CoroutineScope(Dispatchers.Main + job).launch {
            if (userDao.getAll().isEmpty()) {
                for (i in 0 until 5) {
                    userDao.insert(User(i + 1, "fn$i", "ln$i", i))
                    Log.v("INSERT", "insert: $i")
                }
                Log.v("GETALL_ISEMPTY", "getAll: ${userDao.getAll()}")
            }
            for (user in userDao.getAll()) {
                userList.add(user)
            }
        }
        Log.v("GETALL", "getAll: ${userDao.getAll()}")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(userList)
        recyclerView.adapter = adapter

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            CoroutineScope(Dispatchers.Main + job).launch {
                val tmp = userDao.getAll()
                val index: Int =
                    if (tmp.isEmpty()) 0
                    else tmp.last().id
                val user = User(index + 1, "fn$index", "ln$index", index)
                userDao.insert(user)
                userList.add(user)
                Log.v("ADDUSER", userList[userList.size - 1].toString())
                adapter.notifyDataSetChanged()
            }
        }
        val fabDeleteAll = findViewById<FloatingActionButton>(R.id.fab_delete_all)
        fabDeleteAll.setOnClickListener {
            userDao.deleteAll()
            userList.clear()
            Log.v("DELETEALL", "userDao.getAll: " + userDao.getAll() + ", userList: " + userList)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}