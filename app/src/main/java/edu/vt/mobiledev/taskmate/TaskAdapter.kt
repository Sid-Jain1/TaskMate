package edu.vt.mobiledev.taskmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.taskmate.databinding.ListItemTaskBinding

class TaskHolder(
    private val binding: ListItemTaskBinding
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var boundTask: Task
        private set

    fun bind(task: Task) {
        boundTask = task

        binding.taskTitle.text = task.title
        binding.taskStatus.text = "Status: ${task.kind}"

        val iconRes = when (task.kind) {
            TaskKind.COMPLETED -> R.drawable.ic_check_circle
            TaskKind.LATE -> R.drawable.ic_warning
            TaskKind.OPTIONAL -> R.drawable.ic_optional
            TaskKind.ASSIGNED -> R.drawable.ic_assignment
        }

        binding.taskIcon.setImageResource(iconRes)
    }
}

class TaskAdapter(
    private val tasks: List<Task>
) : RecyclerView.Adapter<TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTaskBinding.inflate(inflater, parent, false)
        return TaskHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size
}
