package com.example.testeapp.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testeapp.R
import com.example.testeapp.common.BaseActivity
import com.example.testeapp.databinding.ActivityMainBinding
import com.example.testeapp.model.Post
import com.example.testeapp.model.PostWithUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    val adapterPost = PostAdapter(object : OnPostItemClickListener {
        override fun onFavoriteClicked(post: PostWithUser) {
            viewModel.update(post)
        }
    })

    override fun observeData() {

        lifecycleScope.launch {
            viewModel.state
                .filter { !it.postList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    it.postList?.let {
                        adapterPost.submitList((adapterPost.currentList + it).distinct())
                        binding.recyclerview.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { it.inProgress == true }
                .distinctUntilChanged()
                .collect {
                    binding.loading.visibility = View.VISIBLE
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { !it.errorMessage.isNullOrEmpty() && it.postList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    binding.error.apply {
                        text = (it.errorMessage + "\n Click to try again")
                        visibility = View.VISIBLE
                        setOnClickListener {
                            viewModel.fetchPosts()
                        }
                    }
                    binding.loading.visibility = View.GONE
                    binding.recyclerview.visibility = View.GONE
                }
        }

    }

    override fun setUpUi() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterPost
            val dividerItemDecoration = DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
            val verticalDivider = ContextCompat.getDrawable(context, R.drawable.divisor)

            if (verticalDivider != null) {
                dividerItemDecoration.setDrawable(verticalDivider)
            }
            addItemDecoration(dividerItemDecoration)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisibleItem + 5) {
                        viewModel.isLoading()
                        viewModel.page++
                        viewModel.fetchPosts()
                    }
                }
            })
        }
    }


}