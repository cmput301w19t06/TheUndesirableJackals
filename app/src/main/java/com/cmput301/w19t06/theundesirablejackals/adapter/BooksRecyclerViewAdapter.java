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
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

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

    public BooksRecyclerViewAdapter(){
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
    public BooksRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BooksRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView statusTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewMyBooksItemStatus);
        TextView titleTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewMyBooksItemTitle);
        TextView authorTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewMyBooksItemAuthor);
        TextView isbnTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewMyBooksItemIsbn);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewMyBooksItemPhoto);

        Book b = dataSet.getBook(position);
        BookInformation i = dataSet.getInformation(position);
        BookStatus status = i.getStatus();
        String title = b.getTitle();
        String author = b.getAuthor();
        String isbn = b.getIsbn();


        if(status != null) {
            if(status == BookStatus.UNKNOWN) {
                statusTextView.setText("");
            } else {
                statusTextView.setText(status.toString());
            }

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
            case UNKNOWN:
                if(b.getThumbnail() != null && !b.getThumbnail().isEmpty()) {
                    Picasso.get()
                            .load(b.getThumbnail())
                            .error(R.drawable.book_icon)
                            .placeholder(R.drawable.book_icon)
                            .into(bookThumbnail);
                }else{
                    bookThumbnail.setImageResource(R.drawable.book_icon);
                }
            default:
                bookThumbnail.setImageResource(R.drawable.book_icon);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onViewAttachedToWindow(BooksRecyclerViewAdapter.MyViewHolder holder){
        super.onViewAttachedToWindow(holder);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewMyBooksItemPhoto);
        int position = holder.getAdapterPosition();
        Book book = dataSet.getBook(position);
        BookInformation bookInformation = dataSet.getInformation(position);
        BookStatus bookStatus = bookInformation.getStatus();
        if(bookStatus == BookStatus.UNKNOWN){
            if(book.getThumbnail() != null && !book.getThumbnail().isEmpty()){
                Picasso.get()
                        .load(book.getThumbnail())
                        .error(R.drawable.book_icon)
                        .placeholder(R.drawable.book_icon)
                        .into(bookThumbnail);
            }
        }
    }

    public void deleteItem(int position){
        dataSet.remove(position);
        this.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
        this.notifyItemRangeChanged(position, this.getItemCount());
        updateItems();
    }

    public void addItem(Book book, BookInformation bookInformation){
        dataSet.addPair(book, bookInformation);
        updateItems();
    }


    public void addItems(BookInformationPairing newItems){
        dataSet.addAll( newItems);
        updateItems();
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

    // performs filter operation for the recyclerview
    public  void setFilter(List<String> listItem){

        List<String> titles = new ArrayList<>();
        titles.addAll(listItem);
        notifyDataSetChanged();


    }

}
