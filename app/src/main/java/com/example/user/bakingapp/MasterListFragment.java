package com.example.user.bakingapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingapp.adapter.MasterListAdapter;
import com.example.user.bakingapp.adapter.RecipeAdapter;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// TODO: Display ingredients and steps
public class MasterListFragment extends Fragment implements MasterListAdapter.OnStepClickListener{

    public MasterListFragment(){}

    private Recipe recipe;


    @BindView(R.id.master_list_recycler_view) RecyclerView masterListRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("RECIPE", "MasterListFragment");
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if(bundle != null){
            Log.d("RECIPE", "bundle != null");
            recipe = getArguments().getParcelable(MainActivity.KEY_RECIPE);
            Log.d("RECIPE", recipe.toString());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            MasterListAdapter adapter = new MasterListAdapter(recipe.getSteps(), this);
            masterListRecyclerView.setLayoutManager(layoutManager);
            masterListRecyclerView.setAdapter(adapter);
            masterListRecyclerView.setHasFixedSize(true);
        }

        return rootView;
    }

    @Override
    public void onStepSelected(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.KEY_RECIPE, recipe);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
