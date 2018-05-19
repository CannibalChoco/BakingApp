package com.example.user.bakingapp;


import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
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

// TODO: Style- single method for prev/next buttons

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        DetailFragment.OnNextStepListener{

    public static final String KEY_RECIPE_LIST = "recipe_list";
    public static final String KEY_RECIPE = "recipe";
    public static final String KEY_STEP = "step";
    public static final String KEY_INGREDIENT_LIST = "ingredient_list";
    public static final String KEY_RECIPE_NAME = "recipe_name";
    public static final String KEY_RECIPE_SERVINGS = "servings";
    public static final String KEY_STEP_ID = "step_id";
    public static final String KEY_ADAPTER_SIZE = "adapter_size";

    private static List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipeList = new ArrayList<>();

        if(savedInstanceState == null){
            getRecipes();
        }

    }

    /**
     * gets recipe list from server asynchronously
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

                startRecipeListFragment();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
            }
        });
    }

    private void startRecipeListFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_RECIPE_LIST, (ArrayList<Recipe>) recipeList);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        recipeListFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_container, recipeListFragment).commit();

        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    /**
     * ActionBar up navigation solution taken from:
     * https://stackoverflow.com/a/20314570
     */
    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

//    @Override
//    public void onStepSelected(int position) {
//        MasterListFragment masterListFragment = (MasterListFragment)
//                getSupportFragmentManager().findFragmentByTag(MasterListFragment.TAG);
//
//        if (masterListFragment != null){
//            Log.d("NEXT", "masterFragment not null");
//            masterListFragment.launchNextStep(position);
//        } else {
//            Log.d("NEXT", "masterFragment null");
//        }
//    }

    @Override
    public void onNextStep(int position) {
        Log.d("NEXT", "MainActivity- callback");
        MasterListFragment masterListFragment = (MasterListFragment)
                getSupportFragmentManager().findFragmentByTag(MasterListFragment.TAG);

        if (masterListFragment != null){
            Log.d("NEXT", "masterFragment not null");
            masterListFragment.launchNextStep(position);
        } else {
            Log.d("NEXT", "masterFragment null");
        }
    }

    @Override
    public void onPrevStep(int position) {
        Log.d("NEXT", "MainActivity- callback");
        MasterListFragment masterListFragment = (MasterListFragment)
                getSupportFragmentManager().findFragmentByTag(MasterListFragment.TAG);

        if (masterListFragment != null){
            Log.d("NEXT", "masterFragment not null");
            masterListFragment.launchNextStep(position);
        } else {
            Log.d("NEXT", "masterFragment null");
        }
    }
}
