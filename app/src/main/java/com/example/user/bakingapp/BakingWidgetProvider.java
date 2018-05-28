package com.example.user.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.service.ListWidgetService;
import com.example.user.bakingapp.ui.RecipeListActivity;
import com.example.user.bakingapp.utils.BakingAppConstants;
import com.example.user.bakingapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    // TODO: get displayed recipes name to launch the recipes activity
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        String name = SharedPreferencesUtils.getRecipeNameFromPreferences(context);
        // set recipe name
        views.setTextViewText(R.id.widget_recipe_name, name);

        // Create Intent for launching the app
        Intent launchAppIntent = new Intent(context, RecipeListActivity.class);

        PendingIntent launchAppPendingIntent = PendingIntent.getActivity(context, 0,
                launchAppIntent, 0);
        // set click pending intent template for remote views
        views.setOnClickPendingIntent(R.id.widget_recipe_name, launchAppPendingIntent);

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

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//
//        if (intent.getAction() == AppWidgetManager.ACTION_APPWIDGET_UPDATE){
//            int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//            if(intent.hasExtra(BakingAppConstants.KEY_RECIPE_BUNDLE)){
//                Log.d("WIDGET", "widget provider intent has recipe bundle");
//                Bundle bundle = intent.getBundleExtra(BakingAppConstants.KEY_RECIPE_BUNDLE);
//                if(ingredients != null && !ingredients.isEmpty()){
//                    ingredients.clear();
//                }
//                ingredients = bundle.getParcelableArrayList(BakingAppConstants.KEY_INGREDIENT_LIST);
//
//                if (ids != null && ids.length > 0){
//                    this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
//                }
//
//            }
//        }
//
//    }
}

