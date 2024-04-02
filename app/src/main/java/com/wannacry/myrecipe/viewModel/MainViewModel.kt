package com.wannacry.myrecipe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.retrofit.MealApiClient
import com.wannacry.myrecipe.data.Category
import com.wannacry.myrecipe.data.CategoryList
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.data.MealList
import com.wannacry.myrecipe.data.PopularMeal
import com.wannacry.myrecipe.data.PopularMealList
import com.wannacry.myrecipe.repository.FavoriteMealRepository
import com.wannacry.myrecipe.repository.RecipeRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel(
    private val favoriteMealRepository: FavoriteMealRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private var randomMealState: Meal? = null
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealsLiveData = MutableLiveData<List<PopularMeal>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = favoriteMealRepository.getAllFavoriteMeals()
    private var recipeLiveData = recipeRepository.getAllRecipe()
    private var mealDetailsLiveData = MutableLiveData<Meal>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

    fun observeRandomMealLiveData(): LiveData<Meal> = randomMealLiveData
    fun observePopularMealsLiveData(): LiveData<List<PopularMeal>> = popularMealsLiveData
    fun observeCategoriesLiveData(): LiveData<List<Category>> = categoriesLiveData
    fun observeFavoriteMealsLiveData(): LiveData<List<Meal>> = favoriteMealsLiveData
    fun observerRecipeLiveData(): LiveData<List<Meal>> = recipeLiveData
    fun observeMealDetailsLiveData(): LiveData<Meal> = mealDetailsLiveData
    fun observeSearchedMealsLiveData(): LiveData<List<Meal>> = searchedMealsLiveData

    fun getRandomMeal() {
        randomMealState?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }
        val randomMealApiCall = MealApiClient.mealApiService.getRandomMeal()
        randomMealApiCall.enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "RandomMealApiCall",
                        "onResponse Error: ${response.errorBody().toString()}"
                    )
                    return
                }
                if (response.body() == null) {
                    Log.d("RandomMealApiCall", "onResponse Error: null response body")
                    return
                }
                val meals: List<Meal>? = response.body()?.meals
                val randomMeal: Meal = meals!!.first()
                randomMealLiveData.value = randomMeal
                randomMealState = randomMeal
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("RandomMealApiCall", "onFailure: ${t.message}")
            }
        })
    }

    fun getPopularMeals(categoryName: String) {
        val popularMealsApiCall = MealApiClient.mealApiService.getPopularMeals(categoryName)
        popularMealsApiCall.enqueue(object : retrofit2.Callback<PopularMealList> {
            override fun onResponse(
                call: Call<PopularMealList>,
                response: Response<PopularMealList>
            ) {
                if (!response.isSuccessful) {
                    Log.d(
                        "PopularMealsApiCall",
                        "onResponse Error: ${response.errorBody().toString()}"
                    )
                    return
                }
                if (response.body() == null) {
                    Log.d("PopularMealsApiCall", "onResponse Error: null response body")
                    return
                }
                val popularMeals: List<PopularMeal>? = response.body()?.meals
                popularMealsLiveData.value = popularMeals!!
            }

            override fun onFailure(call: Call<PopularMealList>, t: Throwable) {
                Log.d("PopularMealsApiCall", "onFailure: ${t.message}")
            }

        })
    }

    fun getCategories() {
        val categoriesApiCall = MealApiClient.mealApiService.getCategories()
        categoriesApiCall.enqueue(object : retrofit2.Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "CategoriesApiCall",
                        "onResponse Error: ${response.errorBody().toString()}"
                    )
                    return
                }
                if (response.body() == null) {
                    Log.d("CategoriesApiCall", "onResponse Error: null response body")
                    return
                }
                val categories: List<Category>? = response.body()?.categories
                categoriesLiveData.value = categories!!
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("CategoriesApiCall", "onFailure: ${t.message}")
            }

        })
    }

    fun getMealDetailsById(id: String) {
        val mealDetailsApiCall = MealApiClient.mealApiService.getMealDetailsById(id = id)
        mealDetailsApiCall.enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "MealDetailsApiCall",
                        "onResponse Error: ${response.errorBody().toString()}"
                    )
                    return
                }
                if (response.body() == null) {
                    Log.d("MealDetailsApiCall", "onResponse Error: null response body")
                    return
                }
                val meals: List<Meal>? = response.body()?.meals
                if (meals.isNullOrEmpty()) {
                    Log.d("MealDetailsApiCall", "onResponse Error: empty or null meals list")
                    return
                }
                val meal: Meal = meals.first()
                mealDetailsLiveData.value = meal
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealDetailsApiCall", "onFailure: ${t.message}")
            }

        })
    }

    fun searchMeals(searchQuery: String) {
        val searchMealsApiCall = MealApiClient.mealApiService.searchMeals(searchQuery)
        searchMealsApiCall.enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "SearchMealsApiCall",
                        "onResponse Error: ${response.errorBody().toString()}"
                    )
                    return
                }
                if (response.body() == null) {
                    Log.d("SearchMealsApiCall", "onResponse Error: null response body")
                    return
                }
                val searchedMeals: List<Meal>? = response.body()?.meals
                searchedMealsLiveData.value = searchedMeals!!
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("SearchMealsApiCall", "onFailure: ${t.message}")
            }
        })
    }

    fun addRecipe(meal: Meal) {
        //TODO: Will update for edit function
        viewModelScope.launch {
            recipeRepository.addRecipe(meal)
        }
    }

    fun deleteRecipe(id: String) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(id)
        }
    }

    fun removeMealFromFavorites(meal: Meal) {
        viewModelScope.launch {
            favoriteMealRepository.removeMealFromFavorites(meal)
        }
    }

    fun addFromFavorites(meal: Meal) {
        viewModelScope.launch {
            favoriteMealRepository.addMealToFavorites(meal)
        }
    }
}