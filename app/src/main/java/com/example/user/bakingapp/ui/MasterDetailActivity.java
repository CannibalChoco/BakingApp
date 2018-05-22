package com.example.user.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;

import java.util.ArrayList;

// TODO: fix up button for handset layout
public class MasterDetailActivity extends AppCompatActivity implements
        MasterListFragment.OnStepClickListener,
        DetailFragment.OnSwitchStepClickListener {
    // TODO: 1. Handle master-detail flow

    private boolean isTwoPane;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        Bundle bundle = getIntent().getBundleExtra(BakingAppConstants.KEY_RECIPE_BUNDLE);
        recipe = bundle.getParcelable(BakingAppConstants.KEY_RECIPE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MasterListFragment masterListFragment = new MasterListFragment();
        masterListFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.master_list_container, masterListFragment)
                .commit();


        if (findViewById(R.id.detail_container) != null) {
            Log.d("DETAIL", "MasterDetailActivity detail_container != null");
            isTwoPane = true;
            // populate detail view
            if (savedInstanceState == null) {
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(getArgsForDetailFragment
                        (BakingAppConstants.DEFAULT_FRAGMENT_DETAIL_ITEM));
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.detail_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
            // TODO: hide buttons?
        } else {
            isTwoPane = false;
        }
        Log.d("DETAIL", "MasterDetailActivity onCreate isTwoPane = " + isTwoPane);
    }

    /**
     * Callback triggered from MasterListFragment when list item is clicked
     *
     * @param position position of the item in the list
     */
    @Override
    public void onStepClicked(int position) {
        Log.d("DETAIL", "onStepClicked MasterDetailActivity isTwoPane = " + isTwoPane);
        // TODO: make sure logic is correct
        if (isTwoPane) {
            startDetailFragment(position);
        } else {
            startDetailActivity(position);
        }
    }

    /**
     * Callbacks triggered from DetailFragment when "previous" or "next" button is clicked
     *
     * @param position the position of the selected step
     */
    @Override
    public void onStepSelected(int position) {
        // TODO: make sure logic is correct
        Log.d("DETAIL", "onStepSelected MasterDetailActivity isTwoPane = " + isTwoPane);
        if (isTwoPane) {
            getSupportFragmentManager().popBackStack();
            startDetailFragment(position);
        }
    }

    private void startDetailFragment(int position) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(MasterDetailActivity.this,
                DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingAppConstants.KEY_RECIPE, recipe);
        bundle.putInt(BakingAppConstants.KEY_STEP_ID, position);

        intent.putExtra(BakingAppConstants.KEY_RECIPE_BUNDLE, bundle);
        startActivity(intent);
    }

    /**
     * Create a new Bundle to hold the users selected information
     *
     * @param position item clicked; position 0 contains ingredient list, the rest- recipe steps
     * @return bundle for the DetailFragment
     */
    @NonNull
    private Bundle getArgsForDetailFragment(int position) {
        Bundle bundle = new Bundle();
        if (position == 0) {
            // ingredients clicked- send ingredients
            bundle.putParcelableArrayList(BakingAppConstants.KEY_INGREDIENT_LIST,
                    (ArrayList) recipe.getIngredients());
            bundle.putInt(BakingAppConstants.KEY_RECIPE_SERVINGS, recipe.getServings());
        } else {
            // step clicked - send step
            bundle.putParcelable(BakingAppConstants.KEY_STEP, recipe.getSteps().get(position));
        }

        bundle.putString(BakingAppConstants.KEY_RECIPE_NAME, recipe.getName());
        bundle.putInt(BakingAppConstants.KEY_STEP_ID, position);
        int itemCount = recipe.getSteps().size() + 1;
        bundle.putInt(BakingAppConstants.KEY_DETAIL_ITEM_COUNT, itemCount);

        return bundle;
    }
}
