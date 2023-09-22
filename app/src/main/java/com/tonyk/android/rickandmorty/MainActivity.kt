package com.tonyk.android.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tonyk.android.rickandmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isVisibleBottomBar = when (destination.id) {
                R.id.locationDetailFragment -> false
                R.id.episodeDetailFragment -> false
                R.id.characterDetailFragment -> false
                else -> true
            }
            binding.bottomNavigationView?.isVisible = isVisibleBottomBar
        }
    }
}