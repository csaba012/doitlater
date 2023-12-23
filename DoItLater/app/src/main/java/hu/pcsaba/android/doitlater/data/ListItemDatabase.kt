package hu.pcsaba.android.doitlater.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ListItem::class], version = 1)
abstract class ListItemDatabase : RoomDatabase() {
	abstract fun listItemDao(): ListItemDao

	companion object {
		fun getDatabase(applicationContext: Context): ListItemDatabase {
			return Room.databaseBuilder(
				applicationContext,
				ListItemDatabase::class.java,
				"todo-list"
			).build()
		}
	}
}