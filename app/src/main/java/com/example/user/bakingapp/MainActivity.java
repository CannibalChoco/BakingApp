package com.example.user.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user.bakingapp.data.database.AppDatabase;
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

// TODO: check network connection before doing network actions
// TODO: refresh db once a day
// TODO: if new recipes are found, notify user

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;

    private AppDatabase appDb;
    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        appDb = AppDatabase.getInstance(this);

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

//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                recipeList = appDb.recipeDao().loadAllRecipes();
//
//                List<Ingredient> ingredientList = new ArrayList<>();
//                List<Step> stepList = new ArrayList<>();
//                ingredientList = appDb.ingredientDao().loadAllIngredients();
//                stepList = appDb.stepDao().loadAllSteps();
//
//                recipeAdapter.addAll(recipeList);
//                Log.d("RECIPES", "load from db");
//                Log.d("RECIPES", "From db : " + recipeList.toString());
//                Log.d("RECIPES", "From db : " + ingredientList.toString());
//                Log.d("RECIPES", "From db : " + stepList.toString());
//            }
//        });

        // TODO: why populated lists are entering this condition? .size() == 0, list == null, list.isEmpty()
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
                    //Log.d("RECIPES", "load from server");
                    //Log.d("RECIPES", "From server : " + recipeList.toString());

                    //insertRecipesInDb();
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {}
            });
        }
    }

    private void getDataFromDb() {

    }

    private void getDataFromServer() {

    }

    private void insertRecipesInDb(){
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("RECIPES", "inserting in db");
//                appDb.recipeDao().insertAllRecipes(recipeList);
//            }
//        });
    }


}
