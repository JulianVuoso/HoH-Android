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

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.Routine;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.RoutinesViewHolder> {
    private List<Routine> data;
    private OnItemClickListener<Routine> listener;

    public RoutinesAdapter(OnItemClickListener<Routine> onClickListener) {
        this.listener = onClickListener;
    }

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_img_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {
        if (data != null)
            holder.bind(data.get(position), listener);
        else
            holder.tvRoutineName.setText(R.string.empty_routine_list);
    }

    public void setRoutines(List<Routine> routines) {
        this.data = routines;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.data != null)
            return data.size();
        return 0;
    }

    class RoutinesViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoutineName;
        ImageView ivRoutineImage;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoutineName = itemView.findViewById(R.id.card_item_name);
            ivRoutineImage = itemView.findViewById(R.id.card_item_img);
        }

        public void bind(final Routine routine, final OnItemClickListener<Routine> listener) {
            tvRoutineName.setText(routine.getName());
            ivRoutineImage.setImageResource(MyApplication.getDrawableFromString(routine.getMeta().getImg()));
            ivRoutineImage.setContentDescription(routine.getMeta().getImg());
            itemView.setOnClickListener(v -> listener.onItemClick(routine));
        }
    }
}
