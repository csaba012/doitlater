package hu.pcsaba.android.doitlater.data

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "todoItem")
data class ListItem(
	@ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "details") var details: String,
	@ColumnInfo(name = "date") var date: String,
	@ColumnInfo(name = "is_deleted") var isDeleted: Boolean,
	@ColumnInfo(name = "date_year") var year: Int,
	@ColumnInfo(name = "date_month") var month: Int,
	@ColumnInfo(name = "date_day") var day: Int
) {

}