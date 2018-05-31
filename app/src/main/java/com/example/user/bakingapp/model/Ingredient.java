package com.example.user.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Ingredient implements Parcelable {

    private static final String MEASURE_UNIT = "UNIT";

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

    public String getMeasureUnit() {
        if(measure.contentEquals(MEASURE_UNIT)) return "";
        return measure.toLowerCase();
    }

    public String getIngredient() {
        return ingredient.substring(0,1).toUpperCase() + ingredient.substring(1);
    }

    public String getQuantityWithMeasure(){
        String formattedQuantityWithMeasure = getQuantity();
        String measure = getMeasureUnit();

        if (!measure.isEmpty()){
            return formattedQuantityWithMeasure + " " + measure;
        }

        return formattedQuantityWithMeasure;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
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
