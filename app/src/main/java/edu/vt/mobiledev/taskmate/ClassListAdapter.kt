package edu.vt.mobiledev.taskmate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.taskmate.databinding.ListItemClassBinding
import java.util.UUID

class ClassHolder(
    private val binding: ListItemClassBinding
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var boundClass: ClassItem
        private set

    fun bind(classItem: ClassItem, onClassClicked: (UUID) -> Unit) {
        boundClass = classItem

        binding.className.text = classItem.name

        val completed = classItem.completedCount
        val total = classItem.totalCount
        binding.classProgress.text = "$completed / $total tasks completed"

        binding.root.setOnClickListener {
            onClassClicked(classItem.id)
        }
    }
}

class ClassListAdapter(
    private val classes: List<ClassItem>,
    private val onClassClicked: (UUID) -> Unit
) : RecyclerView.Adapter<ClassHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemClassBinding.inflate(inflater, parent, false)
        return ClassHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassHolder, position: Int) {
        holder.bind(classes[position], onClassClicked)
    }

    override fun getItemCount() = classes.size
}
