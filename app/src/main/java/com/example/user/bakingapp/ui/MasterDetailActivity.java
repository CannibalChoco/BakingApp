package com.example.user.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;

import java.util.ArrayList;

// TODO: fix up button for handset layout
public class MasterDetailActivity extends AppCompatActivity implements
        MasterListFragment.OnStepClickListener,
        DetailFragment.OnSwitchStepClickListener {
    // TODO: 1. Handle master-detail flow

    private boolean istTwoPane;
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
                .add(R.id.fragment_container, masterListFragment)
                .commit();


//        if(findViewById(R.id.divider_line) != null){
//            istTwoPane = true;
//
//
//        }
    }

    /**
     * Callback triggered from MasterListFragment when list item is clicked
     * @param position position of the item in the list
     */
    @Override
    public void onStepClicked(int position) {
        startDetailFragment(position);
    }

    /**
     * Callbacks triggered from DetailFragment when "previous" or "next" button is clicked
     * @param position the position of the selected step
     */
    @Override
    public void onStepSelected(int position) {
        getSupportFragmentManager().popBackStack();
        startDetailFragment(position);
    }

    private void startDetailFragment(int position) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Create a new Bundle to hold the users selected information
     * @param position item clicked; position 0 contains ingredient list, the rest- recipe steps
     * @return bundle for the DetailFragment
     */
    @NonNull
    private Bundle getArgsForDetailFragment(int position) {
        Bundle bundle = new Bundle();
        if (position == 0){
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
