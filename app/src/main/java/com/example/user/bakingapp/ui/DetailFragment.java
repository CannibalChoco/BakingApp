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
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
    private static final String KEY_PLAYER_POSITION = "player_position";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_CURRENT_WINDOW = "current_window";

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
    private long playerPosition;
    private boolean playWhenReady;
    private int currentWindow;

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


        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(KEY_PLAYER_POSITION)) {
                playerPosition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
            } else {
                playerPosition = C.TIME_UNSET;
            }

            if (savedInstanceState.containsKey(KEY_PLAY_WHEN_READY)) {
                playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            } else {
                playWhenReady = true;
            }

            if (savedInstanceState.containsKey(KEY_CURRENT_WINDOW)) {
                currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            } else {
                currentWindow = 0;
            }
        } else {
            playerPosition = C.TIME_UNSET;
            playWhenReady = true;
            currentWindow = 0;
        }

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
            int stepCount = args.getInt(BakingAppConstants.KEY_STEP_COUNT);

            if (stepId == 0) {
                buttonPrev.setVisibility(View.GONE);
            } else if (stepId > 0 && stepId == stepCount - 1) {
                buttonNext.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    /**
     * Starting with API level 24 Android supports multiple windows. As our app can be visible but
     * not active in split window mode, we need to initialize the player in onStart. Before API
     * level 24 we wait as long as possible until we grab resources, so we wait until onResume
     * before initializing the player.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && videoUrl != null) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null) && videoUrl != null) {
            initPlayer();
        }
    }

    /**
     * Before API Level 24 there is no guarantee of onStop being called. So we have to release
     * the player as early as possible in onPause. Starting with API Level 24 (which brought
     * multi and split window mode) onStop is guaranteed to be called and in the paused mode
     * our activity is eventually still visible. Hence we need to wait releasing until onStop.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (player != null){
            outState.putLong(KEY_PLAYER_POSITION, player.getCurrentPosition());
            outState.putInt(KEY_CURRENT_WINDOW, player.getCurrentWindowIndex());
            outState.putBoolean(KEY_PLAY_WHEN_READY, player.getPlayWhenReady());
        }

        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        //player = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
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

        player.seekTo(currentWindow, playerPosition);

        player.prepare(videoSource, false, false);
        player.setPlayWhenReady(playWhenReady);
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
