package cl.jacevedo.droidcontroller.storage

import android.content.Context
import androidx.preference.PreferenceManager
import cl.jacevedo.droidcontroller.data.BluetoothDroidObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val DROID_JSON_KEY = "droid_json"
class BluetoothDroidStorage(private val context: Context) {

    fun getDroids():List<BluetoothDroidObject>{
        val stringJson = PreferenceManager.getDefaultSharedPreferences(context).getString(DROID_JSON_KEY, "")
        return if(stringJson != ""){
            val type = object : TypeToken<List<BluetoothDroidObject>>() {}.type
            Gson().fromJson(stringJson, type)
        } else {
            return listOf()
        }
    }

    fun storeDroids(listBluetooth : List<BluetoothDroidObject>){
        val jsonString = Gson().toJson(listBluetooth)
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(DROID_JSON_KEY, jsonString)
            .apply()
    }

}