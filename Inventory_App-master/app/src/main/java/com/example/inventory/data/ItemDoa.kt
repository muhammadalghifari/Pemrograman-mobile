package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Dao untuk mengakses Inventory database
@Dao
interface ItemDao {

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * from item WHERE id = :id")
    //memastikan mendapatkan notifikasi setiap data dalam database berubah
    fun getItem(id: Int): Flow<Item>

    //akan memberitahu room untuk mengabaikan item baru
    //dengan primaryKey yang sudah ada dalam database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    //entity yang diperbaharui akan memiliki
    //key yang sama sebelum di update
    @Update
    suspend fun update(item: Item)

    //fungsi untuk menghapus item atau list item
    @Delete
    suspend fun delete(item: Item)

}