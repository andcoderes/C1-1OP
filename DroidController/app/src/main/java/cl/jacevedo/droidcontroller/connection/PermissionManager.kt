package cl.jacevedo.droidcontroller.connection

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class PermissionManager(private val context: Context) {
    private lateinit var onPermissionResult: OnPermissionResult
    private val requestMultiplePermissions =
        (context as ComponentActivity).registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var allPermissionGranted = true
            permissions.entries.forEach {
                Log.e(cl.jacevedo.droidcontroller.TAG, "${it.key} : ${it.value}")
                allPermissionGranted = allPermissionGranted && it.value
            }
            if (allPermissionGranted) {
                onPermissionResult.onPermissionGranted()
            } else {
                Toast.makeText(context, "Bluetooth permission is required", Toast.LENGTH_SHORT).show()
                onPermissionResult.onPermissionError()
            }
        }
    private val bluetoothEnablingResult = (context as ComponentActivity).registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Bluetooth is enabled, good to go
        } else {
            // User dismissed or denied Bluetooth prompt
            requestPermissionsForBluetooth()
        }
    }
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Activity.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    fun requestPermissions(onPermissionResult: OnPermissionResult){
        this.onPermissionResult = onPermissionResult
        if(context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED || context.checkSelfPermission(
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsForBluetooth()
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).apply {
                bluetoothEnablingResult.launch(this)
            }
            return
        }
        this.onPermissionResult.onPermissionGranted()
    }

    private fun requestPermissionsForBluetooth(){
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
    }
}
interface OnPermissionResult {
    fun onPermissionGranted()
    fun onPermissionError()
}
