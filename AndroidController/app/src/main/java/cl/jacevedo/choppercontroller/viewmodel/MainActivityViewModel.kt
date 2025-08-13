package cl.jacevedo.choppercontroller.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.jacevedo.choppercontroller.data.BluetoothDroidObject
import cl.jacevedo.choppercontroller.storage.BluetoothDroidStorage

class MainActivityViewModel: ViewModel() {
    val bluetoothDroidList : MutableLiveData<MutableList<BluetoothDroidObject>> by lazy {
        MutableLiveData<MutableList<BluetoothDroidObject>>()
    }

    fun addItem(item: BluetoothDroidObject, context: Context) {
        addItem(item)
        bluetoothDroidList.value?.toList()?.let {
            BluetoothDroidStorage(context).storeDroids(it)
        }
    }

    private fun addItem(item:BluetoothDroidObject){
        val currentList = bluetoothDroidList.value?.toMutableList() ?: mutableListOf()
        currentList.add(item)
        bluetoothDroidList.value = currentList.toMutableList() // Create a new list instance
    }

    fun setList(items: List<BluetoothDroidObject>){
        bluetoothDroidList.value = mutableListOf()

        items.forEach {
            addItem(it)
        }
    }
}