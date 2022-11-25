package com.kodego.app.roomdemo.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmployeeDao {
    @Insert
    fun addEmployee(employee: Employee)

    @Query("SELECT * FROM Employee")
    fun getAllEmployees():MutableList<Employee>

    @Query("UPDATE Employee SET name = :name WHERE id = :id")
    fun updateEmployee(name:String,id:Int)

    @Query("DELETE FROM Employee WHERE id = :id")
    fun deleteEmployee(id:Int)
}