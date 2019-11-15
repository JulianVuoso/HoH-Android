package ar.edu.itba.hci.hoh.ui.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.hoh.elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder> {
    private List<Room> data;
    private OnItemClickListener<Room> listener;

    public RoomsAdapter(OnItemClickListener<Room> onClickListener) {
        this.listener = onClickListener;
    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_img_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {
        if (data != null) {
            holder.bind(data.get(position), listener);
        } else {
            holder.tvRoomName.setText(R.string.empty_room_list);
        }
    }

    public void setRooms(List<Room> rooms) {
        this.data = rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.data != null)
            return data.size();
        return 0;
    }

    class RoomsViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName;
        ImageView ivRoomImage;
        Context context; // TODO: VER SI PUEDO EVITARLO

        public RoomsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.card_item_name);
            ivRoomImage = itemView.findViewById(R.id.card_item_img);
            context = itemView.getContext();
        }

        public void bind(final Room room, final OnItemClickListener<Room> listener) {
            tvRoomName.setText(room.getName());
            ivRoomImage.setImageResource(MainActivity.getDrawableFromString(context, room.getMeta().getImage()));
            ivRoomImage.setContentDescription(room.getMeta().getImage());
            itemView.setOnClickListener(v -> listener.onItemClick(room));
        }
    }
}
