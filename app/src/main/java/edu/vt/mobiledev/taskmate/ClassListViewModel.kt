package edu.vt.mobiledev.taskmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ClassListViewModel : ViewModel() {

    private val repository = TaskRepository.get()

    val classes: StateFlow<List<ClassItem>> =
        repository.getClasses()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun addClass(classItem: ClassItem) {
        viewModelScope.launch {
            repository.addClass(classItem)
        }
    }

    fun deleteClass(classItem: ClassItem) {
        viewModelScope.launch {
            repository.deleteClass(classItem)
        }
    }

    fun updateClass(classItem: ClassItem) {
        viewModelScope.launch {
            repository.updateClass(classItem)
        }
    }

    suspend fun getClassById(id: UUID): ClassItem {
        return repository.getClass(id)
    }
}
