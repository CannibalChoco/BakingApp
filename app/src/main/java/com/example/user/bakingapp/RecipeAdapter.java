package com.example.user.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.model.Recipe;

import junit.framework.Test;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private Context context;
    private List<Recipe> recipeList;

    public interface OnRecipeClickListener{
        void onRecipeSelected(int position);
    }

    private static OnRecipeClickListener recipeClickListener;

    public RecipeAdapter(Context context, List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        recipeClickListener = listener;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.name.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public void clear(){
        int size = getItemCount();
        this.recipeList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Recipe> recipeList){
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        @BindView(R.id.list_item_name)
        TextView name;

        ViewHolder(View itemView){
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            Log.d("RECIPE", "ViewHolder - recipe selected");
            recipeClickListener.onRecipeSelected(getAdapterPosition());

        }
    }
}
