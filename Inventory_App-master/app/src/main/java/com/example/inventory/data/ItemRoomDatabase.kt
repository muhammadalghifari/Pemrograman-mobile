package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//menentukan item menjadi  satu satunya class list of entities
//nomor versi akan meningkat setiap merubah skema tabel database
//exportSchema agar tidak menyimpan cadangan histori skema version
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    //membuat database mengetahui DAO
    abstract fun itemDao(): ItemDao
    //memungkinkan akases ke metode untuk mengubah database dengan menggunakan class name
    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null
        fun getDatabase(context: Context): ItemRoomDatabase {
            //memastikan hanya 1 thread dalam 1 waktu
            //(database hanya diinisialisasi sekali)
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    //untuk membuat instance database
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}