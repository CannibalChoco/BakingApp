package com.example.user.bakingapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    long id;
    //long recipeId;
    private float quantity;
    private String measure;
    private String ingredient;

    public Ingredient(long id, /*long recipeId,*/ float quantity, String measure, String ingredient) {
        this.id = id;
        //this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    @Ignore
    public Ingredient(/*long recipeId, */float quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public long getId() {
        return id;
    }

//    public long getRecipeId() {
//        return recipeId;
//    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
