package com.example.project_farmcare

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.project_farmcare.Adapters.CategoriesAdapter
import com.example.project_farmcare.databinding.FragmentHomeBinding
import com.example.project_farmcare.packages.CategoryMealsActivity
import com.example.project_farmcare.pojo.Meal

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var randomMeal: Meal

    companion object{
        const val MEAL_ID =     "com.example.project_farmcare.idMeal"
        const val MEAL_NAME =     "com.example.project_farmcare.nameMeal"
        const val MEAL_THUMB =     "com.example.project_farmcare.thumbMeal"

        const val CATEGORY_NAME = "com.example.project_farmcare.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated (view, savedInstanceState)


        homeMvvm.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()
        categoriesRecyclerView()

        homeMvvm.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()

    }

    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener({
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        })
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun categoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.CategoryRecView.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        homeMvvm.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })

    }

    private fun observeRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner, {
            meal ->
                Glide.with(this@HomeFragment)
                        .load(meal.strMealThumb)
                        .into(binding.imgRandomMeal)
                this.randomMeal = meal
        })
    }


}

