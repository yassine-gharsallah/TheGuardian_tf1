package com.tf1.Guardianapp.ui.articlelists

import androidx.lifecycle.MutableLiveData
import com.tf1.Guardianapp.api.GuardianApiStatus
import com.tf1.Guardianapp.api.GuardianService
import com.tf1.Guardianapp.api.model.asDatabaseModel
import com.tf1.Guardianapp.database.ArticleDatabase
import com.tf1.Guardianapp.domain.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ArticlesRepository @Inject constructor(
    private val database: ArticleDatabase,
    private val guardianService: GuardianService,
) {

    private val _feedStatus = MutableLiveData<GuardianApiStatus>()
    val feedStatus get() = _feedStatus

    private val _detailStatus = MutableLiveData<GuardianApiStatus>()
    val detailStatus get() = _detailStatus

    suspend fun getLatestFintechArticles() {
        _feedStatus.postValue(GuardianApiStatus.LOADING)
        withContext(Dispatchers.IO) {
            try {
                val articleResponse = guardianService.searchArticlesAsync("football and brexit").await()
                withContext(Dispatchers.Main) {
                    _feedStatus.postValue(GuardianApiStatus.SUCCESS)
                }
                database.articleDao().insertAll(*articleResponse.response.results.asDatabaseModel())
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _feedStatus.postValue(GuardianApiStatus.ERROR)
                }
                exception.printStackTrace()
            }
        }
    }

    suspend fun getArticleDetails(article: Article): Article {
        _detailStatus.postValue(GuardianApiStatus.LOADING)
        try {
            val articleDetailResponse = guardianService.getArticleAsync(article.url, "headline,thumbnail,body").await()
            val apiArticle = articleDetailResponse.response.content.asDatabaseModel()
            database.articleDao().update(apiArticle.copy(body = article.body, favourite = article.favourite))
            withContext(Dispatchers.Main) {
                _detailStatus.postValue(GuardianApiStatus.SUCCESS)
            }
            return apiArticle.asDomainModel()
        } catch (exception: Exception) {
            withContext(Dispatchers.Main) {
                _detailStatus.postValue(GuardianApiStatus.ERROR)
            }
            exception.printStackTrace()
        }
        return article
    }
}
