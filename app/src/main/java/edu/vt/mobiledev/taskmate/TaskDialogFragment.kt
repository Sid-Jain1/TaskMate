package edu.vt.mobiledev.taskmate

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import edu.vt.mobiledev.taskmate.databinding.FragmentTaskDialogBinding

class TaskDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentTaskDialogBinding.inflate(layoutInflater)

        val kindValues = TaskKind.entries.map { it.name }
        binding.taskKindSpinner.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            kindValues
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val positiveListener = DialogInterface.OnClickListener { _, _ ->
            val title = binding.taskTitleInput.text.toString()
            val kind = TaskKind.valueOf(binding.taskKindSpinner.selectedItem as String)

            setFragmentResult(
                REQUEST_KEY,
                bundleOf(
                    TASK_NAME_KEY to title,
                    TASK_KIND_KEY to kind
                )
            )
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.task_dialog_title)
            .setPositiveButton(R.string.task_dialog_positive, positiveListener)
            .setNegativeButton(R.string.task_dialog_negative, null)
            .create()
    }

    companion object {
        const val REQUEST_KEY = "edu.vt.mobiledev.taskmate.FragmentTaskDialog.Request"
        const val TASK_NAME_KEY = "edu.vt.mobiledev.taskmate.FragmentTaskDialog.TaskName"
        const val TASK_KIND_KEY = "edu.vt.mobiledev.taskmate.FragmentTaskDialog.TaskKind"
    }
}
