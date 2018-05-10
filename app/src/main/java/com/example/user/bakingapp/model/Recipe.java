package com.example.user.bakingapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int servings;
    private String image;
//    @Ignore
//    private List<Ingredient> ingredients;
//    @Ignore
//    private List<Step> steps;

    public Recipe (int id, String name, int servings, String image){
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

//    @Ignore
//    public Recipe (String name, int servings, String image, List<Ingredient> ingredients,
//                List<Step> steps){
//        this.name = name;
//        this.servings = servings;
//        this.image = image;
//        this.ingredients = ingredients;
//        this.steps = steps;
//    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public List<Ingredient> getIngredients() {
//        return ingredients;
//    }
//
//    public List<Step> getSteps() {
//        return steps;
//    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
