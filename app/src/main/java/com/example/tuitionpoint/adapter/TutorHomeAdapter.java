package com.example.tuitionpoint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionpoint.R;

public class TutorHomeAdapter extends RecyclerView.Adapter<TutorHomeAdapter.TutorHomeAdapterViewHolder> {

    CardView card_popup;

    public TutorHomeAdapter(CardView card_popup) {
        this.card_popup = card_popup;
    }

    @NonNull
    @Override
    public TutorHomeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tuition_card_layout, parent, false);
        return new TutorHomeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorHomeAdapterViewHolder holder, int position) {

        holder.cardView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        holder.cardView.startAnimation(holder.animation);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card_popup.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public static class TutorHomeAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        Animation animation;

        public TutorHomeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.popup);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    cardView.setLayerType(View.LAYER_TYPE_NONE,null);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

}
