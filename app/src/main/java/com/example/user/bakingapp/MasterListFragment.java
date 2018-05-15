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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment implements MasterListAdapter.OnStepClickListener{

    public MasterListFragment(){}

    private Recipe recipe;

    @BindView(R.id.master_list_recycler_view)
    RecyclerView masterListRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);


        Bundle bundle = getArguments();
        if(bundle != null){
            recipe = getArguments().getParcelable(MainActivity.KEY_RECIPE);
            setUpMasterListView();

            getActivity().setTitle(recipe.getName());
        }

        return rootView;
    }

    @Override
    public void onStepSelected(int position) {
        launchDetailFragment(getArgsForDetailFragment(position));
    }

    /**
     * Set up everything needed for the master list to display list items
     */
    private void setUpMasterListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        MasterListAdapter adapter = new MasterListAdapter(recipe.getSteps(), this);
        masterListRecyclerView.setLayoutManager(layoutManager);
        masterListRecyclerView.setAdapter(adapter);
        masterListRecyclerView.setHasFixedSize(true);
    }

    /**
     * Create new DetailFragment, add bundle of arguments, start fragment transaction
     * @param bundle arguments to be sent to the fragment
     */
    private void launchDetailFragment(Bundle bundle) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
            bundle.putParcelableArrayList(MainActivity.KEY_INGREDIENT_LIST, (ArrayList) recipe.getIngredients());
        } else {
            // step clicked - send step
            bundle.putParcelable(MainActivity.KEY_STEP, recipe.getSteps().get(position));
        }

        bundle.putString(MainActivity.KEY_RECIPE_NAME, recipe.getName());
        bundle.putInt(MainActivity.KEY_RECIPE_SERVINGS, recipe.getServings());
        return bundle;
    }
}
