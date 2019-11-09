package ar.edu.itba.hci.hoh.ui.device;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.itba.hci.hoh.Elements.Device;
import ar.edu.itba.hci.hoh.Elements.DeviceType;
import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {
    private Map<Room, List<Device>> map = new HashMap<>();
    private List<Room> data = new ArrayList<>();
    private OnItemClickListener<Device> listener;

    public DeviceListAdapter(List<Device> data, OnItemClickListener<Device> listener) {
        for (Device dev : data) {
            if (map.containsKey(dev.getRoom()))
                map.get(dev.getRoom()).add(dev);
            else {
                List<Device> list = new ArrayList<>();
                list.add(dev);
                map.put(dev.getRoom(), list);
                this.data.add(dev.getRoom());
            }
        }
//        this.data = data; // TODO: VER SI HACE FALTA ESTA LISTA
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        Log.v("TAG", String.format("OnBindViewHolder with %d, %s", position, data.get(position).getName()));
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DeviceListViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName;
        RecyclerView rvRoomDevices;
        Context context;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.devices_room_name);
            rvRoomDevices = itemView.findViewById(R.id.rv_room_devices);
            context = itemView.getContext();
        }

        public void bind(final Room room, final OnItemClickListener<Device> listener) {
            tvRoomName.setText(room.getName());
            rvRoomDevices.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
            rvRoomDevices.setAdapter(new DeviceAdapter(map.get(room), listener));
        }

    }
}