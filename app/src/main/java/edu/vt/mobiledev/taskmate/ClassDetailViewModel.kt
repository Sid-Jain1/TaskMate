package edu.vt.mobiledev.taskmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
//Siddharth Jain
//PID: siddharthjain
class ClassDetailViewModel(classId: UUID) : ViewModel() {
    private val repository = TaskRepository.get()

    private val _classItem: MutableStateFlow<ClassItem?> = MutableStateFlow(null)
    val classItem get() = _classItem.asStateFlow()

    fun updateClass(classItem: ClassItem) {
        viewModelScope.launch {
            repository.updateClass(classItem)
        }
    }


    override fun onCleared() {
        super.onCleared()
        classItem.value?.let { classData ->
            viewModelScope.launch {
                TaskRepository.get().updateClass(classData)
            }
        }
    }


    init {
        viewModelScope.launch {
            _classItem.value = repository.getClass(classId)
        }
    }
}

class ClassDetailViewModelFactory(private val classId: UUID) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClassDetailViewModel(classId) as T
    }
}
