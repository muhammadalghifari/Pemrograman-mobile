package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

//memberikan nama tabel menjadi item
@Entity(tableName = "item")
//mendeklarasikan variabel class item
//kemudian dikonversikan ke class data
data class Item(
    //membuat val id menjadi primaryKey dan memastikan ID setiap item unik
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //columnInfo memastikan nama kolom dengan bidamg tertentu
    @ColumnInfo(name = "name")
    val itemName: String,
    @ColumnInfo(name = "price")
    val itemPrice: Double,
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int)

//mengembalikan harga yang diteruskan dalam format mata uang
fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)