package com.wannacry.myrecipe.repository

import androidx.lifecycle.LiveData
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.db.MyRecipeDatabase


class FavoriteMealRepository(private val db: MyRecipeDatabase) {
    suspend fun addMealToFavorites(meal: Meal) = db.getFavoriteMealDAO().addMealToFavorites(meal)
    suspend fun removeMealFromFavorites(meal: Meal) =
        db.getFavoriteMealDAO().removeMealFromFavorites(meal)

    fun getAllFavoriteMeals(): LiveData<List<Meal>> = db.getFavoriteMealDAO().getAllFavoriteMeals()
}