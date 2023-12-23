package hu.pcsaba.android.doitlater.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.pcsaba.android.doitlater.data.ListItem
import hu.pcsaba.android.doitlater.databinding.ItemDeletedListBinding
import hu.pcsaba.android.doitlater.databinding.ItemListBinding

class ListDeletedAdapter(private val listener: ListItemClickListener) :
	RecyclerView.Adapter<ListDeletedAdapter.ListViewHolder>() {

	private val items = mutableListOf<ListItem>()
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
		ItemDeletedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
	)

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val todo_item = items[position]

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
		holder.binding.btnRestore.setOnClickListener {
			listener.onItemRestored(todo_item)
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

	interface ListItemClickListener {
		fun onItemChanged(item: ListItem)
		fun onItemDeleted(item: ListItem)
		fun onItemRestored(item: ListItem)
		fun onItemEdit(item: ListItem)
	}

	inner class ListViewHolder(val binding: ItemDeletedListBinding) : RecyclerView.ViewHolder(binding.root)
}