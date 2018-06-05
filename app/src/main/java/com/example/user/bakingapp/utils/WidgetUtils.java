package com.example.user.bakingapp.utils;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.receiver.BakingWidgetProvider;

public class WidgetUtils {

    /**
     * Get instance of AppWidgetManager and call WidgetProviders updateAppWidgets()
     * Notify remote adapter to update widget data
     */
    public static void updateWidget(Application application) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(application);
        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(application, BakingWidgetProvider.class));

        BakingWidgetProvider.updateAppWidgets(application, appWidgetManager, ids);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list_view);
    }

}
