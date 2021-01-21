package com.deonolarewaju.ulesson.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.deonolarewaju.ulesson.util.Event
import com.deonolarewaju.ulesson.repository.source.repository.Repository

class HomeViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _subjects = MutableLiveData<Unit>()

    private val _openSubjectId = MutableLiveData<Event<Long>>()
    val openSubjectId: LiveData<Event<Long>> = _openSubjectId

    private val _toggleText = MutableLiveData<String>()
    val toggleText: LiveData<String> = _toggleText

    val recentViews = toggleText.switchMap {
        when (it) {
            "VIEW ALL" -> repository.getRecentViews(2)
            else -> repository.getRecentViews(1000)
        }
    }

    fun getSubjects() {
        _subjects.value = Unit
    }

    fun toggleButton(text: String) {
        when (text) {
            "VIEW ALL" -> {
                _toggleText.value = "VIEW LESS"
            }
            "VIEW LESS" -> {
                _toggleText.value = "VIEW ALL"
            }
        }
    }

    fun openSubject(subjectId: Long) {
        _openSubjectId.value = Event(subjectId)
    }

    val fetchingSubject = _subjects.switchMap {
        repository.fetchSubjects()
    }

    val subjects = repository.getSubjects()

}