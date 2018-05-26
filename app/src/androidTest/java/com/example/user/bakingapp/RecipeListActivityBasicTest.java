package com.example.user.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.user.bakingapp.ui.MasterListFragment;
import com.example.user.bakingapp.ui.RecipeListActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityBasicTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> recipeListActivityActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource(){
        idlingResource = recipeListActivityActivityTestRule.getActivity().getIdlingResource();
        // TODO: register idling resource
    }

    @Test
    public void recipeListRecyclerViewIsDisplayed(){
        onView(withId(R.id.recipe_list_recycler_view)).check(matches(isDisplayed())); // passes
    }

    // TODO: register idling resource before this test
    @Test
    public void clickRecipeStartsMasterListActivity(){
        // find the view
        // perform action on the view
        //onData(anything()).inAdapterView(withId(R.id.recipe_list_recycler_view)).atPosition(0).perform(click());

        // check if the view does what you expected
        //onView(withId(R.id.master_list_container)).check(matches(isDisplayed()));
    }
}
