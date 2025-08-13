package cl.jacevedo.droidcontroller.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ButtonDroidEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    var deviceId: Int?,
    var buttonType: Int?,
    val label: String?,
    val macro: String?
)