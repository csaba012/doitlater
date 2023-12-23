package hu.pcsaba.android.doitlater.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import hu.pcsaba.android.doitlater.R
import java.io.Serializable
import java.util.Calendar

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val c = Calendar.getInstance()
		val year = c.get(Calendar.YEAR)
		val month = c.get(Calendar.MONTH)
		val day = c.get(Calendar.DAY_OF_MONTH)
		return DatePickerDialog(requireContext(), R.style.CustomDatePickerDialog, this, year, month, day)
	}

	data class DatePickerResult(
		val year: Int,
		val month: Int,
		val dayOfMonth: Int,
	) : Serializable

	override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
		val result = DatePickerResult(year, month + 1, dayOfMonth)
		findNavController()
			.previousBackStackEntry
			?.savedStateHandle
			?.set(NewItemFragment.DATE_SELECTED_KEY, result)
	}
}