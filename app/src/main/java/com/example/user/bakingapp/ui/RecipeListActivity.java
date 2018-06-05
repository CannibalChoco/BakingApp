package com.example.user.bakingapp.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.user.bakingapp.MyApplication;
import com.example.user.bakingapp.R;
import com.example.user.bakingapp.RecipeClient;
import com.example.user.bakingapp.adapter.RecipeAdapter;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.receiver.ConnectivityReceiver;
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
        RecipeAdapter.OnRecipeClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private List<Recipe> recipeList;

    @BindView(R.id.recipe_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recipe_list_empty_state_text)
    TextView recipeEmptyStateText;
    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar recipeListProgressBar;

    @Nullable
    private SimpleIdlingResource idlingResource;

    //private ConnectivityReceiver connectivityReceiver;

    private boolean isWaitingForInternetConnection;
    private boolean hasLoadedRecipes = false;

    private ConnectivityReceiver connectivityReceiver;

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

        getIdlingResource();

        getRecipesIfConnected();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // connectivity receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityReceiver = new ConnectivityReceiver();
            this.registerReceiver(connectivityReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        /*
         * register connection status listener
         * as shown in the androidhive tutorial disclosed in ConnectivityReceiver.java
         */
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister connectivity receiver
        if (connectivityReceiver != null) {
            this.unregisterReceiver(connectivityReceiver);
        }
    }

    private void getRecipesIfConnected(){
        if (ConnectivityReceiver.isConnected()){
            getRecipes();
        } else {
            hasLoadedRecipes = false;
            showEmptyStateNoConnection();
        }
    }

    /**
     * gets recipe list from server asynchronously
     */
    private void getRecipes() {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        showLoading();

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

                hasLoadedRecipes = true;

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

                showRecipes();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                hasLoadedRecipes = false;
                showEmptyStateError();
                recipeEmptyStateText.setText(R.string.recipe_list_empty_error);
            }
        });
    }


    private void setUpUi() {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
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

        startActivity(intent);
    }

    private void showLoading(){
        recipeListProgressBar.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.INVISIBLE);
        recipeEmptyStateText.setVisibility(View.INVISIBLE);
    }

    private void showRecipes(){
        recyclerView.setVisibility(View.VISIBLE);

        recipeListProgressBar.setVisibility(View.INVISIBLE);
        recipeEmptyStateText.setVisibility(View.INVISIBLE);
    }

    private void showEmptyStateNoConnection(){
        recipeEmptyStateText.setVisibility(View.VISIBLE);
        recipeEmptyStateText.setText(R.string.recipe_list_empty_no_connection);

        recipeListProgressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyStateError(){
        recipeEmptyStateText.setVisibility(View.VISIBLE);
        recipeEmptyStateText.setText(R.string.recipe_list_empty_error);

        recipeListProgressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (isWaitingForInternetConnection) {
                if (!hasLoadedRecipes){
                    getRecipes();
                }
                isWaitingForInternetConnection = false;
            }
        } else {
            if (!isWaitingForInternetConnection) {
                Toast.makeText(this, R.string.connectivity_lost_msg, Toast.LENGTH_SHORT).show();
                isWaitingForInternetConnection = true;
            }

        }
    }
}
