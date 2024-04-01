package com.wannacry.myrecipe.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.databinding.ActivityMainBinding
import com.wannacry.myrecipe.databinding.FragmentEditDeleteBottomSheetBinding
import com.wannacry.myrecipe.db.MyRecipeDatabase
import com.wannacry.myrecipe.repository.FavoriteMealRepository
import com.wannacry.myrecipe.repository.RecipeRepository
import com.wannacry.myrecipe.viewModel.MainViewModel
import com.wannacry.myrecipe.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    val mainViewModel: MainViewModel by lazy {
        val recipeDatabase = MyRecipeDatabase.getInstance(this)
        val recipeRepository = RecipeRepository(recipeDatabase)
        val myRecipeDatabase = MyRecipeDatabase.getInstance(this)
        val favoriteMealRepository = FavoriteMealRepository(myRecipeDatabase)
        val mainViewModelFactory = MainViewModelFactory(favoriteMealRepository, recipeRepository)
        ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val contentView = activityMainBinding.root
        setContentView(contentView)

        val bnvMain = activityMainBinding.bnvMain
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcvMain) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bnvMain, navController)
    }
}