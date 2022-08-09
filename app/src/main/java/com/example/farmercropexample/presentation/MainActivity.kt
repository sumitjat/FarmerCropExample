package com.example.farmercropexample.presentation

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farmercropexample.R
import com.example.farmercropexample.databinding.ActivityMainBinding
import com.example.farmercropexample.presentation.adapter.FarmerCropAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = FarmerCropAdapter()
        binding.rvGithubRepo.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { uiState ->
                    binding.progressCircular.isVisible = false
                    when (uiState) {
                        is State.FAILURE -> {
                            Toast.makeText(this@MainActivity, uiState.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        State.LOADING -> {
                            binding.progressCircular.isVisible = true
                        }
                        State.START -> {
                            binding.progressCircular.isVisible = true
                        }
                        is State.SUCCESS -> {
                            adapter.submitList(uiState.users.records.toMutableList())
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.showSearch.collect {
                binding.svFilter.isVisible = it
            }
        }


        binding.rgFilter.setOnCheckedChangeListener { _, _ ->

            when (binding.rgFilter.checkedRadioButtonId) {
                R.id.rb_all -> viewModel.setFilter("")
                R.id.rb_district -> viewModel.setFilter("district")
                R.id.rb_state -> viewModel.setFilter("state")
                R.id.rb_market -> viewModel.setFilter("market")
            }
        }

        binding.svFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchValue(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchValue(newText)
                return false
            }
        }


        )

    }
}