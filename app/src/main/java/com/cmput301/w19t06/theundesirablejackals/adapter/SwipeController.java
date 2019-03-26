package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class SwipeController<T extends RecyclerView.Adapter> extends Callback {
    private T myAdapter;

    public SwipeController(T adapter){
        super();
        myAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //myAdapter.updateItems();
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //myAdapter.updateItems();
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        Integer position = viewHolder.getAdapterPosition();
//        Log.d("SwipeController", position.toString());
//        myAdapter.deleteItem(position);

    }
}
