package hu.pcsaba.android.doitlater.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListItemDao {
	@Query("SELECT * FROM todoItem WHERE is_deleted == 0 ORDER BY CASE WHEN date = '' THEN 0 ELSE 1 END, date_year, date_month, date_day, name")
	fun getAll(): List<ListItem>

	@Query("SELECT * FROM todoItem WHERE is_deleted == 1 ORDER BY date DESC")
	fun getAllDeleted(): List<ListItem>

	@Query("SELECT * FROM todoItem WHERE id == :id_param")
	fun getFromId(id_param: Long?): ListItem
	@Insert
	fun insert(items: ListItem): Long

	@Update
	fun update(item: ListItem)

	@Delete
	fun deleteItem(item: ListItem)
}