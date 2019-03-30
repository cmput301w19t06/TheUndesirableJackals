package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;


public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "RequestAdapter";
    private BookRequestList dataSet;
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

    public RequestsRecyclerViewAdapter(){
        dataSet = new BookRequestList();
    }

    public void setMyListener(RecyclerViewClickListener listener){
        myListener = listener;
    }

    public void setDataSet(BookRequestList data){
        dataSet = data;
        updateItems();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RequestsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RequestsRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView titleTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewRequestItemTitle);
        final TextView authorTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewRequestItemAuthor);
        TextView requesterTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewRequestItemBorrower);
        TextView ownerTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewRequestItemOwner);
        TextView statusTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewRequestItemStatusChange);
        TextView isbnTextView = holder.mainTextView.findViewById(R.id.textViewRequestItemISBN);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewRequestItemBook);


        BookRequest bookRequest = dataSet.get(position);
        BookInformation i = bookRequest.getBookRequested();
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getBookFromDatabase(i.getIsbn(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                if(book != null){
                    titleTextView.setText(book.getTitle());
                    authorTextView.setText(book.getAuthor());
                }
            }
        });
        BookRequestStatus status = bookRequest.getCurrentStatus();

        String requester = bookRequest.getBorrower().getUserName();

        String owner = bookRequest.getBookRequested().getOwner();

        if(status != null) {
            statusTextView.setText(status.getStatusDescription());
        }

        if(bookRequest.getBookRequested().getIsbn()!=null) {
            isbnTextView.setText("ISBN: " + bookRequest.getBookRequested().getIsbn());
        }

        if(requester != null) {
            requesterTextView.setText("Requester: "+ requester);
        } else {
            requesterTextView.setText("ERROR LOADING BORROWER");
        }

        if (owner != null) {
            ownerTextView.setText("Owner: "+owner);
        }  else {
            ownerTextView.setText("ERROR LOADING OWNER");
        }

        switch (status) {
            case REQUESTED:
                bookThumbnail.setImageResource(R.drawable.ic_status_borrowed);
                break;
            case ACCEPTED:
                bookThumbnail.setImageResource(R.drawable.ic_status_available);
                break;
            case CANCELLED:
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

    public BookRequestList getDataSet(){
        return dataSet;
    }

    public BookRequest get(int i){
        return dataSet.get(i);
    }

    public void deleteItem(final int position){
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.deleteRequest(dataSet.get(position), new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {
                        if(bool){
                            dataSet.getBookRequests().remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                        }else{
                            notifyItemChanged(position);
                        }
                    }
                });

    }

    public void doDeleteCleanup(final int position){
        final DatabaseHelper databaseHelper = new DatabaseHelper();
        final BookRequest request = dataSet.get(position);
        databaseHelper.getSpecificBookLendRequests(request.getBookRequested().getOwner(),
                request.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        deleteItem(position);
                        if(bookRequestList != null && bookRequestList.size() > 0){
                            boolean noOpenRequests = true;
                            for(BookRequest bookRequest : bookRequestList.getBookRequests()){
                                if(bookRequest.getCurrentStatus() != BookRequestStatus.DENIED  &&
                                        !bookRequest.getBookRequestBorrowKey().equals(request.getBookRequestBorrowKey())){
                                    noOpenRequests = false;
                                }
                            }
                            if(noOpenRequests) {
                                BookInformation bookInformation = request.getBookRequested();
                                bookInformation.setStatus(BookStatus.AVAILABLE);
                                databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if (bool) {
                                            Log.d(TAG, "Request updated successfully");
                                        } else {
                                            Log.d(TAG, "Couldn't update book status to AVAILABLE");
                                        }
                                    }
                                });
                            }
                        }else if(bookRequestList == null){
                            Log.d(TAG, "Something went wrong updating the book status");
                        }else{
                            Log.d(TAG, "You shouldn't ever see this message...");
                        }
                    }
                });
    }

}
