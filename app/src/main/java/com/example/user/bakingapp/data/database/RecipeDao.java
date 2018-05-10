package com.example.user.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.bakingapp.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    List<Recipe> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRecipe(Recipe recipe);

    @Insert
    void insertAllRecipes(List<Recipe> recipes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Recipe recipe);

    @Delete
    void deleteTask(Recipe recipe);

}
