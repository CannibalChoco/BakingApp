package com.example.user.bakingapp;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

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

    public static final String KEY_RECIPE_LIST = "recipe_list";
    public static final String KEY_RECIPE = "recipe";

    private static List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipeList = new ArrayList<>();

        getRecipes();
    }

    /**
     * gets recipe list from db or server asynchronously
     */
      private void getRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipeClient.RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeClient client = retrofit.create(RecipeClient.class);

        Call<List<Recipe>> call = client.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList = response.body();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_RECIPE_LIST, (ArrayList<Recipe>) recipeList);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                MasterListFragment masterListFragment = new MasterListFragment();
                masterListFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, masterListFragment).commit();

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
            }
        });
    }
}
