package com.example.user.bakingapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;

import com.example.user.bakingapp.service.ListWidgetService;
import com.example.user.bakingapp.ui.MasterDetailActivity;
import com.example.user.bakingapp.ui.RecipeListActivity;
import com.example.user.bakingapp.utils.SharedPreferencesUtils;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        String name = SharedPreferencesUtils.getRecipeNameFromPreferences(context);
        if(name != null && !name.isEmpty()){
            // set empty state text  view gone
            views.setViewVisibility(R.id.widget_empty_state_text, View.GONE);

            // set list view visible
            views.setViewVisibility(R.id.widget_list_view, View.VISIBLE);

            // set recipe name
            views.setTextViewText(R.id.widget_recipe_name, name);

            // go to RecipeListActivity when click back or up from MasterDetailActivity
            PendingIntent backStackPendingIntent =
                    getPendingIntentWithBackStack(
                            context,
                            getIntentForSelectedRecipe(context));

            // set click pending intent for recipe name
            views.setOnClickPendingIntent(R.id.widget_recipe_name, backStackPendingIntent);
            // Set list item click to launch recipe
            views.setPendingIntentTemplate(R.id.widget_list_view, backStackPendingIntent);

            // Set the ListWidgetService to act as the adapter for the ListView
            Intent widgetServiceIntent = new Intent(context, ListWidgetService.class);
            views.setRemoteAdapter(R.id.widget_list_view, widgetServiceIntent);
        } else {
            // set list view gone
            views.setViewVisibility(R.id.widget_list_view, View.GONE);

            // set empty state text  view visible
            views.setViewVisibility(R.id.widget_empty_state_text, View.VISIBLE);

            // set app name on widget
            views.setTextViewText(R.id.widget_recipe_name, context.getString(R.string.app_name));

            // launch recipe list view on click
            Intent recipeListActivityIntent = new Intent(context, RecipeListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipeListActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.widget_empty_state_text, pendingIntent);
            views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Construct a pending intent for launching activity on click; add back stack for proper
     * navigation
     * @param context app context
     * @param launchActivityIntent intent for activity to be launched
     * @return pending intent with back stack
     */
    private static PendingIntent getPendingIntentWithBackStack(Context context, Intent launchActivityIntent) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(RecipeListActivity.class);
        stackBuilder.addNextIntentWithParentStack(launchActivityIntent);
        return stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Construct the intent needed for launching MasterDetailActivity on recipe name click,
     * add the recipe in shared prefs as extra
     *
     * @param context app context
     * @return intent for launching activity
     */
    @NonNull
    private static Intent getIntentForSelectedRecipe(Context context) {
        Intent launchActivityIntent = new Intent(context, MasterDetailActivity.class);
        launchActivityIntent.putExtra(MasterDetailActivity.KEY_GET_RECIPE_FROM_SHARED_PREFS,
                MasterDetailActivity.GET_RECIPE_FROM_SHARED_PREFS);
        return launchActivityIntent;
    }

    /**
     * Go through all app widgets and update  the m
     * @param context app context
     * @param appWidgetManager
     * @param appWidgetIds array oi int ids of all the widgets on home screen
     */
    public static void updateAppWidgets (Context context, AppWidgetManager appWidgetManager,
                                         int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // called when the widget is first added, and in intervals defined in provider_info.xml
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        /*
        This is called when an instance the App Widget is created for the first time. For example,
        if the user adds two instances of your App Widget, this is only called the first time. If
        you need to open a new database or perform other setup that only needs to occur once for all
         App Widget instances, then this is a good place to do it.
         */
    }

    @Override
    public void onDisabled(Context context) {
        /*
        This is called when the last instance of your App Widget is deleted from the App Widget
        host. This is where you should clean up any work done in onEnabled(Context), such as delete
        a temporary database.
         */
    }
}

