package com.example.user.bakingapp.ui;


import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bakingapp.MyApplication;
import com.example.user.bakingapp.R;
import com.example.user.bakingapp.adapter.IngredientAdapter;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.model.Step;
import com.example.user.bakingapp.receiver.ConnectivityReceiver;
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
public class DetailFragment extends Fragment implements IngredientAdapter.OnCheckedStateListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String KEY_PLAYER_POSITION = "player_position";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_CURRENT_WINDOW = "current_window";
    private static final String KEY_IS_CHECKED_ARRAY = "is_checked_array";

    private static final int ID_OF_INGREDIENTS = 0;
    private static final int ID_OF_INTRO = 1;
    private static final int COUNT_OF_NON_STEPS = 2;
    private static final int ADJUST_0_INDEX_FOR_USER_BY = 1;
    /*
        The amount to subtract from the list item Id for steps to start with 1
     */
    private static final int SUBTRACT_FROM_STEP_ID = COUNT_OF_NON_STEPS - ADJUST_0_INDEX_FOR_USER_BY;

    private boolean[] isCheckedArray;
    private boolean isWaitingForInternetConnection = false;
    private boolean hasInitializedVideo = false;

    private ConnectivityReceiver connectivityReceiver;

    public DetailFragment() {
    }

    public interface OnDetailStepClickListener {
        void onDetailStepClicked(int position);
    }

    private OnDetailStepClickListener switchStepListener;

    private List<Ingredient> ingredients;
    private Step step;
    private int stepId;
    private String videoUrl;
    private SimpleExoPlayer player;
    private long playerPosition;
    private boolean playWhenReady;
    private int currentWindow;

    private IngredientAdapter ingredientAdapter;

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

    @BindView(R.id.nav_current_step)
    TextView currentStepTextView;
    @BindView(R.id.nav_total_steps)
    TextView totalStepsTextView;
    @BindView(R.id.nav_icon_next_step)
    ImageView nextStepImageView;
    @BindView(R.id.nav_icon_prev_step)
    ImageView prevStepImageView;
    @BindView(R.id.nav_next_step_text)
    TextView nextStepTextView;
    @BindView(R.id.nav_prev_step_text)
    TextView prevStepTextView;
    @BindView(R.id.label_intro)
    TextView labelIntro;

    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.thumbnail_view)
    ImageView thumbnailView;
    @BindView(R.id.detail_constraint_layout)
    ConstraintLayout detailConstraintLayout;
    @BindView(R.id.player_empty_state_text)
    TextView playerEmptyStateText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            switchStepListener = (OnDetailStepClickListener) context;
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

            if (savedInstanceState.containsKey(KEY_IS_CHECKED_ARRAY)) {
                isCheckedArray = savedInstanceState.getBooleanArray(KEY_IS_CHECKED_ARRAY);
            }
        } else {
            playerPosition = C.TIME_UNSET;
            playWhenReady = true;
            currentWindow = 0;
        }

        Bundle args = getArguments();
        if (args != null) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.setTitle(args.getString(BakingAppConstants.KEY_RECIPE_NAME));
            }

            // User selected ingredients
            if (args.containsKey(BakingAppConstants.KEY_INGREDIENT_LIST)) {

                stepsView.setVisibility(View.INVISIBLE);
                // get ingredients from args
                ingredients = args.getParcelableArrayList(BakingAppConstants.KEY_INGREDIENT_LIST);
                stUpIngredientsView(args.getInt(BakingAppConstants.KEY_RECIPE_SERVINGS));
                if (isCheckedArray == null) {
                    isCheckedArray = new boolean[ingredients.size()];
                }

                // user selected step
            } else if (args.containsKey(BakingAppConstants.KEY_STEP)) {
                ingredientsView.setVisibility(View.GONE);

                step = args.getParcelable(BakingAppConstants.KEY_STEP);

                String stepDescriptionString = step.getDescription() != null ? step.getDescription():
                        getString(R.string.no_step_description);
                stepDescription.setText(stepDescriptionString);

                videoUrl = step.getVideoURL();

                if (videoUrl == null || videoUrl.isEmpty()) {
                    playerView.setVisibility(View.GONE);
                    playerEmptyStateText.setVisibility(View.GONE);

                    String thumbnailUrl = step.getThumbnailURL();
                    if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                        Picasso.with(getContext()).load(thumbnailUrl).into(thumbnailView);
                    }
                } else {
                    thumbnailView.setVisibility(View.GONE);
                }
            }

            // the id of the stepsList item; includes ingredient and intro
            stepId = args.getInt(BakingAppConstants.KEY_STEP_ID);

            // the actual step count
            int actualStepCount = args.getInt(BakingAppConstants.KEY_STEP_COUNT);
            /*
              The actualStepCount is stepsList.size() - ingredientItem - introItem;
              which results in x - 1 - 1;
              The count starts with "0", but we want to show it as "1" for users
              So, instead of x - 1 - 1 + 1, just do x - 1
            */
            int currentStep = stepId - SUBTRACT_FROM_STEP_ID;

            if (stepId == ID_OF_INGREDIENTS) {
                // id == 0 -> ingredients
                hideBottomNavStepsInfoForIngredients();
                labelIntro.setVisibility(View.GONE);
            } else if (stepId > ID_OF_INTRO){
                // step displayed
                currentStepTextView.setText(String.valueOf(currentStep));
                String totalSteps = "/" + (actualStepCount);
                totalStepsTextView.setText(totalSteps);
                labelIntro.setVisibility(View.GONE);
            }

            // hide "next" button for last recipe detail item(which is also the last step)
            if (currentStep == actualStepCount) {
                hideNextInBottomNavForLastStep();
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

        // connectivity receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityReceiver = new ConnectivityReceiver();
            getActivity().registerReceiver(connectivityReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        /*
         * register connection status listener
         * as shown in the androidhive tutorial disclosed in ConnectivityReceiver.java
         */
        MyApplication.getInstance().setConnectivityListener(this);

        if (Util.SDK_INT > 23 && videoUrl != null) {
            initPlayerIfConnected();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null) && videoUrl != null) {
            initPlayerIfConnected();
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

        // unregister connectivity receiver
        if (connectivityReceiver != null) {
            getActivity().unregisterReceiver(connectivityReceiver);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (player != null) {
            outState.putLong(KEY_PLAYER_POSITION, player.getCurrentPosition());
            outState.putInt(KEY_CURRENT_WINDOW, player.getCurrentWindowIndex());
            outState.putBoolean(KEY_PLAY_WHEN_READY, player.getPlayWhenReady());
        }

        if (ingredientsView.getVisibility() == View.VISIBLE) {
            outState.putBooleanArray(KEY_IS_CHECKED_ARRAY, isCheckedArray);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCheckBoxStateChanged(int position, boolean isChecked) {
        isCheckedArray[position] = isChecked;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (isWaitingForInternetConnection) {
                // get back connection
                if (hasInitializedVideo) {
                    if (playerEmptyStateText.getVisibility() == View.VISIBLE){
                        playerEmptyStateText.setVisibility(View.GONE);
                    }
                    // restart play from where it was left
                    playerPosition = player.getCurrentPosition();
                    playWhenReady = player.getPlayWhenReady();
                    currentWindow = player.getCurrentWindowIndex();

                    initPlayer();
                } else {
                    // play from start
                    initPlayer();
                }
                isWaitingForInternetConnection = false;
            }
        } else {
            // no connection
            playerEmptyStateText.setVisibility(View.VISIBLE);

            if (!isWaitingForInternetConnection) {
                isWaitingForInternetConnection = true;
            }

        }
    }

    /**
     * Call initPlayer if connected to network, otherwise show message
     */
    private void initPlayerIfConnected() {
        if(ConnectivityReceiver.isConnected()){
            initPlayer();
            isWaitingForInternetConnection = false;
        } else {
            isWaitingForInternetConnection = true;
            playerEmptyStateText.setVisibility(View.VISIBLE);
            playerEmptyStateText.setText(R.string.waiting_for_connectivity_msg);
        }
    }

    /**
     * Initialize ExopPlayer for playback
     */
    @SuppressWarnings("SpellCheckingInspection")
    private void initPlayer() {
        playerEmptyStateText.setVisibility(View.GONE);

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
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

        hasInitializedVideo = true;
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @OnClick({R.id.nav_next_step_text, R.id.nav_icon_next_step})
    public void onNextStepClicked() {
        int nextStep = stepId + 1;
        switchStepListener.onDetailStepClicked(nextStep);
    }

    @OnClick({R.id.nav_prev_step_text, R.id.nav_icon_prev_step})
    public void onPrevStepClicked() {
        int prevStep = stepId - 1;
        switchStepListener.onDetailStepClicked(prevStep);
    }


    /**
     * Set up the Ingredient RecyclerView
     *
     * @param servings number of servings
     */
    private void stUpIngredientsView(int servings) {
        ingredientAdapter = new IngredientAdapter(ingredients, this, isCheckedArray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewIngredients.setLayoutManager(layoutManager);
        recyclerViewIngredients.setAdapter(ingredientAdapter);
        recyclerViewIngredients.setHasFixedSize(true);

        this.servings.setText(String.valueOf(servings));
    }

    /**
     * When displaying last step, hide the text and button for going to next step
     */
    private void hideNextInBottomNavForLastStep() {
        nextStepImageView.setVisibility(View.GONE);
        nextStepTextView.setVisibility(View.GONE);
    }

    /**
     * When displaying the list of ingredients, hide previous button and text, current and total
     * steps text
     */
    private void hideBottomNavStepsInfoForIngredients() {
        currentStepTextView.setVisibility(View.GONE);
        totalStepsTextView.setVisibility(View.GONE);
        prevStepImageView.setVisibility(View.GONE);
        prevStepTextView.setVisibility(View.GONE);
    }

}
