package com.example.user.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;

    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipeList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(MainActivity.this, recipeList);
        recyclerView.setAdapter(recipeAdapter);

        getRecipes();

    }

    /**
     * gets recipe list from db or server asynchronously
     */
    private void getRecipes(){
        if (recipeList.isEmpty()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RecipeClient.RECIPE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RecipeClient client = retrofit.create(RecipeClient.class);

            Call<List<Recipe>> call = client.getRecipes();

            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    List<Recipe> list = response.body();
                    Log.d("RECIPES", "from server: " + list.toString());
                    recipeList = list;
                    recipeAdapter.addAll(recipeList);

                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {}
            });
        }
    }

}
