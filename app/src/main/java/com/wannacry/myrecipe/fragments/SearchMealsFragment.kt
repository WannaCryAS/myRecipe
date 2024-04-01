package com.wannacry.myrecipe.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.activities.MainActivity
import com.wannacry.myrecipe.activities.MealActivity
import com.wannacry.myrecipe.adapter.RecyclerViewAdapter
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.databinding.FragmentSearchMealsBinding
import com.wannacry.myrecipe.databinding.LayoutMealItemBinding
import com.wannacry.myrecipe.utils.Contants.MEAL_ID
import com.wannacry.myrecipe.utils.Contants.MEAL_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_THUMB
import com.wannacry.myrecipe.viewModel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchMealsFragment : Fragment(R.layout.fragment_search_meals) {
    private var _searchMealsBinding: FragmentSearchMealsBinding? = null
    private val searchMealsBinding get() = _searchMealsBinding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchMealsRecyclerViewAdapter: RecyclerViewAdapter<Meal>
    private lateinit var layoutMealItemBinding: LayoutMealItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = (activity as MainActivity).mainViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _searchMealsBinding = FragmentSearchMealsBinding.inflate(inflater, container, false)
        return searchMealsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchMealsBinding.ivArrowLeft.setOnClickListener {
            searchMeals()
        }
        var searchJob: Job? = null
        searchMealsBinding.etSearchQueryInput.addTextChangedListener {searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                mainViewModel.searchMeals(searchQuery.toString())
                observeSearchedMeals()
            }
        }
    }

    private fun searchMeals(){
        val searchQuery = searchMealsBinding.etSearchQueryInput.text.toString()
        if (searchQuery.isNotEmpty()) {
            mainViewModel.searchMeals(searchQuery)
            observeSearchedMeals()
        }
    }

    private fun observeSearchedMeals(){
        mainViewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner){searchedMeals ->
            setupSearchedMealsRecyclerViewAdapter(searchedMeals)
        }
    }

    private fun setupSearchedMealsRecyclerViewAdapter(searchedMeals: List<Meal>){
        val verticalGridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        searchMealsRecyclerViewAdapter = RecyclerViewAdapter(R.layout.layout_meal_item, searchedMeals, true) {view, searchedMeal, _ ->
            layoutMealItemBinding = LayoutMealItemBinding.bind(view)
            layoutMealItemBinding.tvMealName.text = searchedMeal.strMeal
            Glide
                .with(this@SearchMealsFragment)
                .load(searchedMeal.strMealThumb)
                .centerCrop()
                .placeholder(R.drawable.img_meal_placeholder)
                .error(R.drawable.img_error)
                .into(layoutMealItemBinding.ivMealImage)
            onSearchedMealClicked(searchedMeal)
        }
        searchMealsBinding.rvSearchedMeals.apply {
            adapter = searchMealsRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = verticalGridLayoutManager
        }
    }

    private fun onSearchedMealClicked(searchMeal: Meal){
        layoutMealItemBinding.clMealItem.setOnClickListener {
            val intent = Intent(requireContext(), MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, searchMeal.idMeal)
                putExtra(MEAL_NAME, searchMeal.strMeal)
                putExtra(MEAL_THUMB, searchMeal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _searchMealsBinding = null
    }
}