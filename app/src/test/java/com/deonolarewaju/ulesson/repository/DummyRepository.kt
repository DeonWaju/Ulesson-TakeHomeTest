package com.deonolarewaju.ulesson.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.deonolarewaju.ulesson.util.Resources
import com.deonolarewaju.ulesson.util.Resources.Status
import com.deonolarewaju.ulesson.repository.model.RecentlyViewed
import com.deonolarewaju.ulesson.repository.model.Subject
import com.deonolarewaju.ulesson.repository.source.repository.Repository
import com.deonolarewaju.ulesson.util.TestObjectUtil

class DummyRepository : Repository {

    private var shouldReturnError = false

    private val subjects = mutableListOf<Subject>()
    private val recentviews = mutableListOf<RecentlyViewed>()

    private val observeSubjects = MutableLiveData<List<Subject>>()
    private val observeRecentViews = MutableLiveData<List<RecentlyViewed>>()

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun fetchSubjects(): LiveData<Resources<Unit>> {
        return liveData {
            val response = if (shouldReturnError) {
                Resources.error("error occurred", null)
            } else {
                Resources.success(TestObjectUtil.subjects)
            }

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        saveSubjects(it)
                    }
                    emit(Resources.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resources.error(response.message!!, null))
                }
                else -> {
                }
            }
        }
    }

    override fun getSubjects(): LiveData<List<Subject>> {
        return observeSubjects
    }

    override suspend fun saveRecentView(recentlyViewed: RecentlyViewed) {
        this.recentviews.add(recentlyViewed)
        refreshData()
    }

    override fun getRecentViews(limit: Int): LiveData<List<RecentlyViewed>> {
        observeRecentViews.value = recentviews.take(limit)
        return observeRecentViews
    }

    override suspend fun saveSubjects(subjects: List<Subject>) {
        this.subjects.addAll(subjects)
        refreshData()
    }

    override suspend fun getSubject(id: Long): Resources<Subject> {
        val subject = subjects.find { it.id == id }
        return Resources.success(subject!!)
    }

    fun saveAllRecentView(list: List<RecentlyViewed>) {
        recentviews.addAll(list)
        refreshData()
    }

    private fun refreshData() {
        observeSubjects.value = subjects
        observeRecentViews.value = recentviews
    }

}