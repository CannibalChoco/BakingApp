package com.example.user.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadJSONFromAsset();

        Type recipeListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
        List<Recipe> recipes = new Gson().fromJson(json, recipeListType);

    }

    /**
     * method taken from https://stackoverflow.com/a/19945484
     */
    public void loadJSONFromAsset() {
        try {
            InputStream is = this.getAssets().open("recipe.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }
}
