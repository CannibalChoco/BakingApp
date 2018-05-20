package com.example.user.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private Context context;
    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener{
        void onRecipeSelected(int position);
    }

    public RecipeAdapter(Context context, List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.name.setText(recipe.getName());
        String imageUrl = recipe.getImage();
        boolean imageFound = false;

        if (imageUrl == null || imageUrl.isEmpty()){
            List<Step> steps = recipe.getSteps();

            // go through each step searching for thumbnail
            for (Step step : steps){
                String thumbnailUrl = step.getThumbnailURL();
                if(thumbnailUrl != null && !thumbnailUrl.isEmpty()){
                    imageUrl = thumbnailUrl;
                    imageFound = true;
                    // break out of loop if image url found
                    break;
                }
            }

        } else {
            imageFound = true;
        }

        if (imageFound){
            Picasso.with(context).load(imageUrl).into(holder.recipeImageView);
        }

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

        private OnRecipeClickListener listener;

        @BindView(R.id.list_item_name)
        TextView name;
        @BindView(R.id.recipe_image_view)
        ImageView recipeImageView;

        ViewHolder(View itemView, OnRecipeClickListener listener){
            super(itemView);

            this.listener = listener;

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecipeSelected(getAdapterPosition());
        }
    }
}
