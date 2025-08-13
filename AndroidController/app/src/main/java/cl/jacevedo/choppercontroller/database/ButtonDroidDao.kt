package cl.jacevedo.choppercontroller.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cl.jacevedo.choppercontroller.data.ButtonDroidEntity

@Dao
interface ButtonDroidDao {
    @Query("SELECT * FROM buttondroidentity")
    fun getAll(): List<ButtonDroidEntity>

    @Query("SELECT * FROM buttondroidentity WHERE deviceId = :deviceId")
    fun loadAllButtonsByDeviceId(deviceId : Int): List<ButtonDroidEntity>

    @Query("SELECT * FROM buttondroidentity WHERE deviceId = :deviceId and buttonType = :buttonType")
    fun loadAllButtonsByDeviceIdAndType(deviceId : Int, buttonType : Int): List<ButtonDroidEntity>

    @Insert
    fun insertAll(vararg buttonsDroidEntity: ButtonDroidEntity)

    @Delete
    fun delete(buttonsDroidEntity: ButtonDroidEntity)

}