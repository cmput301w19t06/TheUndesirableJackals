package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.squareup.picasso.Picasso;


public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

    private BookInformationPairing dataSet;
    private BookInformationPairing dataCopy;
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


    public BooksRecyclerViewAdapter(){
        dataSet = new BookInformationPairing();
        dataCopy = new BookInformationPairing();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public BooksRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
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

        // get URL of the thumbnail
//        String thumbnail = b.getThumbnail();

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
        Picasso.get()
                .load(R.drawable.book_icon)
                .into(bookThumbnail);


        switch (status) {
            case REQUESTED:
                statusTextView.setTextColor(Color.parseColor("#EA4335"));
                break;
            case ACCEPTED:
                statusTextView.setTextColor(Color.parseColor("#4285F4"));
                break;
            case BORROWED:
                statusTextView.setTextColor(Color.parseColor("##A52A2A"));
                break;
            case AVAILABLE:
                statusTextView.setTextColor(Color.parseColor("#34A853"));

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onViewAttachedToWindow(BooksRecyclerViewAdapter.MyViewHolder holder){
//        super.onViewAttachedToWindow(holder);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewMyBooksItemPhoto);
        int position = holder.getAdapterPosition();
        Book book = dataSet.getBook(position);
        BookInformation bookInformation = dataSet.getInformation(position);
        if(book.getThumbnail() != null && !book.getThumbnail().isEmpty()){
            Picasso.get()
                    .load(book.getThumbnail())
                    .error(R.drawable.book_icon)
                    .placeholder(R.drawable.book_icon)
                    .into(bookThumbnail);
        }

    }

    public void deleteItem(int position){
        dataSet.remove(position);
        dataCopy.remove(position);
        this.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
        this.notifyItemRangeChanged(position, this.getItemCount());
        updateItems();
    }

    public void addItem(Book book, BookInformation bookInformation){
        dataSet.addPair(new Book(book), new BookInformation(bookInformation));
        dataCopy.addPair(new Book(book), new BookInformation(bookInformation));
        updateItems();
    }


    public void addItems(BookInformationPairing newItems){
        dataSet.addAll(new BookInformationPairing(newItems.getBookList(), newItems.getBookInformationList()));
        dataCopy.addAll(new BookInformationPairing(newItems.getBookList(), newItems.getBookInformationList()));
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

    public BookInformationPairing getDataCopy() {
        return dataCopy;
    }

    public void setDataCopy(BookInformationPairing dataCopy) {
        this.dataCopy = dataCopy;
    }


    public void setMyListener(RecyclerViewClickListener listener){
        myListener = listener;
    }

    public void setDataSet(BookInformationPairing data){
        dataSet = new BookInformationPairing(data.getBookList(), data.getBookInformationList());
        updateItems();
    }

}
