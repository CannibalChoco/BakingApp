package com.example.user.bakingapp.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>{

    public interface OnCheckedStateListener{
        void onCheckBoxStateChanged(int position, boolean isChecked);
    }

    private OnCheckedStateListener checkedStateListener;
    private List<Ingredient> ingredients;
    private boolean[] isCheckedArray;

    public IngredientAdapter(List<Ingredient> ingredients, OnCheckedStateListener listener,
                             boolean[] isCheckedArray){
        this.ingredients = ingredients;
        this.checkedStateListener = listener;
        this.isCheckedArray = isCheckedArray;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ingredient, parent, false);

        return new ViewHolder(view, checkedStateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.checkBox.setText(ingredient.getIngredient());
        if (isCheckedArray != null){
            holder.checkBox.setChecked(isCheckedArray[position]);
        }

        holder.quantityWithMeasure.setText(ingredient.getQuantityWithMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.quantity_with_measure)
        TextView quantityWithMeasure;
        @BindView(R.id.checkBox)
        CheckBox checkBox;

        private OnCheckedStateListener listener;

        ViewHolder(View itemView, OnCheckedStateListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.listener = listener;
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCheckBoxStateChanged(getAdapterPosition(), checkBox.isChecked());
        }
    }
}