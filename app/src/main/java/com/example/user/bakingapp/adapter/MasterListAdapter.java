package com.example.user.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.R;
import com.example.user.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.ViewHolder>{

    private List<Step> steps;
    private OnStepClickListener listener;

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    public MasterListAdapter(List<Step> steps, OnStepClickListener listener){
        this.listener = listener;
        this.steps = steps;
    }


    @NonNull
    @Override
    public MasterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_step, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterListAdapter.ViewHolder holder, int position) {

        if (position == 0){
            holder.ingredients.setText(R.string.label_ingredients);
        } else {
            String itemNr = String.valueOf(position) + ".";
            holder.itemNr.setText(itemNr);
            holder.shortDescription.setText(steps.get(position).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        private OnStepClickListener listener;

        @BindView(R.id.short_description)
        TextView shortDescription;
        @BindView(R.id.item_nr)
        TextView itemNr;
        @BindView(R.id.ingredients)
        TextView ingredients;

        ViewHolder(View itemView, OnStepClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onStepSelected(getAdapterPosition());
            }
    }
}
