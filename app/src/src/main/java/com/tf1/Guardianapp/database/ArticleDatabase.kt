package com.tf1.Guardianapp.database

import androidx.room.*
import com.tf1.Guardianapp.database.model.DBArticle
import kotlinx.coroutines.flow.Flow

@Database(entities = [DBArticle::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg articles: DBArticle)

    @Update
    suspend fun update(article: DBArticle)

    @Query("SELECT * FROM article_table WHERE (favourite == :showFavourites OR favourite = 1) ORDER BY published DESC")
    fun getArticles(showFavourites: Boolean = false): Flow<List<DBArticle>>

    @Query("SELECT * FROM article_table WHERE id == :articleId")
    fun getSingleArticle(articleId: String) : Flow<DBArticle>
}
