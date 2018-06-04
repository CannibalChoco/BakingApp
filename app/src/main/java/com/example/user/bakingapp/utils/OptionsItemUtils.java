package com.example.user.bakingapp.utils;

import android.app.Application;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Recipe;

public class OptionsItemUtils {

    /**
     * Remove recipe from SharedPreferences, call updateWidget(), and notify user of action
     * @param item menu item that was clicked
     */
    public static void removeFromWidget(Context context, Application application, MenuItem item) {
        SharedPreferencesUtils.removeFromPreferences(context);
        WidgetUtils.updateWidget(application);
        item.setIcon(R.drawable.ic_action_show_in_widget_default);

        Toast.makeText(context, R.string.msg_removed_from_widget, Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Save recipe in SharedPreferences, call updateWidget() and notify user of action
     * @param item menu item that was clicked
     */
    public static void pinToWidget(Context context, Application application, MenuItem item,
                                   Recipe recipe) {
        SharedPreferencesUtils.saveRecipeInPreferences(context, recipe);
        WidgetUtils.updateWidget(application);
        item.setIcon(R.drawable.ic_action_show_in_widget_enabled);

        Toast.makeText(context, R.string.msg_pinned_to_widget, Toast.LENGTH_SHORT)
                .show();
    }

}
