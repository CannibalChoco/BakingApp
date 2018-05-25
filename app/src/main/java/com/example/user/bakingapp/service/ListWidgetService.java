package com.example.user.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.utils.BakingAppConstants;
import com.example.user.bakingapp.utils.SharedPreferencesUtils;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WIDGET", "ListWidgetService- onGetViewFactory");
        return new ListRemoteViewsFactory(getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Ingredient> ingredients;
    private Context context;

    public ListRemoteViewsFactory(Context applicationContext) {
        Log.d("WIDGET", "ListRemoteViewsFactory- constructor");
        this.context = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.d("WIDGET", "ListRemoteViewsFactory- onCreate");
    }

    @Override
    public void onDataSetChanged() {
        // TODO: get new list
        Log.d("WIDGET", "ListRemoteViewsFactory- onDataSetChanged");
        ingredients = SharedPreferencesUtils.getIngredientListFromPreferences(context);
        if(ingredients != null && !ingredients.isEmpty()){
            Log.d("WIDGET", "ListRemoteViewsFactory- onDataSetChanged list not empty");
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    // Equivalent to adapters onBindViewHolder
    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredients.get(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        remoteViews.setTextViewText(R.id.list_item_text_view, ingredient.getIngredientFormatted());
        Log.d("WIDGET", "widget service getViewAt");

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}