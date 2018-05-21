package com.example.user.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;

public class MasterDetailActivity extends AppCompatActivity {
    // TODO: 1. Handle master-detail flow

    private boolean istTwoPane;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        Bundle bundle = getIntent().getBundleExtra(BakingAppConstants.KEY_RECIPE_BUNDLE);
        recipe = bundle.getParcelable(BakingAppConstants.KEY_RECIPE);

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
}
