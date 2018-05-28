package com.example.user.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.bakingapp.BakingWidgetProvider;
import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;
import com.example.user.bakingapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;

// TODO: up navigation crashes app
public class DetailActivity extends AppCompatActivity implements
        DetailFragment.OnDetailStepClickListener {

    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getBundleExtra(BakingAppConstants.KEY_RECIPE_BUNDLE);
        recipe = bundle.getParcelable(BakingAppConstants.KEY_RECIPE);

        int stepId = bundle.getInt(BakingAppConstants.KEY_STEP_ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(getArgsForDetailFragment(stepId));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_container, detailFragment)
                    .commit();
        }
    }

    /**
     * Callbacks triggered from DetailFragment when "previous" or "next" button is clicked
     * @param position the position of the selected step
     */
    @Override
    public void onDetailStepClicked(int position) {
        startDetailFragment(position);
    }

    private void startDetailFragment(int position) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container, detailFragment)
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
