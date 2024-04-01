package com.wannacry.myrecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wannacry.myrecipe.data.Meal

@Dao
interface FavoriteMealDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMealToFavorites(meal: Meal)

    @Delete
    suspend fun removeMealFromFavorites(meal: Meal)

    @Query("SELECT * FROM recipe_db")
    fun getAllFavoriteMeals(): LiveData<List<Meal>>
}