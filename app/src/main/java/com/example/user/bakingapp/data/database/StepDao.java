package com.example.user.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.bakingapp.model.Step;

import java.util.List;

@Dao
public interface StepDao {

//    @Query("SELECT * FROM step WHERE recipeId LIKE :recipeId ORDER BY id ASC")
//    List<Step> loadAllRecipeSteps(long recipeId);

    @Insert
    void insertTask(Step step);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Step step);

    @Delete
    void deleteTask(Step step);

}
