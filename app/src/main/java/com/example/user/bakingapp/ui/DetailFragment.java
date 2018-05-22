package com.example.user.bakingapp.ui;


import android.content.Context;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.adapter.IngredientAdapter;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Step;
import com.example.user.bakingapp.utils.BakingAppConstants;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("WeakerAccess")
public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    public DetailFragment() {
    }

    public interface OnSwitchStepClickListener {
        void onStepSelected(int position);
    }

    private OnSwitchStepClickListener switchStepListener;

    private List<Ingredient> ingredients;
    private Step step;
    private int stepId;
    private String videoUrl;
    private SimpleExoPlayer player;

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
    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.thumbnail_view)
    ImageView thumbnailView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            switchStepListener = (OnSwitchStepClickListener) context;
        } catch (ClassCastException e) {
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
            getActivity().setTitle(args.getString(BakingAppConstants.KEY_RECIPE_NAME));
            // User selected ingredients
            if (args.containsKey(BakingAppConstants.KEY_INGREDIENT_LIST)) {
                stepsView.setVisibility(View.INVISIBLE);
                // get ingredients from args
                ingredients = args.getParcelableArrayList(BakingAppConstants.KEY_INGREDIENT_LIST);
                stUpIngredientsView(args.getInt(BakingAppConstants.KEY_RECIPE_SERVINGS));

                // user selected step
            } else if (args.containsKey(BakingAppConstants.KEY_STEP)) {
                ingredientsView.setVisibility(View.GONE);

                step = args.getParcelable(BakingAppConstants.KEY_STEP);
                stepDescription.setText(step.getDescription());

                videoUrl = step.getVideoURL();
                boolean hasVideo = false;

                if (videoUrl == null || videoUrl.isEmpty()) {
                    playerView.setVisibility(View.GONE);

                    String thumbnailUrl = step.getThumbnailURL();
                    if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                        Picasso.with(getContext()).load(thumbnailUrl).into(thumbnailView);
                    }
                } else {
                    thumbnailView.setVisibility(View.GONE);
                }
            }

            stepId = args.getInt(BakingAppConstants.KEY_STEP_ID);
            int itemCount = args.getInt(BakingAppConstants.KEY_DETAIL_ITEM_COUNT);

            // TODO: fix bug - hide "next" button on last step
            if (stepId == 0) {
                buttonPrev.setVisibility(View.GONE);
            } else if (stepId > 0 && stepId == itemCount) {
                buttonNext.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (videoUrl != null) {
            initPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (player != null) {
            playerView.setPlayer(null);
            player.release();
            player = null;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
        playerView.setPlayer(player);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        // tell the exoplayer what to play
        @SuppressWarnings("SpellCheckingInspection")
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getContext(),
                Util.getUserAgent(getContext(), "bakingapp"),
                bandwidthMeter);

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(step.getVideoURL()));

        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }

    @OnClick(R.id.button_next)
    public void onNextStepClicked() {
        int nextStep = stepId + 1;
        switchStepListener.onStepSelected(nextStep);
    }

    @OnClick(R.id.button_prev)
    public void onPrevStepClicked() {
        int prevStep = stepId - 1;
        switchStepListener.onStepSelected(prevStep);
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
