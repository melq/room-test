package com.melq.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
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
        var userList = userDao.getAll()
        CoroutineScope(Dispatchers.Main + job).launch {
            if (userList.isEmpty()) {
                for (i in 0 until 20) {
                    userDao.insert(User(i + 1, "fn$i", "ln$i", i))
                    Log.v("INSERT", "insert: $i")
                }
                Log.v("GETALL_ISEMPTY", "getAll: ${userDao.getAll()}")
            } else {
                for (i in 20 until 30) {
                    userDao.insert(User(i + 1, "fn$i", "ln$i", i))
                    Log.v("INSERT", "insert: $i")
                }
            }
        }
        val usersStr = userDao.getAll().toString()
        Log.v("GETALL", "getAll: $usersStr")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(userList)
        recyclerView.adapter = adapter

//        userDao.deleteAll()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}