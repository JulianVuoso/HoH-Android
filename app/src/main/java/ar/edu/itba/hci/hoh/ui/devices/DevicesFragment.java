package ar.edu.itba.hci.hoh.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ar.edu.itba.hci.hoh.elements.Category;
import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.ui.OnItemClickListener;


public class DevicesFragment extends Fragment {

    private DevicesViewModel devicesViewModel;

    private RecyclerView rvCategories;
    private GridLayoutManager gridLayoutManager;
    private static DevicesAdapter adapter;

    private static CardView emptyCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_devices, container, false);

        MainActivity.reloadCategories();

        rvCategories = root.findViewById(R.id.rv_categories);
        // Para numero automatico, ver:
        // https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
        // TODO: VER SI NO HACEMOS CARDS COMO EN ROOM
        gridLayoutManager = new GridLayoutManager(this.getContext(), 3, GridLayoutManager.VERTICAL, false);
        rvCategories.setLayoutManager(gridLayoutManager);
        adapter = new DevicesAdapter(MainActivity.categories, category -> {
            DevicesFragmentDirections.ActionSelectCategory action = DevicesFragmentDirections.actionSelectCategory(category, category.getName());
            Navigation.findNavController(root).navigate(action);
        });
        rvCategories.setAdapter(adapter);

        emptyCard = root.findViewById(R.id.empty_categories_card);
        if (MainActivity.categories.isEmpty()) {
            TextView tvEmptyRoom = emptyCard.findViewById(R.id.card_no_element_text);
            tvEmptyRoom.setText(R.string.empty_category_list);
        } else {
            emptyCard.setVisibility(View.GONE);
        }

        return root;
    }

    public static void notifyAdapter() {
        if (emptyCard != null)
            emptyCard.setVisibility(View.GONE);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}