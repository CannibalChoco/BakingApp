package com.example.user.bakingapp.ui;

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

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.adapter.MasterListAdapter;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment implements MasterListAdapter.OnStepClickListener{

    public static final String TAG = MasterListFragment.class.getSimpleName();

    public MasterListFragment(){}

    private Recipe recipe;
    private MasterListAdapter adapter;

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
            recipe = getArguments().getParcelable(BakingAppConstants.KEY_RECIPE);
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
        Log.d("MASTER", "setUpMasterListView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new MasterListAdapter(recipe.getSteps(), this);
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
        fragmentTransaction.replace(R.id.fragment_container, detailFragment, DetailFragment.TAG);
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
            bundle.putParcelableArrayList(BakingAppConstants.KEY_INGREDIENT_LIST,
                    (ArrayList) recipe.getIngredients());
        } else {
            // step clicked - send step
            bundle.putParcelable(BakingAppConstants.KEY_STEP, recipe.getSteps().get(position));
        }

        bundle.putString(BakingAppConstants.KEY_RECIPE_NAME, recipe.getName());
        bundle.putInt(BakingAppConstants.KEY_RECIPE_SERVINGS, recipe.getServings());
        bundle.putInt(BakingAppConstants.KEY_STEP_ID, position);
        bundle.putInt(BakingAppConstants.KEY_ADAPTER_SIZE, adapter.getItemCount());

        return bundle;
    }

    public void launchNextStep(int nextStep){
        getFragmentManager().popBackStack();
        if (nextStep < adapter.getItemCount()){
            launchDetailFragment(getArgsForDetailFragment(nextStep));
        }
    }
}
