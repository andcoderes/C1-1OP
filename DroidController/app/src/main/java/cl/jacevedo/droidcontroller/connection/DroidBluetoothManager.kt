package cl.jacevedo.droidcontroller.connection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.core.content.getSystemService
import cl.jacevedo.droidcontroller.TAG
import cl.jacevedo.droidcontroller.communication.ICommunication
import cl.jacevedo.droidcontroller.data.BluetoothDroidObject
import cl.jacevedo.droidcontroller.data.DeviceType
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import java.util.UUID
import java.util.regex.Pattern

const val SERVICE_UUID = "96e3f2cd-28cf-4d37-9b39-a291f917620e"
const val CHOPPER_CHARACTERISTIC = "9c5c0655-5501-4c0d-a91b-defbd9dea110"
const val MOUSE_CHARACTERISTIC = "fe96f496-952a-47c2-a13e-0bfb03617bfb"
const val SELECT_DEVICE_REQUEST_CODE = 0
class DroidBluetoothManager {
    var bluetoothGatt : BluetoothGatt? = null

     suspend fun scanningForDevice(context: Context, onDeviceConnected: (BluetoothDroidObject) -> Unit ):IntentSender{
        val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
            .setNamePattern(Pattern.compile("/* Droid"))
            .build()
        val deviceManager = context.getSystemService<CompanionDeviceManager>()
        val pairingRequest: AssociationRequest = AssociationRequest.Builder()
            .addDeviceFilter(deviceFilter)
            .setSingleDevice(false)
            .build()

        val result = CompletableDeferred<IntentSender>()
        val callback = object : CompanionDeviceManager.Callback() {
            override fun onAssociationPending(intentSender: IntentSender) {
                startIntentSenderForResult(context as Activity, intentSender, SELECT_DEVICE_REQUEST_CODE, null, 0,0,0, null)
            }

            @Suppress("OVERRIDE_DEPRECATION")
            override fun onDeviceFound(intentSender: IntentSender) {
                result.complete(intentSender)
            }

            @SuppressLint("MissingPermission")
            override fun onAssociationCreated(associationInfo: AssociationInfo) {
                val associationId: Int = associationInfo.id
                val macAddress: String? = associationInfo.deviceMacAddress?.toString()
                val deviceName: String?= associationInfo.displayName?.toString()
                if(macAddress == null  || deviceName == null){
                    Toast.makeText(context, "Error getting mac address or device name", Toast.LENGTH_SHORT).show()
                } else {
                    associationInfo.associatedDevice?.bluetoothDevice?.createBond()
                    onDeviceConnected(BluetoothDroidObject(
                        associationId = associationId,
                        nameDevice = deviceName,
                        deviceType = if(deviceName.contains("Chopper")){DeviceType.CHOPPER} else { DeviceType.MOUSE},
                        macAddress = macAddress
                    ))
                }

            }

            override fun onFailure(errorMessage: CharSequence?) {}
        }
        deviceManager?.associate(pairingRequest, callback, null)
        return result.await()
    }

    fun connectBluetoothDroid(context: Context,
                              bluetoothAddress : String?,
                              gattCallback: (newState: Int) -> Unit,
                              onCharacteristicMessage: (value:String) -> Unit){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val device = bluetoothManager.adapter.bondedDevices.firstOrNull{bluetoothAddress != null && it.address.compareTo(bluetoothAddress, true) == 0}
        device?.connectGatt(context, false, object : BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                gatt?.readRemoteRssi()
                bluetoothGatt = gatt
                val characteristicString = if(bluetoothGatt?.device?.name?.contains("mouse", true) == true){
                    MOUSE_CHARACTERISTIC
                } else {
                    CHOPPER_CHARACTERISTIC
                }
                Log.e(TAG, "characteristing $characteristicString")
                bluetoothGatt?.getService(UUID.fromString(SERVICE_UUID))?.getCharacteristic(UUID.fromString(characteristicString))?.let{
                    Log.e(TAG, "characteristic found")
                   bluetoothGatt?.setCharacteristicNotification(it, true)
                }
            }

            override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
                super.onReadRemoteRssi(gatt, rssi, status)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                onCharacteristicMessage(value.decodeToString())

                super.onCharacteristicChanged(gatt, characteristic, value)
            }

            override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
                super.onMtuChanged(gatt, mtu, status)
            }

            @SuppressLint("MissingPermission")
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                Log.d(TAG, "Gatt status: $status, newState: $newState")
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.d(TAG, "Connected")
                    bluetoothGatt = gatt
                    bluetoothGatt?.requestMtu(128)
                    bluetoothGatt?.discoverServices()
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.d(TAG, "Disconnected")
                    bluetoothGatt = null
                }
                gattCallback(newState)
                super.onConnectionStateChange(gatt, status, newState)
            }



            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray,
                status: Int
            ) {
               Log.e(TAG, "Read" + value.decodeToString())
            }

        })
    }

    @SuppressLint("MissingPermission")
    fun sendDroidObjectMessage(buttonActions : ICommunication){
        val jsonMessage = Gson().toJson(buttonActions)
        bluetoothGatt?.run {
            services.firstOrNull { it.uuid == UUID.fromString(SERVICE_UUID) }
            val service : BluetoothGattService? = getService(UUID.fromString(SERVICE_UUID))
            val characteristicString = if(bluetoothGatt?.device?.name?.contains("mouse", true)==true){
                MOUSE_CHARACTERISTIC
            } else {
                CHOPPER_CHARACTERISTIC
            }
            Log.e(TAG, "looking for characteristic $characteristicString service different from null ${service != null}")

            var characteristic : BluetoothGattCharacteristic? = service?.getCharacteristic(UUID.fromString(characteristicString))
            if(characteristic == null){
                characteristic =  service?.characteristics?.firstOrNull()
            }

            Log.e(TAG, "characteristing $characteristicString find: $characteristic")
            characteristic?.let {
                writeCharacteristic(it, jsonMessage.toByteArray(), BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        bluetoothGatt?.disconnect()
    }
}