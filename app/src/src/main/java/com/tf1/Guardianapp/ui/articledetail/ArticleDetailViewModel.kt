package com.tf1.Guardianapp.ui.articledetail

import androidx.lifecycle.ViewModel
import com.tf1.Guardianapp.domain.Article

//Todo
// Not quiet sure how to use Hilt to generate a ViewModel that requires a factory
class ArticleDetailViewModel (val article: Article) : ViewModel()