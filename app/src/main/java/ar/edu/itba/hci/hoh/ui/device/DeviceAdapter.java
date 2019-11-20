package ar.edu.itba.hci.hoh.ui.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.DeviceType;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> data;
    private OnItemClickListener<Device> listener;

    public DeviceAdapter(OnItemClickListener<Device> listener) {
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
        if (data != null)
            holder.bind(data.get(position), listener);
        else
            holder.tvDeviceName.setText(R.string.empty_device_list);
    }

    public void setDevices(List<Device> devices) {
        this.data = devices;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.data != null)
            return data.size();
        return 0;
    }


    class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceState;
        TextView tvDeviceName;
        TextView tvDeviceRoom;
        ImageView ivDeviceImage;
        CardView cardView;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.dev_name);
            tvDeviceRoom = itemView.findViewById(R.id.dev_room);
            tvDeviceState = itemView.findViewById(R.id.dev_state);
            ivDeviceImage = itemView.findViewById(R.id.dev_img);
            cardView = itemView.findViewById(R.id.dev_card);
        }

        public void bind(final Device device, final OnItemClickListener<Device> listener) {
            tvDeviceName.setText(device.getName());
            tvDeviceRoom.setText(device.getRoom().getName());
            tvDeviceState.setText(MyApplication.getDeviceStatusString(device.getState()));
            ivDeviceImage.setImageResource(DeviceType.getDeviceTypeDrawable(device.getType()));
            cardView.setCardBackgroundColor(MyApplication.getCardBackgroundColor(device.getState()));
            itemView.setOnClickListener(view -> listener.onItemClick(device));
        }
    }
}
