package com.example.user.bakingapp.utils;

import com.example.user.bakingapp.RecipeClient;
import com.example.user.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    /**
     * gets recipe list from server
     */
    public static Call<List<Recipe>> getRecipesFromServer(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipeClient.RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeClient client = retrofit.create(RecipeClient.class);

        return client.getRecipes();
    }
}
