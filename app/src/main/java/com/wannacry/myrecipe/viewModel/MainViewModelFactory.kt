package com.wannacry.myrecipe.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wannacry.myrecipe.repository.FavoriteMealRepository
import com.wannacry.myrecipe.repository.RecipeRepository

class MainViewModelFactory(
    private val favoriteMealRepository: FavoriteMealRepository,
    private val recipeRepository: RecipeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(favoriteMealRepository, recipeRepository) as T
    }
}