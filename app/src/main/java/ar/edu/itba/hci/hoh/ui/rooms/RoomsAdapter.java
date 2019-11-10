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

import ar.edu.itba.hci.hoh.Elements.Room;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder> {
    private List<Room> data;
    private OnItemClickListener<Room> listener;

    public RoomsAdapter(List<Room> data, OnItemClickListener<Room> onClickListener) {
        this.data = data;
        this.listener = onClickListener;
    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // TODO: CAMBIAR ESTO CUANDO HAGA EL item_room.xml
    class RoomsViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName;
        ImageView ivRoomImage;
        Context context; // TODO: VER SI PUEDO EVITARLO

        public RoomsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.room_name);
            ivRoomImage = itemView.findViewById(R.id.room_img);
            context = itemView.getContext();
        }

        public void bind(final Room room, final OnItemClickListener<Room> listener) {
            tvRoomName.setText(room.getName());
            ivRoomImage.setImageResource(MainActivity.getDrawableFromString(context, room.getMeta().getImage()));
            ivRoomImage.setContentDescription(room.getMeta().getImage());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(room);
                }
            });
        }
    }
}
