package cl.jacevedo.droidcontroller

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import cl.jacevedo.droidcontroller.connection.DroidBluetoothManager
import cl.jacevedo.droidcontroller.connection.OnPermissionResult
import cl.jacevedo.droidcontroller.connection.PermissionManager
import cl.jacevedo.droidcontroller.storage.BluetoothDroidStorage
import cl.jacevedo.droidcontroller.ui.theme.ChopperControllerTheme
import cl.jacevedo.droidcontroller.view.MainActivityView
import cl.jacevedo.droidcontroller.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val SELECTED_DROID = "selected_droid"
const val TAG = "Droids Apps"
class MainActivity : ComponentActivity() {
    private val permissionManager by lazy {
        PermissionManager(this)
    }
    private val mainActivityViewModel : MainActivityViewModel by viewModels()

    @RequiresPermission(value = "android. permission. BLUETOOTH_CONNECT")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mainActivityViewModel.setList(BluetoothDroidStorage(this).getDroids())
        setContent {
            ChopperControllerTheme {
                MainActivityView(
                    mainActivityViewModel = mainActivityViewModel,
                    onAddDeviceClick = {
                        val bluetoothManager =  DroidBluetoothManager()
                        CoroutineScope(Dispatchers.IO).launch {
                            bluetoothManager.scanningForDevice(this@MainActivity){ newDevice ->
                                mainActivityViewModel.addItem(newDevice, this@MainActivity)
                            }
                        }
                    },
                    onDeviceClick = { device ->
                        val bundle = Bundle()
                        bundle.putParcelable(SELECTED_DROID, device)
                        val i = Intent(this@MainActivity, DroidActivity::class.java)
                        i.putExtras(bundle)
                        startActivity(i)
                    }
                )
            }
        }
        requestPermissions()
    }

    private fun requestPermissions(){
       permissionManager.requestPermissions(object : OnPermissionResult {
            @SuppressLint("MissingPermission")
            override fun onPermissionGranted() {
                Log.e(TAG, "permission granted")

            }
            override fun onPermissionError() {
                Log.e(TAG, "error on permission")
            }
        })
    }
}