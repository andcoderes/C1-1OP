package cl.jacevedo.choppercontroller.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
enum class DeviceType : Parcelable {
    CHOPPER,
    MOUSE
}

@Parcelize
data class BluetoothDroidObject(val nameDevice : String,
                                val macAddress: String,
                                val associationId : Int,
                                val deviceType : DeviceType) : Parcelable
