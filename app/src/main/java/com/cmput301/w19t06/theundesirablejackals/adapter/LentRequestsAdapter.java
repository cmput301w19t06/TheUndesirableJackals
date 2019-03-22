package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.LentListActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookToInformationMap;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;


public class LentRequestsAdapter extends RecyclerView.Adapter<LentRequestsAdapter.MyViewHolder> {

    private BookInformationPairing dataSet;
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

//    // Provide a suitable constructor (depends on the kind of dataset)
//    public BooksRecyclerViewAdapter(RecyclerViewClickListener listener) {
//        myListener = listener;
//        dataSet = new BookToInformationMap();
//    }

    public LentRequestsAdapter(){
        dataSet = new BookInformationPairing();
    }

    public void setMyListener(RecyclerViewClickListener listener){
        myListener = listener;
    }

    public void setDataSet(BookInformationPairing data){
        dataSet = data;
        updateItems();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public LentRequestsAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lent_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(LentRequestsAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView titleTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewLentitemTitle);
        TextView authorTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewLentitemAuthor);
        TextView usernameTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewLentitemUsername);
        TextView statusTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewLentitemStatusChange);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewLentitemBook);

        Book b = dataSet.getBook(position);
        BookInformation i = dataSet.getInformation(position);
        BookStatus status = i.getStatus();
        String title = b.getTitle();
        String author = b.getAuthor();
        //String username = b.getUsername();

        if(status != null) {
            statusTextView.setText(status.toString());
        }
        if(title != null) {
            titleTextView.setText(title);
        }
        if(author != null) {
            authorTextView.setText(author);
        }
        /*
        if(username != null) {
            usernameTextView.setText(username);
        }
        */
        switch (status) {
            case BORROWED:
                bookThumbnail.setImageResource(R.drawable.ic_status_borrowed);
                break;
            case ACCEPTED:
                bookThumbnail.setImageResource(R.drawable.ic_status_available);
                break;
            case REQUESTED:
                bookThumbnail.setImageResource(R.drawable.ic_status_requested);
                break;
            default:
                bookThumbnail.setImageResource(R.drawable.book_icon);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void updateItems(){
        this.notifyDataSetChanged();
    }

    public Book getBook(int position){
        return dataSet.getBook(position);
    }

    public BookInformation getInformation(int position){
        return dataSet.getInformation(position);
    }

    public BookInformationPairing getDataSet(){
        return dataSet;
    }

}