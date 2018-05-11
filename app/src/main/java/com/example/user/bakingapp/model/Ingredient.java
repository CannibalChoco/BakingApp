package com.example.user.bakingapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient",
        foreignKeys = {@ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "parent_id")})
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "parent_id")
    long parentId;
    private float quantity;
    private String measure;
    private String ingredient;

    public Ingredient(long id, long parentId, float quantity, String measure, String ingredient) {
        this.id = id;
        this.parentId = parentId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    @Ignore
    public Ingredient(long parentId, float quantity, String measure, String ingredient) {
        this.parentId = parentId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
