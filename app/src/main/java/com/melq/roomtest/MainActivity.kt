package com.melq.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
        val textView = findViewById<TextView>(R.id.textview)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        val userDao = database.userDao()
        CoroutineScope(Dispatchers.Main + job).launch {
            val userList = userDao.getAll()
            if (userList.isEmpty()) {
                for (i in 0 until 5) {
                    userDao.insert(User(i, "fn$i", "ln$i", i))
                    Log.v("INSERT", "insert: $i")
                }
                Log.v("GETALL_ISEMPTY", "getAll: ${userDao.getAll()}")
            } else {
                for (i in 5 until 10) {
                    userDao.insert(User(i, "fn$i", "ln$i", i))
                    Log.v("INSERT", "insert: $i")
                }
            }
        }
        val usersStr = userDao.getAll().toString()
        Log.v("GETALL", "getAll: $usersStr")
        textView.text = usersStr
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}