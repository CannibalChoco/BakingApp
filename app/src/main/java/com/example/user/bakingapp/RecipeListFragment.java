package com.example.user.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingapp.adapter.RecipeAdapter;
import com.example.user.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener{

    public static final String TAG = RecipeListFragment.class.getSimpleName();

    @BindView(R.id.recipe_list_recycler_view)
    RecyclerView recyclerView;

    private List<Recipe> recipes;

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("RECIPE", "RecipeListFragment");
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.app_name);

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

        MasterListFragment masterListFragment = new MasterListFragment();
        masterListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, masterListFragment, MasterListFragment.TAG);
        fragmentTransaction.addToBackStack(MasterListFragment.TAG);
        Log.d("NEXT", "onRecipeSelected - add to back stack");
        fragmentTransaction.commit();
    }
}
