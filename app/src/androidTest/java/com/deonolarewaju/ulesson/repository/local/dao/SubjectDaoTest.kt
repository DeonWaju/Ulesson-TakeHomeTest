package com.deonolarewaju.ulesson.repository.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.deonolarewaju.ulesson.repository.local.ProjectDatabaseSetup
import com.deonolarewaju.ulesson.util.TestObjectUtil
import com.deonolarewaju.ulesson.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SubjectDaoTest : ProjectDatabaseSetup() {

    @Test
    fun insertAndReadSubjects() {
        saveSubjects()

        val result = subjectDao.getAllSubjects().getOrAwaitValue()
        assertThat(result, `is`(TestObjectUtil.subjects))
        assertThat(result.size, `is`(TestObjectUtil.subjects.size))
    }

    @Test
    fun getSingleSubjectAndRead() = runBlocking {
        saveSubjects()

        val result = subjectDao.getSubject(TestObjectUtil.subjects[0].id)
        assertThat(result, `is`(TestObjectUtil.subjects[0]))
        assertThat(result.id, `is`(TestObjectUtil.subjects[0].id))
    }


    private fun saveSubjects() = runBlocking {
        val subjects = TestObjectUtil.subjects
        subjectDao.saveSubjects(subjects)
    }

}