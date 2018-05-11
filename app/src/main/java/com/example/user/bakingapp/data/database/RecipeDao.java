package com.example.user.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.model.Step;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    List<Recipe> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRecipe(Recipe recipe);

    // TODO: is this correct?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllRecipes(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllRecipesWithChildren(List<Recipe> recipes, List<Ingredient> ingredients, List<Step> steps);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

}
