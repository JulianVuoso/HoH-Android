package ar.edu.itba.hci.hoh.ui;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ar.edu.itba.hci.hoh.MainActivity;

public class LinearLayoutPagerManager extends LinearLayoutManager {
    private int itemsPerPage;

    public LinearLayoutPagerManager(Context context, int orientation, boolean reverseLayout, int itemsPerPage) {
        super(context, orientation, reverseLayout);
        this.itemsPerPage = itemsPerPage;
    }

    public int getitemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return super.checkLayoutParams(lp) && lp.width == getItemSize();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return setProperItemSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return setProperItemSize(super.generateLayoutParams(lp));
    }

    private RecyclerView.LayoutParams setProperItemSize(RecyclerView.LayoutParams lp) {
        int itemSize = getItemSize();
        if (getOrientation() == HORIZONTAL)
            lp.width = itemSize;
        else
            lp.height = itemSize;
        return lp;
    }

    // TODO: REVISAR CONSTANTE PARA CUANDO CAMBIA EL FACTOR DE FORMA O ROTO
    private int getItemSize() {
        int pageSize;
        if (getOrientation() == HORIZONTAL)
            pageSize = getWidth() - 120;
        else
            pageSize = getHeight();
        Log.v(MainActivity.LOG_TAG, String.format("WIDTH TOTAL: %d, PAGESIZE: %d", getWidth(), pageSize));
        return Math.round((float) pageSize / itemsPerPage);
    }
}
