package edu.vt.mobiledev.taskmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.taskmate.databinding.ListItemTaskBinding
/**
 * Displaying a single Task item in the RecyclerView.
 */
class TaskHolder(
    private val binding: ListItemTaskBinding
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var boundTask: Task
        private set

    /**
     * Bind a Task object to the UI elements.
     */
    fun bind(task: Task) {
        boundTask = task

        // Set the task title and status text
        binding.taskTitle.text = task.title
        binding.taskStatus.text = "Status: ${task.kind}"

        // Set the icon based on the task kind
        val iconRes = when (task.kind) {
            TaskKind.COMPLETED -> R.drawable.ic_check_circle
            TaskKind.LATE -> R.drawable.ic_warning
            TaskKind.OPTIONAL -> R.drawable.ic_optional
            TaskKind.ASSIGNED -> R.drawable.ic_assignment
        }

        binding.taskIcon.setImageResource(iconRes)
    }
}
/**
 * Adapter class managing a list of tasks in a RecyclerView.
 */
class TaskAdapter(
    private val tasks: List<Task>
) : RecyclerView.Adapter<TaskHolder>() {
    /**
     * Create a new ViewHolder by inflating the item layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTaskBinding.inflate(inflater, parent, false)
        return TaskHolder(binding)
    }
    /**
     * Bind the task at the current position to the ViewHolder.
     */
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(tasks[position])
    }
    /**
     * Return the total number of tasks in the list.
     */
    override fun getItemCount(): Int = tasks.size
}
