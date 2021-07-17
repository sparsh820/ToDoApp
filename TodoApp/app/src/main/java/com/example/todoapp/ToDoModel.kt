package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
 data class ToDoModel(var Title:String,
                 var description:String,
                 var category:String,
                 var date:Long,
                 var time:Long,
                 var isfinished:Int=-1,
                 @PrimaryKey(autoGenerate = true)
                 var id:Long=0
                 )