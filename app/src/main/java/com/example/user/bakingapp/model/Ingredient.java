package com.example.user.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Ingredient implements Parcelable {

    private float quantity;
    private String measure;
    private String ingredient;

    protected Ingredient(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getQuantity() {
        return String.format(Locale.getDefault(), "%.0f", quantity);
    }

    public String getMeasure() {
        return measure.toLowerCase();
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }

    public String getIngredientFormatted(){
        return quantity + " " + measure.toLowerCase() +
                " " + ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
