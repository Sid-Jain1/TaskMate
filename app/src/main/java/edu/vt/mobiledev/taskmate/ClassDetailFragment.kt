package edu.vt.mobiledev.taskmate

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.taskmate.databinding.FragmentClassDetailBinding
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class ClassDetailFragment : Fragment() {
    //set up viewbinging
    private var _binding: FragmentClassDetailBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private val args: ClassDetailFragmentArgs by navArgs()
    //Setup ViewModel for the screen
    private val viewModel: ClassDetailViewModel by viewModels {
        ClassDetailViewModelFactory(args.classId)
    }
    //Created Launcher for the image capture
    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        binding.taskPhoto.tag = null
        viewModel.classItem.value?.let { updateView(it) }
    }
    // Inflates layout and sets up toolbar menu
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailBinding.inflate(inflater, container, false)

        requireActivity().addMenuProvider(object : MenuProvider {
            // Add menu to toolbar
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_class_detail, menu)
                val captureImageIntent = photoLauncher.contract.createIntent(requireContext(), Uri.EMPTY)
                menu.findItem(R.id.take_photo_menu).isVisible = canResolveIntent(captureImageIntent)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.share_class_menu -> {
                        viewModel.classItem.value?.let { shareClass(it) }
                        true
                    }
                    R.id.take_photo_menu -> {
                        viewModel.classItem.value?.let { takePhoto(it) }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        return binding.root
    }
    // Handles UI setup after layout is inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskRecycler.layoutManager = LinearLayoutManager(context)
        getItemTouchHelper().attachToRecyclerView(binding.taskRecycler)

        // Handles UI setup after layout is inflated
        binding.taskPhoto.setOnClickListener {
            viewModel.classItem.value?.let {
                findNavController().navigate(
                    ClassDetailFragmentDirections.showPhotoDetail("IMG_${it.id}.JPG")
                )
            }
        }
        // Updates class name on text change
        binding.classTitleText.doOnTextChanged { text, _, _, _ ->
            viewModel.classItem.value?.let { currentClass ->
                val updated = currentClass.copy(name = text.toString())
                updated.tasks = currentClass.tasks
                viewModel.updateClass(updated)
            }
        }
        // Launch task creation dialog
        binding.addTaskButton.setOnClickListener {
            findNavController().navigate(ClassDetailFragmentDirections.addTaskDialog())
        }

        // Handle result from task dialog
        setFragmentResultListener(TaskDialogFragment.REQUEST_KEY) { _, bundle ->
            val taskName = bundle.getString(TaskDialogFragment.TASK_NAME_KEY) ?: ""
            val taskKind = bundle.getSerializable(TaskDialogFragment.TASK_KIND_KEY) as? TaskKind ?: TaskKind.ASSIGNED

            viewModel.classItem.value?.let { currentClass ->
                val updatedTasks = currentClass.tasks.toMutableList().apply {
                    add(Task(title = taskName, kind = taskKind, classId = currentClass.id))
                }

                val updated = currentClass.copy(lastUpdated = Date()).also {
                    it.tasks = updatedTasks
                }

                // Save to database
                viewModel.updateClass(updated)

                // ALSO update the in-memory value so new tasks appear immediately
                // (otherwise Flow just keeps the original object)
                viewModel.classItem.value?.tasks = updatedTasks
            }
        }

        // Observe changes to the class item and update UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.classItem.collect { classItem ->
                    classItem?.let {
                        updateView(it)
                        binding.taskRecycler.adapter = TaskAdapter(it.tasks)
                    }
                }
            }
        }
    }
    // Updates the displayed fields with current class info
    private fun updateView(classItem: ClassItem) {
        if (binding.classTitleText.text.toString() != classItem.name) {
            binding.classTitleText.setText(classItem.name)
        }

        binding.lastUpdatedText.text =
            DateFormat.format("'Last updated' yyyy-MM-dd 'at' hh:mm:ss a", classItem.lastUpdated)

        updatePhoto(classItem)
    }
    // Loads and displays the photo associated with a class
    private fun updatePhoto(classItem: ClassItem) {
        val expectedTag = classItem.id.toString()
        with(binding.taskPhoto) {
            if (tag != expectedTag) {
                val photoFile = File(requireContext().applicationContext.filesDir, "IMG_${classItem.id}.JPG")
                if (photoFile.exists()) {
                    doOnLayout { view ->
                        val scaledBitmap = getScaledBitmap(photoFile.path, view.width, view.height)
                        setImageBitmap(scaledBitmap)
                        tag = expectedTag
                        isEnabled = true
                    }
                } else {
                    setImageBitmap(null)
                    tag = null
                    isEnabled = false
                }
            }
        }
    }
    // Opens camera and stores photo using FileProvider
    private fun takePhoto(classItem: ClassItem) {
        val photoFile = File(requireContext().applicationContext.filesDir, "IMG_${classItem.id}.JPG")
        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            "edu.vt.mobiledev.taskmate.fileprovider",
            photoFile
        )
        Log.i("PhotoURI", photoUri.toString())
        photoLauncher.launch(photoUri)
    }
    // Shares class info as plain text
    private fun shareClass(classItem: ClassItem) {
        val shareText = buildString {
            appendLine(classItem.name)
            appendLine()
            appendLine("Last updated ${DateFormat.format("yyyy-MM-dd 'at' hh:mm:ss a", classItem.lastUpdated)}")
            appendLine()
            if (classItem.tasks.isNotEmpty()) {
                appendLine("Tasks:")
                classItem.tasks.forEach {
                    appendLine("• ${it.title} (${it.kind})")
                }
            }
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Shared Class Info")
        }

        val chooser = Intent.createChooser(intent, "Share Class")
        startActivity(chooser)
    }
    // Returns true if an Intent can be handled by the system
    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolvedActivity != null
    }
    // Swipe-to-delete handler for RecyclerView tasks
    private fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskHolder = viewHolder as TaskHolder
                val swipedTask = taskHolder.boundTask

                viewModel.classItem.value?.let { currentClass ->
                    val updatedTasks = currentClass.tasks.filterNot { it == swipedTask }.toMutableList()
                    val updated = currentClass.copy(lastUpdated = Date())
                    updated.tasks = updatedTasks
                    viewModel.updateClass(updated)
                }
            }
        })
    }
    // Clean up binding to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
