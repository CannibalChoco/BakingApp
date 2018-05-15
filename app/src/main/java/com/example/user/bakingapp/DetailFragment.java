package com.example.user.bakingapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.adapter.IngredientAdapter;
import com.example.user.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailFragment extends Fragment {

    public DetailFragment(){}

    private Recipe recipe;

    @BindView(R.id.ingredients_recycler_view)
    RecyclerView recyclerViewIngredients;
    @BindView(R.id.servings)
    TextView servings;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("RECIPE", "DetailFragment");
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if (args != null) {

            recipe = getArguments().getParcelable(MainActivity.KEY_RECIPE);

            Log.d("RECIPE", recipe.toString());

            getActivity().setTitle(recipe.getName());

            IngredientAdapter ingredientAdapter = new IngredientAdapter(recipe.getIngredients());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewIngredients.setLayoutManager(layoutManager);
            recyclerViewIngredients.setAdapter(ingredientAdapter);
            recyclerViewIngredients.setHasFixedSize(true);

            servings.setText(String.valueOf(recipe.getServings()));
        }

        return rootView;
    }
}
