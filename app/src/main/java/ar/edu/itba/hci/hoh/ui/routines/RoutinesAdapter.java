package ar.edu.itba.hci.hoh.ui.routines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.RoutinesViewHolder> {
    private List<Routine> data;
    private OnItemClickListener<Routine> listener;

    public RoutinesAdapter(List<Routine> data, OnItemClickListener<Routine> onClickListener) {
        this.data = data;
        this.listener = onClickListener;
    }

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: VER SI QUEDA item_img_cardcard.xml
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_img_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RoutinesViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoutineName;
        ImageView ivRoutineImage;
        Context context; // TODO: VER SI PUEDO EVITARLO

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoutineName = itemView.findViewById(R.id.card_item_name);
            ivRoutineImage = itemView.findViewById(R.id.card_item_img);
            context = itemView.getContext();
        }

        public void bind(final Routine routine, final OnItemClickListener<Routine> listener) {
            tvRoutineName.setText(routine.getName());
            ivRoutineImage.setImageResource(MainActivity.getDrawableFromString(context, routine.getMeta().getImg()));
            ivRoutineImage.setContentDescription(routine.getMeta().getImg());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(routine);
                }
            });
        }
    }
}
