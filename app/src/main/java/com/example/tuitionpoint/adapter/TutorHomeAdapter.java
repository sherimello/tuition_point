package com.example.tuitionpoint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionpoint.R;
import com.example.tuitionpoint.SendTuitionRequest;

public class TutorHomeAdapter extends RecyclerView.Adapter<TutorHomeAdapter.TutorHomeAdapterViewHolder> {

    Context context;
    Activity activity;

    public TutorHomeAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
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

                Intent intent = new Intent(holder.cardView.getContext(), SendTuitionRequest.class);
                // below method is used to make scene transition
                // and adding fade animation in it.
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, holder.cardView, "request card");
                // starting our activity with below method.
                activity.startActivity(intent, options.toBundle());

//                card_popup.setVisibility(View.VISIBLE);
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

                    cardView.setLayerType(View.LAYER_TYPE_NONE, null);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

}
