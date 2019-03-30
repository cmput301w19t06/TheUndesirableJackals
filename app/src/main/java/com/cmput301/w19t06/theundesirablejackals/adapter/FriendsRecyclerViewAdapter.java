package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.user.UserList;
import com.squareup.picasso.Picasso;



public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.MyViewHolder> {

    private UserList dataSet;
    private UserList dataCopy;
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

    public FriendsRecyclerViewAdapter(){
        //get the data.... Unsure if this is it...
        dataSet = new UserList();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public FriendsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friends, parent, false);
        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendsRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView usernameTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewItemFriendsUserName);
        TextView emailTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewItemFriendsEmail);
        ImageView profileImageView = (ImageView) holder.mainTextView.findViewById(R.id.imageViewItemFriendsPhoto);


        UserInformation i = (UserInformation) dataSet.getUser(position);


        // get URL of the thumbnail
        String profile = i.getUserPhoto();
        String username = i.getUserName();
        String email = i.getEmail();

        emailTextView.setText(email);

        if(profile != null) {
            usernameTextView.setText(username);
        }
        if(email != null) {
            emailTextView.setText(email);
        }
        if(profile != null && !profile.isEmpty()) {
            Picasso.get()
                    //Todo --> Set the profile image
                    .load(i.getUserPhoto())
                    .error(R.drawable.book_icon)
                    .placeholder(R.drawable.book_icon);
                    //.into(bookThumbnail);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onViewAttachedToWindow(FriendsRecyclerViewAdapter.MyViewHolder holder){
//        super.onViewAttachedToWindow(holder);
        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewItemFriendsPhoto);
        int position = holder.getAdapterPosition();
        UserInformation u = dataSet.getUser(position);

        /*
        //Todo --> no idea what this is... figure it out
        if(bookStatus == BookStatus.UNKNOWN){
            if(book.getThumbnail() != null && !book.getThumbnail().isEmpty()){
                Picasso.get()
                        .load(book.getThumbnail())
                        .error(R.drawable.book_icon)
                        .placeholder(R.drawable.book_icon)
                        .into(bookThumbnail);
            }
        }
        */
    }

    public void deleteItem(int position){
        dataSet.delete(dataSet.getUser(position));
        dataCopy.delete(dataSet.getUser(position));
        this.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
        this.notifyItemRangeChanged(position, this.getItemCount());
        updateItems();
    }

    public void addItem(UserInformation u){
        dataSet.add(new UserInformation());
        dataCopy.add(new UserInformation());
        updateItems();
    }


    //Todo --> no idea how to do this...
//    public void addItems(BookInformationPairing newItems){
//        dataSet.addAll(new BookInformationPairing(newItems.getBookList(), newItems.getBookInformationList()));
//        dataCopy.addAll(new BookInformationPairing(newItems.getBookList(), newItems.getBookInformationList()));
//        updateItems();
//    }


    private void updateItems(){
        this.notifyDataSetChanged();
    }

    public UserInformation getUserInformation(int position){
        return dataSet.getUser(position);
    }


    public UserList getDataCopy() {
        return dataCopy;
    }

    public void setDataCopy(UserList dataCopy) {
        this.dataCopy = dataCopy;
    }


    public void setMyListener(RecyclerViewClickListener listener){
        myListener = listener;
    }

    public void setDataSet(UserList data){
        dataSet = new UserList(data.getUserlist());
        updateItems();
    }

    // copied by Felipe on 24-03-2019 from:
    // https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

}
