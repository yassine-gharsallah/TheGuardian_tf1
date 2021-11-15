package com.tf1.Guardianapp.ui.articlelists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tf1.Guardianapp.api.GuardianApiStatus
import com.tf1.Guardianapp.domain.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ArticleListFragment : Fragment(R.layout.fragment_article_list),
    ArticleAdapter.ArticleOnClickListener {
    private val viewModel: ArticlesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)
        setHasOptionsMenu(true)
    }

    private fun setupBinding(view: View) {
        val binding = FragmentArticleListBinding.bind(view)
        val articleAdapter = ArticleAdapter(this)
        binding.apply {
            lifecycleOwner = this@ArticleListFragment
            articlesViewModel = viewModel
            articlesRecyclerview.adapter = articleAdapter
            articlesSwiperefreshlayout.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            articleAdapter.submitList(it)
        })
        viewModel.feedStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                GuardianApiStatus.LOADING -> binding.articlesSwiperefreshlayout.isRefreshing = true
                GuardianApiStatus.SUCCESS -> binding.articlesSwiperefreshlayout.isRefreshing = false
                GuardianApiStatus.ERROR -> {
                    binding.articlesSwiperefreshlayout.isRefreshing = false
                    Snackbar.make(requireView(), "Oops! Something went wrong, pleasue check internet connection", Snackbar.LENGTH_LONG).show()
                }
            }
            viewModel.feedStatus.postValue(null)
        })
        viewModel.detailStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                GuardianApiStatus.LOADING -> binding.articlesSwiperefreshlayout.isRefreshing = true
                GuardianApiStatus.SUCCESS -> binding.articlesSwiperefreshlayout.isRefreshing = false
                GuardianApiStatus.ERROR -> {
                    binding.articlesSwiperefreshlayout.isRefreshing = false
                    Snackbar.make(requireView(), "Oops! Something went wrong, pleasue check internet connection", Snackbar.LENGTH_LONG).show()
                }
            }
            viewModel.detailStatus.postValue(null)
        })
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.articleEvent.collect { event ->
                when (event) {
                    is ArticlesViewModel.ArticleEvent.NavigateToArticleDetail -> {
                        findNavController().navigate(ArticleListFragmentDirections.actionArticleListFragmentToArticleDetailFragment(event.article))
                    }
                }
            }
        }
    }

    override fun articleFavourited(article: Article, isFavourite: Boolean) {
        viewModel.onArticleFavourited(article, isFavourite)
    }

    override fun articleClicked(article: Article) {
        viewModel.onArticleClicked(article)
    }

    //Todo
    // Add more menu options to further improve the filtering process
    // Include --
    // Search by text
    // Sort by ASC / DESC
    // Potential use of pills to filter news articles based on section (i.e. football/politics)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_menu, menu)
        val favouritesItem = menu.findItem(R.id.select_favourites)
        viewModel.showFavourites.observe(viewLifecycleOwner, Observer {isSelected ->
            if (isSelected) {
                favouritesItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite)
            } else {
                favouritesItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_non_favourite)
            }
            favouritesItem.isChecked = isSelected
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_favourites -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) {
                    item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite)
                } else {
                    item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_non_favourite)
                }
                viewModel.showFavourites(item.isChecked)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}