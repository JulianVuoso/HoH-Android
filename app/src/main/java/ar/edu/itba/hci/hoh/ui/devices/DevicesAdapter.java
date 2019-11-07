package ar.edu.itba.hci.hoh.ui.devices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Category;
import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.R;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {
    private List<Category> data;

    public DevicesAdapter(List<Category> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DevicesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {
        Category category = data.get(position);
        holder.tvCategoryName.setText(category.getName());
        holder.ivCategoryImage.setImageResource(category.getDrawableId());
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
    }
}
