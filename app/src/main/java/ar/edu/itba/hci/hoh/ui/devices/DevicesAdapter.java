package ar.edu.itba.hci.hoh.ui.devices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.R;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {
    private List<Device> data;

    public DevicesAdapter(List<Device> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DevicesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {
        Device device = data.get(position);
        holder.tvDeviceName.setText(device.getName());
        holder.tvDeviceRoom.setText(device.getRoom().getName());
        holder.tvDeviceState.setText(device.getState());
        holder.ivDeviceImage.setImageResource(DeviceType.getDeviceTypeDrawable(device.getType()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class DevicesViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceState;
        TextView tvDeviceName;
        TextView tvDeviceRoom;
        ImageView ivDeviceImage;


        public DevicesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.dev_name);
            tvDeviceRoom = itemView.findViewById(R.id.dev_room);
            tvDeviceState = itemView.findViewById(R.id.dev_state);
            ivDeviceImage = itemView.findViewById(R.id.dev_img);
        }
    }
}
