package com.kodego.app.roomdemo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Employee(
    var name:String,
    var salary:Int
    ){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}