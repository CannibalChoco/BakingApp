package com.example.user.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.bakingapp.BakingWidgetProvider;
import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;
import com.example.user.bakingapp.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MasterDetailActivity extends AppCompatActivity implements
        MasterListFragment.OnMasterListStepClickListener,
        DetailFragment.OnDetailStepClickListener {



    private boolean isTwoPane;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        Bundle bundle = getIntent().getBundleExtra(BakingAppConstants.KEY_RECIPE_BUNDLE);
        recipe = bundle.getParcelable(BakingAppConstants.KEY_RECIPE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addMasterListFragment(bundle);

        if (findViewById(R.id.detail_container) != null) {
            isTwoPane = true;
            // populate detail view
            if (savedInstanceState == null) {
                addDetailFragment();
            }
            // TODO: hide buttons?
        } else {
            isTwoPane = false;
        }
    }

    /**
     * Creates a new MasterListFragment for MasterDetailActivity, sets arguments, doesn't add
     * fragment to back stack
     * @param bundle arguments that are passed to fragment
     */
    private void addMasterListFragment(Bundle bundle) {
        MasterListFragment masterListFragment = new MasterListFragment();
        masterListFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.master_list_container, masterListFragment)
                .commit();
    }

    /**
     * Callback triggered from MasterListFragment when list item is clicked
     *
     * @param position position of the item in the list
     */
    @Override
    public void onMasterListStepClicked(int position) {
        if (isTwoPane) {
            replaceDetailFragment(position);
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
    public void onDetailStepClicked(int position) {
        if (isTwoPane) {
            getSupportFragmentManager().popBackStack();
            replaceDetailFragment(position);
        }
    }

    /**
     * Adds a nes DetailFragment in master-detail view if the app is in two pane mode
     */
    private void addDetailFragment() {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment
                (BakingAppConstants.DEFAULT_FRAGMENT_DETAIL_ITEM));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Replaces the existing DetailFragment with a new one. Triggered when step is selected in
     * master list or in DetailFragment navigation
     * @param position position of the next item to display in fragment
     */
    private void replaceDetailFragment(int position) {
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
        int stepCount = recipe.getSteps().size();
        bundle.putInt(BakingAppConstants.KEY_STEP_COUNT, stepCount);

        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_pin_to_widget:
                SharedPreferencesUtils.saveListInPreferences(this, recipe.getIngredients());

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());
                int[] ids = appWidgetManager
                        .getAppWidgetIds(new ComponentName(getApplication(), BakingWidgetProvider.class));

                BakingWidgetProvider.updateAppWidgets(getApplication(), appWidgetManager, ids);
                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list_view);

                Toast.makeText(this, R.string.msg_pinned_toWidget, Toast.LENGTH_SHORT).show();

                return true;
        }

        super.onOptionsItemSelected(item);

        return false;
    }
}
