package com.wannacry.myrecipe.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.activities.CategoryMealsActivity
import com.wannacry.myrecipe.activities.MainActivity
import com.wannacry.myrecipe.adapter.RecyclerViewAdapter
import com.wannacry.myrecipe.data.Category
import com.wannacry.myrecipe.databinding.FragmentCategoriesBinding
import com.wannacry.myrecipe.databinding.LayoutCategoryItemBinding
import com.wannacry.myrecipe.utils.Contants.CATEGORY_NAME
import com.wannacry.myrecipe.viewModel.MainViewModel

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private var _categoriesBinding: FragmentCategoriesBinding? = null
    private val categoriesBinding get() = _categoriesBinding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var categoriesRecyclerViewAdapter: RecyclerViewAdapter<Category>
    private lateinit var layoutCategoryItemBinding: LayoutCategoryItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = (activity as MainActivity).mainViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _categoriesBinding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return categoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCategories()
    }

    private fun observeCategories() {
        mainViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            setupCategoriesRecyclerView(categories)
        }
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
            layoutCategoryItemBinding.cvCategoryItem.setBackgroundColor(requireContext().getColor(R.color.white))
            layoutCategoryItemBinding.tvCategory.text = category.strCategory
            Glide
                .with(this@CategoriesFragment)
                .load(category.strCategoryThumb)
                .placeholder(R.drawable.img_category_placeholder)
                .error(R.drawable.img_error)
                .into(layoutCategoryItemBinding.ivCategory)
            onCategoryClicked(category)
        }
        categoriesBinding.rvCategories.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _categoriesBinding = null
    }
}