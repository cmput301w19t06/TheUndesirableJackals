package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;


public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

    private BookInformationList dataSet;
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
//        dataSet = new BookInformationList();
//    }

    public BooksRecyclerViewAdapter(){
        dataSet = new BookInformationList();
    }

    public void setMyListener(RecyclerViewClickListener    listener){
        myListener = listener;
    }

    public void setDataSet(BookInformationList books){
        dataSet = books;
        updateItems();
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
    public void onBindViewHolder(BooksRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView statusTextView = (TextView) holder.mainTextView.findViewById(R.id.myBooks_status);
        TextView titleTextView = (TextView) holder.mainTextView.findViewById(R.id.myBooks_book_title);
        TextView authorTextView = (TextView) holder.mainTextView.findViewById(R.id.myBooks_author);
        TextView isbnTextView = (TextView) holder.mainTextView.findViewById(R.id.myBooks_isbn);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.myBooks_img_book);

        Book b = dataSet.get(position);
        BookStatus status = b.getStatus();
        String title = b.getTitle();
        String author = b.getAuthor();
        String isbn = b.getIsbn();

        if(status != null) {
            statusTextView.setText(status.toString());
        }
        if(title != null) {
            titleTextView.setText(title);
        }
        if(author != null) {
            authorTextView.setText(author);
        }
        if(isbn != null) {
            isbnTextView.setText("ISBN: "+ isbn);
        }
        switch (status) {
            case ACCEPTED:
                bookThumbnail.setImageResource(R.drawable.ic_status_requested);
                break;
            case BORROWED:
                bookThumbnail.setImageResource(R.drawable.ic_status_borrowed);
                break;
            case AVAILABLE:
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


    public void addItems(BookInformationList newItems){
        dataSet.addBooks( newItems);
        updateItems();
    }

    private void updateItems(){
        this.notifyDataSetChanged();
    }

    public Book getItem(int position){
        return dataSet.get(position);
    }


    public BookInformationList getDataSet(){
        return dataSet;
    }

}
