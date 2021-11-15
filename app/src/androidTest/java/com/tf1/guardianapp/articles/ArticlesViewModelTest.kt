package com.tf1.guardianapp.articles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.tf1.guardianapp.ui.articlelists.ArticlesRepository
import com.tf1.guardianapp.ui.articlelists.ArticlesViewModel
import com.tf1.guardianapp.util.RxSchedulerRule
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ArticlesViewModelTest {
    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    /*
    private val repository = mock<ArticlesRepository>()

    private val viewModel get() = ArticlesViewModel(repository)

    @Test
    suspend fun test() {
        whenever(repository.getLatestFootballArticles()).thenReturn(Single.just(emptyList()))
        Assert.assertNotNull(viewModel.state.value)
    }


     */
}
