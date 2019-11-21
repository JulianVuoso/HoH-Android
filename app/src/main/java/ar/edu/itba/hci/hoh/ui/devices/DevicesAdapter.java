package ar.edu.itba.hci.hoh.ui.devices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {
    private List<Category> data;
    private OnItemClickListener<Category> listener;

    public DevicesAdapter(List<Category> data, OnItemClickListener<Category> onClickListener) {
        this.data = data;
        this.listener = onClickListener;
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DevicesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DevicesViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivCategoryImage;


        public DevicesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.cat_name);
            ivCategoryImage = itemView.findViewById(R.id.cat_img);
        }

        public void bind(final Category category, final OnItemClickListener<Category> listener) {
            tvCategoryName.setText(category.getName());
            ivCategoryImage.setImageResource(category.getDrawableId());
            ivCategoryImage.setContentDescription(category.getName());
            itemView.setOnClickListener(v -> listener.onItemClick(category));
        }
    }
}
