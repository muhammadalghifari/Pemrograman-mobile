package com.example.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

//ViewModel memastikan referensi ke reposirori Inventori dan update list terbaru
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    // chahce semua item dari database menggunakan LiveData
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    //mengembalikan nilai true jika stok memungkinkan dijual
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }
    //mengupdate item yang ada di database
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(
            itemId,
            itemName,
            itemPrice,
            itemCount)
        updateItem(updatedItem)
    }

    //menjalankan coroutine baru untuk memperbarui item dengan cara yang tidak diblokir
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    //mengurangi stok dan mengupdate database
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }
    //memasukkan item baru ke database
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    //menjalankan coroutine baru untuk memasukkan item
    //ke database dengan cara yang tidak diblokir
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    //menjalankan coroutine baru untuk menghapus item
    //ke database dengan cara yang tidak diblokir
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    //mengambil item dari repository
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    //mengembalikan nilai true jika EditText tidak kosong
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    //mengembalikan instance 'Item' dengan info item dimasukkan oleh user
    //ini akan digunakan untuk menambahkan entry baru ke dalam Inventory database
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    //memperbarui entri yang ada di database Inventaris
    //mengembalikan instance kelas entitas 'Item' dengan info item yang diperbarui oleh pengguna.
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
}

//class Factory untuk menggambarkan 'ViewModel' instance
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

