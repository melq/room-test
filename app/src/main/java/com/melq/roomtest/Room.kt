package com.melq.roomtest

import androidx.room.*

class Room {
}

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val firstName: String?,
    val lastName: String?,
    val age: Int
)

@Dao
interface UserDao {
    @Insert
    fun insert(user : User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user : User)

    @Query("delete from users")
    fun deleteAll()

    @Query("select * from users")
    fun getAll()

    @Query("select * from users where id = :id")
    fun getUser(id: Int): User
}