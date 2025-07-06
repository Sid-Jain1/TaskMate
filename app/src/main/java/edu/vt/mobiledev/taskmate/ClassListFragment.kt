package edu.vt.mobiledev.taskmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.vt.mobiledev.taskmate.databinding.FragmentClassListBinding
import kotlinx.coroutines.launch
import java.util.Date


class ClassListFragment : Fragment() {

    private var _binding: FragmentClassListBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: ClassListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassListBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        binding.classRecycler.layoutManager = LinearLayoutManager(context)

        // Add FAB-style button listener
        binding.addClassButton.setOnClickListener {
            showAddClassDialog()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.classes.collect { classList ->
                    val allTasks = classList.flatMap { it.tasks }
                    val total = allTasks.size
                    val completed = allTasks.count { it.kind == TaskKind.COMPLETED }

                    binding.progressBar.max = total.coerceAtLeast(1)
                    binding.progressBar.progress = completed

                    binding.summaryText.text = "Completed: $completed / $total"
                    binding.classStats.text = "Classes: ${classList.size}, Assignments: $total"

                    binding.classRecycler.adapter = ClassListAdapter(classList) { classId ->
                        findNavController().navigate(
                            ClassListFragmentDirections.showClassDetail(classId)
                        )
                    }
                }
            }
        }
    }

    private fun showAddClassDialog() {
        val input = EditText(requireContext()).apply {
            hint = "Class Name"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Add New Class")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val name = input.text.toString()
                if (name.isNotBlank()) {
                    val newClass = ClassItem(
                        name = name,
                        description = "",
                        lastUpdated = Date()
                    )
                    viewModel.addClass(newClass)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
