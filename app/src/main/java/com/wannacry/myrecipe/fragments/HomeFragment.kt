package com.wannacry.myrecipe.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wannacry.myrecipe.activities.CategoryMealsActivity
import com.wannacry.myrecipe.activities.MealActivity
import com.wannacry.myrecipe.fragments.bottomSheet.MealInfoBottomSheetFragment
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.activities.MainActivity
import com.wannacry.myrecipe.adapter.RecyclerViewAdapter
import com.wannacry.myrecipe.data.Category
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.data.PopularMeal
import com.wannacry.myrecipe.databinding.FragmentHomeBinding
import com.wannacry.myrecipe.databinding.LayoutCategoryItemBinding
import com.wannacry.myrecipe.databinding.LayoutPopularMealItemBinding
import com.wannacry.myrecipe.databinding.LayoutRecipeItemBinding
import com.wannacry.myrecipe.fragments.bottomSheet.EditDeleteBottomSheetFragment
import com.wannacry.myrecipe.utils.Contants.CATEGORY_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_ID
import com.wannacry.myrecipe.utils.Contants.MEAL_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_THUMB
import com.wannacry.myrecipe.viewModel.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!
    private lateinit var mainViewModel: MainViewModel

    private lateinit var recipeRecyclerViewAdapter: RecyclerViewAdapter<Meal>
    private lateinit var popularMealsRecyclerViewAdapter: RecyclerViewAdapter<PopularMeal>
    private lateinit var categoriesRecyclerViewAdapter: RecyclerViewAdapter<Category>

    private lateinit var layoutRecipeItemBinding: LayoutRecipeItemBinding
    private lateinit var layoutPopularMealItemBinding: LayoutPopularMealItemBinding
    private lateinit var layoutCategoryItemBinding: LayoutCategoryItemBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = (activity as MainActivity).mainViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getRandomMeal()
        mainViewModel.getPopularMeals("Seafood")
        mainViewModel.getCategories()
        observeRandomMeal()
        observerRecipe()
        observePopularMeals()
        observeCategories()
        onSearchIconClicked()
    }

    private fun observeRandomMeal() {
        mainViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { randomMeal ->
            showRandomMealImageInView(randomMeal)
            onRandomMealClicked(randomMeal)
        }
    }

    private fun observePopularMeals() {
        mainViewModel.observePopularMealsLiveData().observe(viewLifecycleOwner) { popularMeals ->
            setupPopularMealRecyclerView(popularMeals)

        }
    }

    private fun observerRecipe() {
        mainViewModel.observerRecipeLiveData().observe(viewLifecycleOwner) { recipe ->
            setupRecipeRecycleView(recipe)
        }
    }

    private fun observeCategories() {
        mainViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            setupCategoriesRecyclerView(categories)
        }
    }

    private fun observeMealDetails() {
        mainViewModel.observeMealDetailsLiveData().observe(viewLifecycleOwner) { meal ->
            showMealInfoBottomSheet(meal)
        }
    }

    private fun observeRecipeEditDelete() {
        mainViewModel.observeMealDetailsLiveData().observe(viewLifecycleOwner) { meal ->
            showEditDeleteBottomSheet(meal)
        }

    }

    private fun showRandomMealImageInView(randomMeal: Meal) {
        Glide
            .with(this@HomeFragment)
            .load(randomMeal.strMealThumb)
            .centerCrop()
            .placeholder(R.drawable.img_meal_placeholder)
            .error(R.drawable.img_error)
            .into(homeBinding.ivImageRandomMeal)
    }

    private fun onRandomMealClicked(randomMeal: Meal) {
        homeBinding.cvRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, randomMeal.idMeal)
                putExtra(MEAL_NAME, randomMeal.strMeal)
                putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun setupRecipeRecycleView(meal: List<Meal>) {
        val linearHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recipeRecyclerViewAdapter =
            RecyclerViewAdapter(R.layout.layout_recipe_item, meal, true) { view, meal, _ ->
                layoutRecipeItemBinding = LayoutRecipeItemBinding.bind(view)
                Glide
                    .with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .centerInside()
                    .placeholder(R.drawable.img_meal_placeholder)
                    .error(R.drawable.img_error)
                    .into(layoutRecipeItemBinding.ivRecipeItem)
                onRecipeClicked(meal)
                onRecipeLongClicked(meal)
            }
        homeBinding.rvRecipe.apply {
            adapter = recipeRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = linearHorizontalLayoutManager
        }
    }

    private fun onRecipeClicked(meal: Meal) {
        layoutRecipeItemBinding.ivRecipeItem.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, meal.idMeal)
                putExtra(MEAL_NAME, meal.strMeal)
                putExtra(MEAL_THUMB, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun onRecipeLongClicked(meal: Meal) {
        layoutRecipeItemBinding.ivRecipeItem.setOnLongClickListener {
            mainViewModel.getMealDetailsById(meal.idMeal)
            observeRecipeEditDelete()
            return@setOnLongClickListener true
        }
    }

    private fun setupPopularMealRecyclerView(popularMeals: List<PopularMeal>) {
        val linearHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularMealsRecyclerViewAdapter = RecyclerViewAdapter(
            R.layout.layout_popular_meal_item,
            popularMeals,
            true
        ) { view, popularMeal, _ ->
            layoutPopularMealItemBinding = LayoutPopularMealItemBinding.bind(view)
            Glide
                .with(this@HomeFragment)
                .load(popularMeal.strMealThumb)
                .centerInside()
                .placeholder(R.drawable.img_meal_placeholder)
                .error(R.drawable.img_error)
                .into(layoutPopularMealItemBinding.ivPopularMealItem)
            onPopularMealClicked(popularMeal)
            onPopularMealLongClicked(popularMeal)
        }
        homeBinding.rvPopularMeals.apply {
            adapter = popularMealsRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = linearHorizontalLayoutManager
        }
    }

    private fun onPopularMealClicked(popularMeal: PopularMeal) {
        layoutPopularMealItemBinding.ivPopularMealItem.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, popularMeal.idMeal)
                putExtra(MEAL_NAME, popularMeal.strMeal)
                putExtra(MEAL_THUMB, popularMeal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun onPopularMealLongClicked(popularMeal: PopularMeal) {
        layoutPopularMealItemBinding.ivPopularMealItem.setOnLongClickListener {
            mainViewModel.getMealDetailsById(popularMeal.idMeal)
            observeMealDetails()
            return@setOnLongClickListener true
        }
    }

    private fun showMealInfoBottomSheet(meal: Meal) {
        val mealInfoBottomSheetFragment = MealInfoBottomSheetFragment.newInstance(
            meal.idMeal,
            meal.strMeal,
            meal.strArea,
            meal.strCategory,
            meal.strMealThumb
        )
        mealInfoBottomSheetFragment.show(childFragmentManager, "Meal Info Bottom Sheet")
    }

    private fun showEditDeleteBottomSheet(meal: Meal) {
        val mealInfoBottomSheetFragment = EditDeleteBottomSheetFragment.newInstance(
            meal.idMeal,
            meal.strMeal,
            meal.strArea,
            meal.strCategory,
            meal.strMealThumb
        )
        mealInfoBottomSheetFragment.show(childFragmentManager, "Edit Delete Bottom Sheet")
    }

    private fun setupCategoriesRecyclerView(categories: List<Category>) {
        val verticalGridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        categoriesRecyclerViewAdapter = RecyclerViewAdapter(
            R.layout.layout_category_item,
            categories,
            true
        ) { view, category, _ ->
            layoutCategoryItemBinding = LayoutCategoryItemBinding.bind(view)
            layoutCategoryItemBinding.tvCategory.text = category.strCategory
            Glide
                .with(this@HomeFragment)
                .load(category.strCategoryThumb)
                .placeholder(R.drawable.img_category_placeholder)
                .error(R.drawable.img_error)
                .into(layoutCategoryItemBinding.ivCategory)
            onCategoryClicked(category)
        }
        homeBinding.rvCategories.apply {
            adapter = categoriesRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = verticalGridLayoutManager
        }
    }

    private fun onCategoryClicked(category: Category) {
        layoutCategoryItemBinding.llCategoryItem.setOnClickListener {
            val intent = Intent(requireContext(), CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun onSearchIconClicked() {
        homeBinding.ivSearchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchMealsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeBinding = null
    }
}