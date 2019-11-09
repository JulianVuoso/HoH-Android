package ar.edu.itba.hci.hoh.ui.device;

import android.util.Log;
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
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> data;
    private OnItemClickListener<Device> listener;

    public DeviceAdapter(List<Device> data, OnItemClickListener<Device> listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceState;
        TextView tvDeviceName;
        TextView tvDeviceRoom;
        ImageView ivDeviceImage;


        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.dev_name);
            tvDeviceRoom = itemView.findViewById(R.id.dev_room);
            tvDeviceState = itemView.findViewById(R.id.dev_state);
            ivDeviceImage = itemView.findViewById(R.id.dev_img);
        }

        public void bind(final Device device, final OnItemClickListener<Device> listener) {
            tvDeviceName.setText(device.getName());
            tvDeviceRoom.setText(device.getRoom().getName());
            tvDeviceState.setText(device.getState());
            ivDeviceImage.setImageResource(DeviceType.getDeviceTypeDrawable(device.getType()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(device);
                }
            });
        }
    }
}
