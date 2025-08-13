package cl.jacevedo.choppercontroller

import android.bluetooth.BluetoothGatt
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.room.Room
import cl.jacevedo.choppercontroller.connection.DroidBluetoothManager
import cl.jacevedo.choppercontroller.droidController.DroidController
import cl.jacevedo.choppercontroller.data.BluetoothDroidObject
import cl.jacevedo.choppercontroller.data.ButtonDroidEntity
import cl.jacevedo.choppercontroller.data.DeviceType
import cl.jacevedo.choppercontroller.database.DroidDatabase
import cl.jacevedo.choppercontroller.ui.theme.ChopperControllerTheme
import cl.jacevedo.choppercontroller.view.DroidActivityView
import cl.jacevedo.choppercontroller.viewmodel.DroidActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val MACRO_BUTTONS = 0
const val AUDIO_BUTTONS = 1

class DroidActivity : ComponentActivity() {
    private val droidActivityViewModel : DroidActivityViewModel by viewModels()
    private val droidController = DroidController()
    private val bluetoothDeviceManager = DroidBluetoothManager()
    private val droidObject by lazy {
        intent.getParcelableExtra(SELECTED_DROID, BluetoothDroidObject::class.java)
    }
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            DroidDatabase::class.java, "droidDatabase"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        CoroutineScope(Dispatchers.IO).launch {
            getDataFromDatabase(droidType = droidObject?.deviceType ?: DeviceType.CHOPPER)
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            ChopperControllerTheme {
                DroidActivityView(droidActivityViewModel, droidObject,{
                    droidController.appButtonPress(it)
                }, {
                    droidController.setVolume(it)
                })
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if(!hasFocus){
            droidController.onPause()
        }
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onPause() {
        super.onPause()
        droidController.onPause()
    }

    override fun onStop() {
        droidController.onPause()
        bluetoothDeviceManager.disconnect()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        bluetoothDeviceManager.connectBluetoothDroid(this, droidObject?.macAddress,{state ->
            val textToast = when(state){
                BluetoothGatt.STATE_CONNECTED -> {
                    "Droid Connected"
                }
                BluetoothGatt.STATE_DISCONNECTED -> {
                    this@DroidActivity.onStop()
                    "Droid Disconnected"
                }
                BluetoothGatt.STATE_CONNECTING -> {
                    "STATE_CONNECTING"
                }
                BluetoothGatt.STATE_DISCONNECTING ->{
                    "Disconnect"
                }
                else -> {
                    ""
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    Toast.makeText(baseContext, textToast, Toast.LENGTH_SHORT).show()
                }
            }
            if(state == BluetoothGatt.STATE_CONNECTED) {
                droidController.droidBluetoothManager = bluetoothDeviceManager
            } else {
                droidController.droidBluetoothManager = null
            }
        }, { message ->
            Log.e(TAG, "Message from device $message")
        })
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return droidController.motionEvent(event, this)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.keyCode == KeyEvent.KEYCODE_BACK){
            return super.onKeyUp(keyCode, event)
        }
        return droidController.buttonRelease(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.keyCode == KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event)
        }
        return droidController.buttonPress(keyCode, event, this)
    }

    private fun getDataFromDatabase(droidType: DeviceType){
        droidObject?.run{
            var listMacroButtons = database.buttonDroidDao().loadAllButtonsByDeviceIdAndType(associationId, MACRO_BUTTONS)
            if(listMacroButtons.isEmpty()){
                listMacroButtons = readButtonsFromXml(this, "macro.json", MACRO_BUTTONS)
                listMacroButtons.forEach {
                    database.buttonDroidDao().insertAll(it)
                }
            }
            var listAudioButtons = database.buttonDroidDao().loadAllButtonsByDeviceIdAndType(associationId, AUDIO_BUTTONS)
            if(listAudioButtons.isEmpty()){
                val audioFile = if(droidType == DeviceType.MOUSE){
                    "audio_mouse.json"
                } else {
                    "audio.json"
                }
                listAudioButtons = readButtonsFromXml(this, audioFile, AUDIO_BUTTONS)
                listAudioButtons.forEach {
                    database.buttonDroidDao().insertAll(it)
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                droidActivityViewModel.macroButtonsList.value = listMacroButtons.toMutableList()
                droidActivityViewModel.audioButtonsList.value = listAudioButtons.toMutableList()
            }
        }
    }

    private fun readButtonsFromXml(
        bluetoothDroidObject: BluetoothDroidObject,
        fileText: String,
        macroType: Int
    ):List<ButtonDroidEntity>{
        val str = assets.open(fileText).bufferedReader().use { it.readText() }
        val itemType = object : TypeToken<List<ButtonDroidEntity>>() {}.type
        val listItems = Gson().fromJson(str, itemType) ?: emptyList<ButtonDroidEntity>()
        listItems.map { it.buttonType = macroType }
        listItems.map { it.deviceId =  bluetoothDroidObject.associationId }
        return listItems
    }

}
