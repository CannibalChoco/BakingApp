package com.example.user.bakingapp;

import com.example.user.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeClient {

    String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @SuppressWarnings("SpellCheckingInspection")
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
