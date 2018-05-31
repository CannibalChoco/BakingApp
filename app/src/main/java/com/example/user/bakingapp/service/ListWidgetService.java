package com.example.user.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.utils.SharedPreferencesUtils;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Ingredient> ingredients;
    private Context context;

    public ListRemoteViewsFactory(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        // get new list
        ingredients = SharedPreferencesUtils.getIngredientListFromPreferences(context);
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
        remoteViews.setTextViewText(R.id.widget_list_item_ingredient, ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.widget_list_item_quantity_with_measure,
                ingredient.getQuantityWithMeasure());

        Intent fillInIntent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item_container, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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