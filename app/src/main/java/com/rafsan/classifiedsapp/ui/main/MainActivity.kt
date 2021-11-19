package com.rafsan.classifiedsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rafsan.classifiedsapp.base.BaseActivity
import com.rafsan.classifiedsapp.databinding.ActivityMainBinding
import com.rafsan.classifiedsapp.ui.detail.DetailActivity
import com.rafsan.classifiedsapp.ui.main.adapter.ListingAdapter
import com.rafsan.classifiedsapp.utils.Constants.Companion.CACHE_SIZE
import com.rafsan.classifiedsapp.utils.NetworkResult
import com.rafsan.image_lib.ImageLoader
import com.rafsan.image_lib.cache.CacheType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var imageLoader: ImageLoader
    lateinit var listingAdapter: ListingAdapter

    override fun onViewReady(savedInstanceState: Bundle?) {
        super.onViewReady(savedInstanceState)
        configImageLoader()
        setupRecyclerView()
        setupObservers()
    }

    override fun setBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private fun configImageLoader() {
        imageLoader = ImageLoader.getInstance(this, CACHE_SIZE, CacheType.DISK)
    }

    private fun setupRecyclerView() {
        //Swipe refresh listener
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            mainViewModel.fetchListing()
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener);

        listingAdapter = ListingAdapter(imageLoader)
        binding.rvItems.apply {
            adapter = listingAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
        listingAdapter.setOnItemClickListener { item ->
            //Navigate to detail
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("item", item)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        mainViewModel.listingResponse.observe(this, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressBar()
                    response.data?.let { listingResponse ->
                        listingAdapter.differ.submitList(listingResponse.results)
                    }
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }

                is NetworkResult.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        mainViewModel.errorToast.observe(this, Observer { value ->
            if (value.isNotEmpty()) {
                Toast.makeText(this@MainActivity, value, Toast.LENGTH_LONG).show()
            } else {
                mainViewModel.hideErrorToast()
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}