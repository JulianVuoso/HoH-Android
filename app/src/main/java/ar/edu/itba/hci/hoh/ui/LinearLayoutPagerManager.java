package ar.edu.itba.hci.hoh.ui;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ar.edu.itba.hci.hoh.MainActivity;

public class LinearLayoutPagerManager extends LinearLayoutManager {
    private int itemsPerPage;
    private int itemWidth;

    public LinearLayoutPagerManager(Context context, int orientation, boolean reverseLayout, int itemsPerPage) {
        super(context, orientation, reverseLayout);
        this.itemsPerPage = itemsPerPage;
    }

    public LinearLayoutPagerManager(Context context, int orientation, boolean reverseLayout, float width) {
        super(context, orientation, reverseLayout);
        this.itemWidth = (int) width;
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

    private int getItemSize() {
        int pageSize;
        if (getOrientation() == HORIZONTAL)
            pageSize = getWidth() - getPaddingLeft() -getPaddingLeft();
        else
            pageSize = getHeight() - getPaddingBottom() - getPaddingTop();
        if (itemsPerPage != 0)
            return Math.round((float) pageSize / itemsPerPage);
        else
            return itemWidth;
    }
}
