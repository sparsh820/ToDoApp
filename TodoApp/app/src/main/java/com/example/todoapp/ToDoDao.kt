package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ToDoDao {


    @Insert()
    suspend fun insertTask(todoModel: ToDoModel):Long

    @Query("Select * from TodoModel where isFinished == -1")
    fun getTask():LiveData<List<ToDoModel>>

    @Query("Update TodoModel Set isFinished = 1 where id=:uid")
    fun finishTask(uid:Long)

    @Query("Delete from TodoModel")
   suspend fun deleteTask()

    @Query("Delete from TodoModel where id=:uid")
    fun deleteGivenTask(uid:Long)



}