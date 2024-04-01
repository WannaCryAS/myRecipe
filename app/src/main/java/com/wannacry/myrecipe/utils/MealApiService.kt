package com.wannacry.myrecipe.utils

import com.wannacry.myrecipe.data.CategoryList
import com.wannacry.myrecipe.data.MealByCategoryList
import com.wannacry.myrecipe.data.MealList
import com.wannacry.myrecipe.data.PopularMealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetailsById(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularMeals(@Query("c") categoryName: String): Call<PopularMealList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery: String): Call<MealList>
}