package com.melq.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val userList = userDao.getAll()
        if (userList.isEmpty()) {
//            CoroutineScope(Dispatchers.Main + job).launch {
            for (i in 0 until 5) {
                userDao.insert(User(i, "fn$i", "ln$i", i))
                Log.v("INSERT", "insert: $i")
//                }
                Log.v("GETALL_ISEMPTY", "getAll: ${userDao.getAll().toString()}")
            }
        } else {
            for (i in 5 until 10) {
                userDao.insert(User(i, "fn$i", "ln$i", i))
                Log.v("INSERT", "insert: $i")
            }
        }
        Log.v("GETALL", "getAll: ${userDao.getAll()}")
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}