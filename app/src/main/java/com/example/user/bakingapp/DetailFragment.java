package com.example.user.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.bakingapp.adapter.IngredientAdapter;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO: 1. Get Step or Ingredient instead of Recipe; ???
// TODO: 2. Switch between Ingredients and Steps details
// TODO: 3. Create Step Detail layout
// TODO: 4. display Step and Ingredient in separate layouts
// TODO: 5. Set the title accordingly
public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    public DetailFragment(){}

    public interface OnNextStepListener{
        void onNextStep(int position);
        void onPrevStep(int position);
//        void onStepSelected(int position);
    }

    private OnNextStepListener nextStepListener;

    private List<Ingredient> ingredients;
    private Step step;
    private int stepId;

    @BindView(R.id.ingredients_recycler_view)
    RecyclerView recyclerViewIngredients;
    @BindView(R.id.servings)
    TextView servings;
    @BindView(R.id.ingredients_view)
    ConstraintLayout ingredientsView;
    @BindView(R.id.steps_view)
    ConstraintLayout stepsView;
    @BindView(R.id.step_description)
    TextView stepDescription;
    @BindView(R.id.button_prev)
    Button buttonPrev;
    @BindView(R.id.button_next)
    Button buttonNext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            nextStepListener = (OnNextStepListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnNextStepListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if (args != null) {
            getActivity().setTitle(args.getString(MainActivity.KEY_RECIPE_NAME));
            // User selected ingredients
            if(args.containsKey(MainActivity.KEY_INGREDIENT_LIST)){
                stepsView.setVisibility(View.INVISIBLE);
                // get ingredients from args
                ingredients = args.getParcelableArrayList(MainActivity.KEY_INGREDIENT_LIST);
                stUpIngredientsView(args.getInt(MainActivity.KEY_RECIPE_SERVINGS));

                // user selected step
            } else if (args.containsKey(MainActivity.KEY_STEP)){
                ingredientsView.setVisibility(View.GONE);

                step = args.getParcelable(MainActivity.KEY_STEP);
                stepDescription.setText(step.getDescription());
            }

            stepId = args.getInt(MainActivity.KEY_STEP_ID);
            int adapterSize = args.getInt(MainActivity.KEY_ADAPTER_SIZE);

            if (stepId == 0){
                buttonPrev.setVisibility(View.GONE);
            } else if (stepId > 0 && stepId + 1 == adapterSize){
                buttonNext.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

//    @OnClick(R.id.button_next)
//    public void nextClicked(){
//        nextStepListener.onStepSelected(stepId++);
//    }
//
//    @OnClick(R.id.button_prev)
//    public void prevClicked(){
//        nextStepListener.onStepSelected(stepId--);
//    }


    @OnClick(R.id.button_next)
    public void onNextStepClicked(){
        Log.d("NEXT", "next step clicked");
        Log.d("NEXT", String.valueOf(stepId++));
        nextStepListener.onNextStep(stepId ++);
    }

    @OnClick(R.id.button_prev)
    public void onPrevStepClicked(){
        Log.d("NEXT", "next step clicked");
        Log.d("NEXT", String.valueOf(stepId--));
        nextStepListener.onPrevStep(stepId--);
    }

    /**
     * Set up everything needed for the Ingredient view
     *
     * @param servings number of servings
     */
    private void stUpIngredientsView(int servings) {
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewIngredients.setLayoutManager(layoutManager);
        recyclerViewIngredients.setAdapter(ingredientAdapter);
        recyclerViewIngredients.setHasFixedSize(true);

        this.servings.setText(String.valueOf(servings));
    }
}
