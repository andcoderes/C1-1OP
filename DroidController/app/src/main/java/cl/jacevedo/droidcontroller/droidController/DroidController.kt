package cl.jacevedo.droidcontroller.droidController

import android.app.Activity
import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import cl.jacevedo.droidcontroller.AUDIO_BUTTONS
import cl.jacevedo.droidcontroller.communication.ButtonActions
import cl.jacevedo.droidcontroller.communication.ConnectionStatus
import cl.jacevedo.droidcontroller.communication.MovementObject
import cl.jacevedo.droidcontroller.communication.Settings
import cl.jacevedo.droidcontroller.connection.DroidBluetoothManager
import cl.jacevedo.droidcontroller.data.ButtonDroidEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.abs

val CONTROLLER_TO_DROID_ACTIONS = mapOf(
    KeyEvent.KEYCODE_BUTTON_Y to "y",
    KeyEvent.KEYCODE_BUTTON_X to "x",
    KeyEvent.KEYCODE_BUTTON_A to "a",
    KeyEvent.KEYCODE_BUTTON_B to "b",
    KeyEvent.KEYCODE_BUTTON_L1 to "l1",
    KeyEvent.KEYCODE_BUTTON_L2 to "l2",
    KeyEvent.KEYCODE_BUTTON_R1 to "r1",
    KeyEvent.KEYCODE_BUTTON_R2 to "r2",
    KeyEvent.KEYCODE_BUTTON_THUMBL to "al",
    KeyEvent.KEYCODE_BUTTON_THUMBR to "ar",
    KeyEvent.KEYCODE_BUTTON_START to "st",
    KeyEvent.KEYCODE_BUTTON_SELECT to "se",
    KeyEvent.KEYCODE_DPAD_UP to "du",
    KeyEvent.KEYCODE_DPAD_LEFT to "dl",
    KeyEvent.KEYCODE_DPAD_RIGHT to "dr",
    KeyEvent.KEYCODE_DPAD_DOWN to "dd",
    )

class DroidController {
    private val listButtons = mutableMapOf<String, Boolean>()
    private val TAG = "DroidController"
    private val dpadMotion = DpadMotion()
    private var previousButton: Int = KeyEvent.KEYCODE_DPAD_CENTER
    private var movementObject = MovementObject(0,0,0,0,0,0)
    private var isActive = false
    var droidBluetoothManager : DroidBluetoothManager ? = null
    private var sendingJob: Job? = null

    fun onPause(){
        Log.e(TAG, "calling on pause")
        movementObject = MovementObject(0,0,0,0,0,0)
        droidBluetoothManager?.sendDroidObjectMessage(movementObject.movementObject())
    }

    private fun startSendingWhileActive() {
        if(isActive){
            return
        }
        isActive = true
        sendingJob?.cancel()
        sendingJob = CoroutineScope(Dispatchers.IO).launch{
            while (isActive) {
                val jsonMessage = Gson().toJson(movementObject.movementObject())
                Log.d(TAG, jsonMessage)
                droidBluetoothManager?.sendDroidObjectMessage(movementObject.movementObject())
                if (movementObject.isAllZero()) {
                    isActive = false
                    break
                }
                delay(100) // adjust frequency as needed
            }
        }
    }

    fun buttonPress(keyCode: Int, event: KeyEvent?, activity: Activity?) : Boolean{
        if (event != null && event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD ) {
            return pressButtonAction(keyCode)
        }
        if(activity == null || activity.parent == null){
            return true
        }
        return activity.parent.onKeyDown(keyCode, event)
    }
    fun buttonRelease(keyCode: Int, event: KeyEvent?) : Boolean{
        setPressButton(keyCode, false)
        return true
    }
    fun motionEvent(event: MotionEvent?, activity: Activity?) : Boolean{
        val press: Int = dpadMotion.getDirectionPressed(event)
        if(press != DpadMotion.CENTER){
            pressButtonAction(press)
            return false
        }
        if(previousButton == DpadMotion.CENTER){
            genericMotionEvent(event)
            return true
        }
        previousButton = press
        if(activity == null || activity.parent == null){
            return true
        }
        return activity.parent.onGenericMotionEvent(event)
    }
    fun appButtonPress(buttonDroidEntity: ButtonDroidEntity) {
        val connectionStatus = if(buttonDroidEntity.buttonType == AUDIO_BUTTONS){
            ConnectionStatus.AUDIO.value
        } else {
            ConnectionStatus.BUTTONS.value
        }
        buttonDroidEntity.macro?.let {
            if(connectionStatus == ConnectionStatus.AUDIO.value){
                droidBluetoothManager?.sendDroidObjectMessage(ButtonActions(connectionStatus, arrayOf(), arrayOf(it.toInt())))
            } else {
                droidBluetoothManager?.sendDroidObjectMessage(ButtonActions(connectionStatus, arrayOf(it), arrayOf()))
            }
        }
    }

    private fun setPressButton(keyCode:Int, state:Boolean) : String {
        val buttonCode = getButtonString(keyCode)
        if(buttonCode != "") {
            listButtons[buttonCode] = state
        }
        return buttonCode
    }

    private fun pressButtonAction(keyCode:Int) : Boolean {
        val buttonString = getButtonString(keyCode)
        if(buttonString != "" && listButtons[buttonString] != true) {
            setPressButton(keyCode, true)
            val filteredValues : MutableMap<String, Boolean> = listButtons.filterValues { it }.toMutableMap()
            val arrayButtons = if(filteredValues.isNotEmpty()){
                filteredValues.keys.toTypedArray()
            } else {
                arrayOf()
            }
            val buttonActions = ButtonActions(ConnectionStatus.BUTTONS.value, arrayButtons, arrayOf())
            droidBluetoothManager?.sendDroidObjectMessage(buttonActions)
            return true
        }
        return false
    }
    private fun genericMotionEvent(event: MotionEvent?) {
        event?.let{
            if (event.source and InputDevice.SOURCE_GAMEPAD != InputDevice.SOURCE_GAMEPAD) {
                val lxAxis: Int = deltaValueOfAnalog((String.format(Locale.US,"%.2f", event.getAxisValue(MotionEvent.AXIS_X)).toFloat() * 100).toInt())
                val lyAxis: Int = deltaValueOfAnalog((String.format(Locale.US,"%.2f", event.getAxisValue(MotionEvent.AXIS_Y)).toFloat() * 100).toInt())
                val rxAxis: Int = deltaValueOfAnalog((String.format(Locale.US,"%.2f", event.getAxisValue(MotionEvent.AXIS_Z)).toFloat() * 100).toInt())
                val ryAxis: Int = deltaValueOfAnalog((String.format(Locale.US,"%.2f", event.getAxisValue(MotionEvent.AXIS_RZ)).toFloat() * 100).toInt())
                val buttonR2 = (String.format(Locale.US,"%.2f", event.getAxisValue(MotionEvent.AXIS_RTRIGGER)).toFloat() * 100).toInt()
                val buttonL2 = (String.format(Locale.US,"%.2f", event.getAxisValue(MotionEvent.AXIS_LTRIGGER)).toFloat() * 100).toInt()

                movementObject = MovementObject(lxAxis, lyAxis, rxAxis, ryAxis, buttonR2, buttonL2)
                startSendingWhileActive()
            }
        }
    }

    private fun deltaValueOfAnalog(value:Int) : Int {
        if(abs(value) <= 3f){
            return 0
        }
        return value
    }

    private fun getButtonString(keyCode: Int) =
     if(CONTROLLER_TO_DROID_ACTIONS.containsKey(keyCode)){ CONTROLLER_TO_DROID_ACTIONS[keyCode]} else {""} ?: ""

    fun setVolume(it: Float) {
        droidBluetoothManager?.sendDroidObjectMessage(Settings(ConnectionStatus.SETTINGS.value, it.toInt()))
    }

}