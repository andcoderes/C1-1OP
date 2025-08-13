package cl.jacevedo.droidcontroller.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.jacevedo.droidcontroller.data.ButtonDroidEntity

@Database(entities = [ButtonDroidEntity::class], version = 1, exportSchema = false)
abstract class DroidDatabase : RoomDatabase(){
    abstract fun buttonDroidDao(): ButtonDroidDao
}