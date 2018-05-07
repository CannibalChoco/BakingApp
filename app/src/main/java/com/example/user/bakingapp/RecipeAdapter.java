package com.example.user.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe>{
    private Context context;
    private List<Recipe> values;

    public RecipeAdapter(Context context, List<Recipe> values) {
        super(context, R.layout.activity_main, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView textView = row.findViewById(R.id.list_item_name);

        Recipe item = values.get(position);
        textView.setText(item.getName());

        return row;
    }
}
