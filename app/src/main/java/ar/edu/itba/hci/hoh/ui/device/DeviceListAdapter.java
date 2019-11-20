package ar.edu.itba.hci.hoh.ui.device;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.hoh.MyApplication;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.GridLayoutAutofitManager;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {
    private Map<Room, List<Device>> map = new HashMap<>();
    private List<Room> rooms = new ArrayList<>();
    private OnItemClickListener<Device> listener;

    private List<Device> data;
    private List<DeviceAdapter> adapterList = new ArrayList<>();

    public DeviceListAdapter(List<Device> data, OnItemClickListener<Device> listener) {
        this.data = data;
        this.listener = listener;
        updateMap();
    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        holder.bind(rooms.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void updatedDataSet() {
        updateMap();
        for (DeviceAdapter adapter : adapterList)
            adapter.notifyDataSetChanged();
        this.notifyDataSetChanged();
    }

    public void clearDataSet() {
        map.clear();
        rooms.clear();
    }

    private void updateMap() {
        for (Device dev : data) {
            if (map.containsKey(dev.getRoom()) && !map.get(dev.getRoom()).contains(dev))
                map.get(dev.getRoom()).add(dev);
            else {
                List<Device> list = new ArrayList<>();
                list.add(dev);
                map.put(dev.getRoom(), list);
                this.rooms.add(dev.getRoom());
            }
        }
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
            GridLayoutManager manager = new GridLayoutAutofitManager(context, (int) MyApplication.getDeviceCardWidth(), GridLayoutManager.VERTICAL, false);
            rvRoomDevices.setLayoutManager(manager);
            DeviceAdapter adapter = new DeviceAdapter(listener);
            adapter.setDevices(map.get(room));
            adapterList.add(adapter);
            rvRoomDevices.setAdapter(adapter);
        }

    }
}