package hu.pcsaba.android.doitlater.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.pcsaba.android.doitlater.R
import hu.pcsaba.android.doitlater.data.ListItem
import hu.pcsaba.android.doitlater.databinding.ItemListBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ListAdapter(private val listener: ListItemClickListener) :
	RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

	private val items = mutableListOf<ListItem>()
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
		ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
	)

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val todo_item = items[position]

		holder.binding.titleLayout.visibility = View.VISIBLE
		holder.binding.tvDetails.visibility = View.VISIBLE
		holder.binding.tvDate.visibility = View.VISIBLE

		val time = Calendar.getInstance()
		val formatter = SimpleDateFormat("yyyy.M.d")
		val ma = formatter.format(time.time)
		time.add(Calendar.DAY_OF_YEAR, 1)
		val holnap = formatter.format(time.time)
		time.add(Calendar.DAY_OF_YEAR, 1)
		val holnaputan = formatter.format(time.time)

		if(position == 0) {
			if(todo_item.date == "") {
				holder.binding.titleText.text = holder.itemView.context.getString(R.string.kozeljovoben)
			}
			else if(todo_item.date == ma) {
				holder.binding.titleText.text = holder.itemView.context.getString(R.string.ma_datummal, ma.substring(5))
			}
			else if(todo_item.date == holnap) {
				holder.binding.titleText.text = holder.itemView.context.getString(R.string.holnap_datummal, holnap.substring(5))
			}
			else {
				holder.binding.titleText.text = holder.itemView.context.getString(R.string.kozeljovoben)
			}
		}
		else {
			if(toDate(items[position - 1].date).compareTo(toDate(todo_item.date)) < 0) {
				if(todo_item.date == "") {
					holder.binding.titleText.text = holder.itemView.context.getString(R.string.kozeljovoben)
				}
				else if(todo_item.date == ma) {
					holder.binding.titleText.text = holder.itemView.context.getString(R.string.ma_datummal, ma.substring(5))
				}
				else if(todo_item.date == holnap) {
					holder.binding.titleText.text = holder.itemView.context.getString(R.string.holnap_datummal, holnap.substring(5))
				}
				else if(toDate(todo_item.date).compareTo(toDate(ma)) < 0) {
					holder.binding.titleLayout.visibility = View.GONE
				}
				else if(toDate(items[position - 1].date).compareTo(toDate(holnaputan)) < 0) {
					holder.binding.titleText.text = holder.itemView.context.getString(R.string.kozeljovoben)
				}
				else {
					holder.binding.titleLayout.visibility = View.GONE
				}
			}
			else {
				holder.binding.titleLayout.visibility = View.GONE
			}
		}

		holder.binding.tvName.text = todo_item.name
		if(todo_item.details != "")
			holder.binding.tvDetails.text = todo_item.details
		else
			holder.binding.tvDetails.visibility = View.GONE
		if(todo_item.date != "")
			holder.binding.tvDate.text = todo_item.date.substring(5)
		else
			holder.binding.tvDate.visibility = View.GONE
		holder.binding.btnDelete.setOnClickListener{
			listener.onItemDeleted(todo_item)
		}
		holder.binding.taskText.setOnClickListener {
			listener.onItemEdit(todo_item)
		}
	}

	override fun getItemCount(): Int = items.size

	fun update(todo_items: List<ListItem>) {
		items.clear()
		items.addAll(todo_items)
		notifyDataSetChanged()
	}

	fun toDate(date_string: String): LocalDate{
		if(date_string == "")
			return LocalDate.MIN
		return LocalDate.parse(date_string, DateTimeFormatter.ofPattern("yyyy.M.d"))
	}

	interface ListItemClickListener {
		fun onItemChanged(item: ListItem)
		fun onItemDeleted(item: ListItem)
		fun onItemEdit(item: ListItem)
	}

	inner class ListViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}