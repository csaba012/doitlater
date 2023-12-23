package hu.pcsaba.android.doitlater.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavAction
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.pcsaba.android.doitlater.R
import hu.pcsaba.android.doitlater.adapter.ListAdapter
import hu.pcsaba.android.doitlater.data.ListItem
import hu.pcsaba.android.doitlater.data.ListItemDatabase
import hu.pcsaba.android.doitlater.databinding.FragmentListBinding
import kotlin.concurrent.thread


class ListFragment : Fragment(), ListAdapter.ListItemClickListener {
	private lateinit var binding: FragmentListBinding
	private lateinit var database: ListItemDatabase
	private lateinit var adapter: ListAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentListBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		database = ListItemDatabase.getDatabase(requireActivity().applicationContext)
		binding.fab.setOnClickListener {
			findNavController().navigate(R.id.action_listFragment_to_newItemFragment)
		}
		initRecyclerView()

	}
	private fun initRecyclerView() {
		adapter = ListAdapter(this)
		binding.rvMain.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
		binding.rvMain.adapter = adapter
		loadItemsInBackground()
	}

	private fun loadItemsInBackground() {
		thread {
			val items = database.listItemDao().getAll()
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

	override fun onItemDeleted(item: ListItem) {
		thread {
			item.isDeleted = true
			database.listItemDao().update(item)
			val items = database.listItemDao().getAll()
			activity?.runOnUiThread {
				adapter.update(items)
			}
		}
	}

	override fun onItemEdit(item: ListItem) {
		val bundle = bundleOf("item" to item.id)
		findNavController().navigate(R.id.action_listFragment_to_editItemFragment, bundle)
	}
}