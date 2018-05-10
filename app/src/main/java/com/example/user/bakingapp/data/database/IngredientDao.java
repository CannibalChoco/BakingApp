package com.example.user.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.bakingapp.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

//    @Query("SELECT * FROM ingredient WHERE recipeId LIKE :recipeId ORDER BY id ASC")
//    List<Ingredient> loadAllIngredients(long recipeId);

    @Insert
    void insertRecipe(Ingredient ingredient);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Ingredient ingredient);

    @Delete
    void deleteTask(Ingredient ingredient);

}
