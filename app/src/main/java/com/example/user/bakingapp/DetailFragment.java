package com.example.user.bakingapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingapp.model.Recipe;

import butterknife.ButterKnife;


public class DetailFragment extends Fragment {

    public DetailFragment(){}

    private Recipe recipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(rootView);


        Bundle args = getArguments();
        if (args != null) {

            recipe = getArguments().getParcelable(MainActivity.KEY_RECIPE);

            Log.d("RECIPE", recipe.toString());
        }

        return rootView;
    }
}
