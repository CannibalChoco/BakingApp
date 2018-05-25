package com.example.user.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.bakingapp.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SharedPreferencesUtils {

    public static String BAKING_SHARED_PREFERENCES = "bakingSharedPrefs";

    public static void saveListInPreferences(Context context, List<Ingredient> ingredients){
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);

        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BakingAppConstants.KEY_INGREDIENT_LIST, json);
        editor.apply();
    }

    public static List<Ingredient> getIngredientListFromPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);
        String json = preferences.getString(BakingAppConstants.KEY_INGREDIENT_LIST, "");

        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Ingredient>>(){}.getType());
    }
}
