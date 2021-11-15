package com.tf1.guardianapps.articles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tf1.Guardianapp.ui.articlelists.ArticlesRepository
import com.tf1.Guardianapp.ui.articlelists.ArticlesViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Rule
import org.junit.Test

class ArticlesViewModelTest {
    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository = mock<ArticlesRepository>()

    private val viewModel get() = ArticlesViewModel(repository)

    @Test
    fun test() {
        whenever(repository.latestFintechArticles()).thenReturn(Single.just(emptyList()))
        Assert.assertNotNull(viewModel.state.value)
    }
}
