package ar.edu.itba.hci.hoh.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecorator extends RecyclerView.ItemDecoration {
    private int spaceHeight;

    public MarginItemDecorator (int spaceHeight){
        this.spaceHeight = spaceHeight;
    }

    public int getSpaceHeight() {
        return spaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = spaceHeight;
        outRect.top = spaceHeight;
        outRect.right = spaceHeight;
        outRect.left = spaceHeight;
    }
}
