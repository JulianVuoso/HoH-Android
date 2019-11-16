package ar.edu.itba.hci.hoh.ui.room;

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

import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.elements.Device;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;
import ar.edu.itba.hci.hoh.ui.device.DeviceAdapter;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomListViewHolder> {
    private Map<Category, List<Device>> map = new HashMap<>();
    private List<Category> categories = new ArrayList<>();
    private OnItemClickListener<Device> listener;

    private List<Device> data;
    private List<DeviceAdapter> adapterList = new ArrayList<>();

    public RoomListAdapter(OnItemClickListener<Device> listener) {
        this.listener = listener;
//        updateMap();
    }

    @NonNull
    @Override
    public RoomListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    public void setDevices(List<Device> devices) {
        this.data = devices;
        updatedDataSet();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updatedDataSet() {
        updateMap();
        for (DeviceAdapter adapter : adapterList)
            adapter.notifyDataSetChanged();
        this.notifyDataSetChanged();
    }

    // TODO: COMO EVITO USAR LAS CATEGORIAS DE DEVICES FRAGMENT? NO SE SI MOVERLAS A MAIN ACTIVITY PORQUE EN EL CREADOR HAGO UN ADAPTER.NOTIFYCHANGES
    // todo: ASI COMO ESTA, SI O SI DEBO PRIMERO ABRIR CATEGORIES ANTES DE ENTRAR EN UN ROOM
    private void updateMap() {
        for (Device dev : data) {
            Category category = Category.getCategoryFromType(dev.getType(), MainActivity.categories);
            if (map.containsKey(category) && !map.get(category).contains(dev))
                map.get(category).add(dev);
            else {
                List<Device> list = new ArrayList<>();
                list.add(dev);
                map.put(category, list);
                this.categories.add(category);
            }
        }
    }

    class RoomListViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        RecyclerView rvCategoryDevices;
        Context context;

        public RoomListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.devices_room_name);
            rvCategoryDevices = itemView.findViewById(R.id.rv_room_devices);
            context = itemView.getContext();
        }

        public void bind(final Category category, final OnItemClickListener<Device> listener) {
            tvCategoryName.setText(category.getName());
            rvCategoryDevices.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
            DeviceAdapter adapter = new DeviceAdapter(listener);
            adapter.setDevices(map.get(category));
            adapterList.add(adapter);
            rvCategoryDevices.setAdapter(adapter);
        }

    }
}
