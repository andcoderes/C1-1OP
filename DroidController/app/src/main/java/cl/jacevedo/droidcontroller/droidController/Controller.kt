package cl.jacevedo.droidcontroller.droidController

import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import cl.jacevedo.droidcontroller.communication.ButtonActions
import cl.jacevedo.droidcontroller.communication.ConnectionStatus

class Controller (private val shouldLog :Boolean = false) {

    private val listButtons = mutableMapOf<String, Boolean>()
    private var xPad = 0
    private var yPad = 0
    private val TAG = "Controller"

    fun keyUp(keyCode: Int): Boolean{
        setPressButton(keyCode, false)
        return true
    }

    fun keyDown(keyCode: Int, register: Boolean = true): Boolean{
        val keyCodeString = getButtonString(keyCode)
        if(keyCodeString != "" && listButtons[keyCodeString] != true){
            setPressButton(keyCode, register)
            val filteredValues : MutableMap<String, Boolean> = listButtons.filterValues { it }.toMutableMap()
            if(!register){
                filteredValues[keyCodeString] = true
            }
            val arrayButtons = if(filteredValues.isNotEmpty()){
                filteredValues.keys.toTypedArray()
            } else {
                arrayOf()
            }
            val buttonActions = ButtonActions(ConnectionStatus.BUTTONS.value, arrayButtons, arrayOf())
            //bluetoothController.sendMessage(buttonActions)
            return true
        }
        return false
    }

    private fun setPressButton(keyCode:Int, state:Boolean) : String{
        val buttonCode = getButtonString(keyCode)
        if(buttonCode != "") {
            listButtons[buttonCode] = state
        }
        return buttonCode
    }

    private fun getButtonString(keyCode: Int) =  when(keyCode) {
        KeyEvent.KEYCODE_BUTTON_Y -> {
            "y"
        }
        KeyEvent.KEYCODE_BUTTON_X -> {
            "x"
        }
        KeyEvent.KEYCODE_BUTTON_A -> {
            "a"
        }
        KeyEvent.KEYCODE_BUTTON_B -> {
            "b"
        }
        KeyEvent.KEYCODE_BUTTON_L1 -> {
            "l1"
        }
        KeyEvent.KEYCODE_BUTTON_L2 -> {
            "l2"
        }
        KeyEvent.KEYCODE_BUTTON_R1 -> {
            "r1"
        }
        KeyEvent.KEYCODE_BUTTON_R2 -> {
            "r2"
        }
        KeyEvent.KEYCODE_BUTTON_THUMBL -> {
            "al"
        }
        KeyEvent.KEYCODE_BUTTON_THUMBR -> {
            "ar"
        }
        KeyEvent.KEYCODE_BUTTON_START -> {
            "st"
        }
        KeyEvent.KEYCODE_BUTTON_SELECT -> {
            "se"
        }
        KeyEvent.KEYCODE_DPAD_UP -> {
            "du"
        }
        KeyEvent.KEYCODE_DPAD_LEFT -> {
            "dl"
        }
        KeyEvent.KEYCODE_DPAD_RIGHT -> {
            "dr"
        }
        KeyEvent.KEYCODE_DPAD_DOWN -> {
            "dd"
        }
        else -> {
            ""
        }
    }

    fun getGameControllerIds(): List<Int> {
        val gameControllerDeviceIds = mutableListOf<Int>()
        val deviceIds = InputDevice.getDeviceIds()
        deviceIds.forEach { deviceId ->
            InputDevice.getDevice(deviceId)?.apply {
                // Verify that the device has gamepad buttons, control sticks, or both.
                if (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
                    || sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) {
                    // This device is a game controller. Store its device ID.
                    gameControllerDeviceIds
                        .takeIf { !it.contains(deviceId) }
                        ?.add(deviceId)
                }
            }
        }
        return gameControllerDeviceIds
    }

    private fun logEvent(text:String){
        //if(shouldLog) {
            Log.d("Controller", text)
        //}
    }
}