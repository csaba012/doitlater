package hu.pcsaba.android.doitlater.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.pcsaba.android.doitlater.R
import hu.pcsaba.android.doitlater.adapter.ListAdapter
import hu.pcsaba.android.doitlater.data.ListItem
import hu.pcsaba.android.doitlater.data.ListItemDatabase
import hu.pcsaba.android.doitlater.databinding.FragmentNewItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class NewItemFragment : Fragment() {
	lateinit var binding: FragmentNewItemBinding
	private lateinit var database: ListItemDatabase
	private var date = ""
	private var date_asDATE: LocalDate? = null
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNewItemBinding.inflate(inflater, container, false)
		return binding.root;
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		database = ListItemDatabase.getDatabase(requireActivity().applicationContext)
		binding.datePickerLayout.setOnClickListener {
			findNavController().navigate(R.id.action_newItemFragment_to_datePickerDialogFragment)
		}
		binding.dateRemoveButton.setOnClickListener {
			date = ""
			date_asDATE = null
			binding.datePickerText.text = context?.getString(R.string.datum_nelkul)
			binding.dateRemoveButton.visibility = View.GONE
		}
		binding.btnCancel.setOnClickListener {
			findNavController().navigate(R.id.action_newItemFragment_to_listFragment)
		}
		binding.btnSave.setOnClickListener {
			var item = ListItem(
				name = if(binding.titleEdittext.text.isNotEmpty()) binding.titleEdittext.text.toString() else getString(R.string.cim_nelkul),
				details = binding.descriptionEdittext.text.toString(),
				date = date,
				isDeleted = false,
				year = date_asDATE?.year ?: 1970,
				month =  date_asDATE?.monthValue ?: 1,
				day = date_asDATE?.dayOfMonth ?: 1)
			thread {
				val insertID = database.listItemDao().insert(item)
				item.id = insertID
			}
			findNavController().navigate(R.id.action_newItemFragment_to_listFragment)
		}
		findNavController()
			.currentBackStackEntry
			?.savedStateHandle
			?.getLiveData<DatePickerDialogFragment.DatePickerResult>(DATE_SELECTED_KEY)
			?.observe(viewLifecycleOwner) {
				result ->
					date = result.year.toString() + "." + result.month + "." + result.dayOfMonth
					date_asDATE = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.M.d"))
					binding.datePickerText.text = result.month.toString() + "." + result.dayOfMonth.toString()
					binding.dateRemoveButton.visibility = View.VISIBLE
			}
	}

	override fun onResume() {
		super.onResume()
		binding.titleEdittext.requestFocus()
	}

	companion object {
		const val DATE_SELECTED_KEY = "date_selected"
	}
}