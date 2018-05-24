package com.example.user.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.user.bakingapp.ui.RecipeListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Create Intent for launching the app
        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        // set click pending intent for remote views
        views.setOnClickPendingIntent(R.id.widget_list_view, pendingIntent);

        // TODO 2: set up ListView

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // TODO 1: initial setup + updates on any further changes
    // called when the widget is first added, and in intervals defined in provider_info.xml
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

