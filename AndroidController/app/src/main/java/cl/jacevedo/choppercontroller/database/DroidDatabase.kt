package cl.jacevedo.choppercontroller.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.jacevedo.choppercontroller.data.ButtonDroidEntity

@Database(entities = [ButtonDroidEntity::class], version = 1, exportSchema = false)
abstract class DroidDatabase : RoomDatabase(){
    abstract fun buttonDroidDao(): ButtonDroidDao
}