package com.example.user.bakingapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingapp.model.Recipe;

import java.util.List;

public class MasterListFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener{

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        List<Recipe> recipes;

        Bundle args = getArguments();
        if (args != null) {
            recipes = getArguments().getParcelableArrayList(MainActivity.KEY_RECIPE_LIST);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            RecipeAdapter adapter = new RecipeAdapter(getActivity(), recipes, this);
            final RecyclerView recyclerView = rootView.findViewById(R.id.master_list_recycler_view);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onRecipeSelected(int position) {
        Log.d("RECIPE", "fragment - recipe selected");
        MainActivity.startFragmentTransaction(position);
    }
}
