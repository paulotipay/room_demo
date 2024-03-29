package com.kodego.app.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Employee::class],
    version = 1
)

abstract class CompanyDatabase: RoomDatabase() {
    abstract fun getEmployees():EmployeeDao


    companion object {
        @Volatile
        private var instance: CompanyDatabase? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CompanyDatabase::class.java,
            "company"
        ).build()
    }
}