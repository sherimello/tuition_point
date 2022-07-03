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
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.core.app.ActivityOptionsCompat;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.tuitionpoint.R;
        import com.example.tuitionpoint.SendTuitionRequest;
        import com.example.tuitionpoint.classes.TuitionRequest;

        import java.text.MessageFormat;
        import java.util.ArrayList;

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.PendingRequestsAdapterViewHolder> {

    ArrayList<TuitionRequest> tuitionRequests;
    Context context;
    Activity activity;

    public PendingRequestsAdapter(Context context, Activity activity, ArrayList<TuitionRequest> tuitionRequests) {
        this.context = context;
        this.activity = activity;
        this.tuitionRequests = tuitionRequests;
    }

    @NonNull
    @Override
    public PendingRequestsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_requests, parent, false);
        return new PendingRequestsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestsAdapterViewHolder holder, int position) {

        TuitionRequest currentItem = tuitionRequests.get(position);

        holder.cardView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        holder.cardView.startAnimation(holder.animation);

        holder.text_address.setText(MessageFormat.format("address: {0}", currentItem.address));
        holder.text_class.setText(MessageFormat.format("class: {0}", currentItem.student_class));
        holder.text_days.setText(MessageFormat.format("days: {0}", currentItem.days));
        holder.text_description.setText(MessageFormat.format("description: {0}", currentItem.description));
        holder.text_salary.setText(MessageFormat.format("salary: {0}", currentItem.salary));
        holder.text_for_sub.setText(MessageFormat.format("For: {0}", currentItem.subject));

//        holder.cardView.setOnClickListener(view -> {
//
//            Intent intent = new Intent(holder.cardView.getContext(), SendTuitionRequest.class);
//            // below method is used to make scene transition
//            // and adding fade animation in it.
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity, holder.cardView, "request card");
//            // starting our activity with below method.
//            activity.startActivity(intent, options.toBundle());
//
////                card_popup.setVisibility(View.VISIBLE);
//        });
    }

    @Override
    public int getItemCount() {
        return tuitionRequests.size();
    }

    public static class PendingRequestsAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        Animation animation;
        TextView text_for_sub, text_address, text_class, text_days, text_salary, text_description;

        public PendingRequestsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            text_for_sub = itemView.findViewById(R.id.text_for_sub);
            text_address = itemView.findViewById(R.id.text_address);
            text_class = itemView.findViewById(R.id.text_class);
            text_days = itemView.findViewById(R.id.text_days);
            text_salary = itemView.findViewById(R.id.text_salary);
            text_description = itemView.findViewById(R.id.text_description);
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
