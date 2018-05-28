package com.example.user.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SharedPreferencesUtils {

    public static String BAKING_SHARED_PREFERENCES = "bakingSharedPrefs";

    public static void saveRecipeInPreferences(Context context, Recipe recipe){
        Gson gson = new Gson();
        String json = gson.toJson(recipe);

        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BakingAppConstants.KEY_RECIPE, json);
        editor.apply();
    }

    public static void saveListInPreferences(Context context, List<Ingredient> ingredients){
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);

        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BakingAppConstants.KEY_INGREDIENT_LIST, json);
        editor.apply();
    }

    public static void saveRecipeNameInPreferences (Context context, String name){
        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BakingAppConstants.KEY_RECIPE_NAME, name);
        editor.apply();
    }

    public static Recipe getRecipeFromPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);
        String json = preferences.getString(BakingAppConstants.KEY_RECIPE, "");

        Recipe recipe = new Gson().fromJson(json, Recipe.class);

        return recipe;
    }

    public static List<Ingredient> getIngredientListFromPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);
        String json = preferences.getString(BakingAppConstants.KEY_INGREDIENT_LIST, "");

        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Ingredient>>(){}.getType());
    }

    public static String getRecipeNameFromPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);
        String json = preferences.getString(BakingAppConstants.KEY_INGREDIENT_LIST, "");

        return preferences.getString(BakingAppConstants.KEY_RECIPE_NAME,
                BakingAppConstants.DEFAULT_RECIPE_NAME_FOR_WIDGET);
    }

}
