package com.example.user.bakingapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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
        // set recipe name
        views.setTextViewText(R.id.widget_recipe_name, name);

        // Create Intent for launching the app
        Intent launchActivityIntent = new Intent(context, MasterDetailActivity.class);
        launchActivityIntent.putExtra(MasterDetailActivity.KEY_GET_RECIPE_FROM_SHARED_PREFS,
                MasterDetailActivity.GET_RECIPE_FROM_SHARED_PREFS);

        // go to RecipeListActivity when click back or up
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(RecipeListActivity.class);
        stackBuilder.addNextIntentWithParentStack(launchActivityIntent);
        PendingIntent backStackPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // set click pending intent for recipe name
        views.setOnClickPendingIntent(R.id.widget_recipe_name, backStackPendingIntent);
        // Set list item click to launch recipe
        views.setPendingIntentTemplate(R.id.widget_list_view, backStackPendingIntent);

        // Set the ListWidgetService to act as the adapter for the ListView
        Intent widgetServiceIntent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, widgetServiceIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

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

