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
        CoroutineScope(Dispatchers.Main + job).launch {
            for (i in 0 until 5) {
                userDao.insert(User(i + 1, "fn$i", "ln$i", i))
                Log.v("INSERT", "insert: $i")
            }
            Log.v("GETALL_ISEMPTY", "getAll: ${userDao.getAll()}")
        }
        val usersStr = userDao.getAll().toString()
        Log.v("GETALL", "getAll: $usersStr")
        val userList: ArrayList<User> = ArrayList()
        for (user in userDao.getAll()) {
            userList.add(user)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter = CustomAdapter(userList)
        recyclerView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            val index = userDao.getAll().last().id
            CoroutineScope(Dispatchers.Main + job).launch {
                val user = User(index + 1, "fn$index", "ln$index", index)
                userDao.insert(user)
                userList.add(user)
                Log.v("ADDUSER", userList[userList.size - 1].toString())
                adapter.notifyDataSetChanged()
            }
        }

//        userDao.deleteAll()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}