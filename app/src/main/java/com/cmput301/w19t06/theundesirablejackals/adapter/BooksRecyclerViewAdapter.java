package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;


public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

    private BookList dataSet;
    private RecyclerViewClickListener myListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is a MyHealthStats member
        public ConstraintLayout mainTextView;
        private RecyclerViewClickListener myListener;


        MyViewHolder(ConstraintLayout v, RecyclerViewClickListener listener) {
            super(v);
            myListener = listener;
            mainTextView = v;

            mainTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            myListener.onClick(v, getAdapterPosition());
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BooksRecyclerViewAdapter(RecyclerViewClickListener listener) {
        myListener = listener;
        dataSet = new BookList();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public BooksRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_books_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        TextView systolicView = (TextView) holder.mainTextView.findViewById(R.id.Recycler_View_Systolic_Pressure);
//        TextView dateView = (TextView) holder.mainTextView.findViewById(R.id.Recycler_View_Date_of_Measurement);
//        TextView diastolicView = (TextView) holder.mainTextView.findViewById(R.id.Recycler_View_Diastolic_Pressure);
//        TextView heartView = (TextView) holder.mainTextView.findViewById(R.id.Recycler_View_HeartRate);

//        Integer systolicPressure = dataSet.get(position).getSystolicPressure();
//        Integer diastolicPressure = dataSet.get(position).getDiastolicPressure();
//        Integer heartRate = dataSet.get(position).getHeartRate();

//        (systolicView).setText(String.valueOf(systolicPressure));
//        (dateView).setText(dataSet.get(position).getDateMeasured());
//        (diastolicView).setText(String.valueOf(diastolicPressure));
//        (heartView).setText(String.valueOf(heartRate));

//        ((ImageView) holder.mainTextView.findViewById(R.id.Recycler_View_Image)).setImageResource(R.drawable.greencheck);
//
//        if((systolicPressure.compareTo(140) > 0) || (systolicPressure.compareTo(90) < 0 ) ||
//                (diastolicPressure.compareTo(90) > 0) || (diastolicPressure.compareTo(60) < 0)){
//            ((ImageView) holder.mainTextView.findViewById(R.id.Recycler_View_Image)).setImageResource(R.drawable.redx);
//        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void deleteItem(int position){
        dataSet.getBooks().remove(position);
        this.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
        this.notifyItemRangeChanged(position, this.getItemCount());
        updateItems();
    }

    public void addItem(Book newItem){
        dataSet.addBook(newItem);
        updateItems();
    }


    public void addItems(BookList newItems){
        dataSet.addBooks( newItems);
        updateItems();
    }

    private void updateItems(){
        this.notifyDataSetChanged();
    }

    public Book getItem(int position){
        return dataSet.get(position);
    }


    public BookList getDataSet(){
        return dataSet;
    }

}
