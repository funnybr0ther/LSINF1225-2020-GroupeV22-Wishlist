package com.example.wishlist.Class;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FollowListItemDecorator extends RecyclerView.ItemDecoration {

    private final int VerticalSpace;

    public FollowListItemDecorator(final int verticalSpace) {
        this.VerticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(@NonNull final Rect outRect, @NonNull final View view, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
        outRect.bottom = this.VerticalSpace;
    }
}
