package hu.pcsaba.android.doitlater.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.pcsaba.android.doitlater.R
import hu.pcsaba.android.doitlater.adapter.ListDeletedAdapter
import hu.pcsaba.android.doitlater.data.ListItem
import hu.pcsaba.android.doitlater.data.ListItemDatabase
import hu.pcsaba.android.doitlater.databinding.FragmentListDeletedBinding
import kotlin.concurrent.thread


class ListDeletedFragment : Fragment(), ListDeletedAdapter.ListItemClickListener {
	private lateinit var binding: FragmentListDeletedBinding
	private lateinit var database: ListItemDatabase
	private lateinit var adapter: ListDeletedAdapter
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentListDeletedBinding.inflate(inflater, container, false)

		return binding.root;
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		database = ListItemDatabase.getDatabase(requireActivity().applicationContext)
		initRecyclerView()

	}
	private fun initRecyclerView() {
		adapter = ListDeletedAdapter(this)
		binding.rvMain.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
		binding.rvMain.adapter = adapter
		loadItemsInBackground()
	}

	private fun loadItemsInBackground() {
		thread {
			val items = database.listItemDao().getAllDeleted()
			activity?.runOnUiThread {
				adapter.update(items)
			}
		}
	}

	override fun onItemChanged(item: ListItem) {
		thread {
			database.listItemDao().update(item)
		}
	}

	override fun onItemRestored(item: ListItem) {
		thread {
			item.isDeleted = false
			database.listItemDao().update(item)
			val items = database.listItemDao().getAllDeleted()
			activity?.runOnUiThread {
				adapter.update(items)
			}
		}
	}

	override fun onItemDeleted(item: ListItem) {
		thread {
			database.listItemDao().deleteItem(item)
			val items = database.listItemDao().getAllDeleted()
			activity?.runOnUiThread {
				adapter.update(items)
			}
		}
	}

	override fun onItemEdit(item: ListItem) {
		val bundle = bundleOf("item" to item.id)
		findNavController().navigate(R.id.action_listDeletedFragment_to_editDeteledItemFragment, bundle)
	}
}