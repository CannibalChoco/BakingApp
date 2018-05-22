package com.example.user.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.adapter.MasterListAdapter;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.utils.BakingAppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class MasterListFragment extends Fragment implements MasterListAdapter.OnStepClickListener{

    public static final String TAG = MasterListFragment.class.getSimpleName();

    public MasterListFragment(){}

    private Recipe recipe;

    private OnStepClickListener stepClickListener;

    public interface OnStepClickListener{
        void onStepClicked(int position);
    }

    @BindView(R.id.master_list_recycler_view)
    RecyclerView masterListRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            stepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

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
        stepClickListener.onStepClicked(position);
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

}
