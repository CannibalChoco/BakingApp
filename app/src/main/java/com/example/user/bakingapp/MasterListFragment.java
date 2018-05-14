package com.example.user.bakingapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener{

    @BindView(R.id.master_list_recycler_view) RecyclerView recyclerView;

    private List<Recipe> recipes;

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if (args != null) {
            recipes = getArguments().getParcelableArrayList(MainActivity.KEY_RECIPE_LIST);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            RecipeAdapter adapter = new RecipeAdapter(recipes, this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }

        return rootView;
    }

    @Override
    public void onRecipeSelected(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.KEY_RECIPE, recipes.get(position));

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
