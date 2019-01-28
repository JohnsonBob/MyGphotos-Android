package io.gphotos.gin.ui.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mColumn;
    private int mSpace;

    public GridSpacesItemDecoration(int i, int i2) {
        this.mSpace = i;
        this.mColumn = i2;
    }

    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        rect.set(this.mSpace, this.mSpace, this.mSpace, this.mSpace);
    }
}
