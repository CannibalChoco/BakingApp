package com.example.user.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.user.bakingapp.R;
import com.example.user.bakingapp.RecipeClient;
import com.example.user.bakingapp.adapter.RecipeAdapter;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;

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
@SuppressWarnings("WeakerAccess")
public class RecipeListActivity extends AppCompatActivity implements
        RecipeAdapter.OnRecipeClickListener {

    private List<Recipe> recipeList;

    @BindView(R.id.recipe_list_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    private SimpleIdlingResource idlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        recipeList = new ArrayList<>();

//        if(savedInstanceState == null){
//            getRecipes();
//        }

        getIdlingResource();

        getRecipes();

    }

    /**
     * gets recipe list from server asynchronously
     */
    private void getRecipes() {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

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
                setUpUi();

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
            }
        });
    }

    private void setUpUi() {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600){
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }

        RecipeAdapter adapter = new RecipeAdapter(this, recipeList, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onRecipeSelected(int position) {
        Recipe recipe = recipeList.get(position);

        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingAppConstants.KEY_RECIPE, recipe);

        Intent intent = new Intent(RecipeListActivity.this,
                MasterDetailActivity.class);

        intent.putExtra(BakingAppConstants.KEY_RECIPE_BUNDLE, bundle);

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        startActivity(intent);
    }

}
