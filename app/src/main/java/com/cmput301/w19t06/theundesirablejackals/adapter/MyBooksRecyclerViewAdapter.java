/**
 * An adapter that connects our data in MyBooksModelClass to our RecyclerView.
 * it determines the Viewholder which we will need to display the data
 */

package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;

import java.util.List;

public class MyBooksRecyclerViewAdapter extends RecyclerView.Adapter<MyBooksRecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    BookList mData;

    /**
     * the adapter class that connects our data to recycler view
     * @param mContext context of the class
     * @param mData pass the data from MyBooksModelClass
     */
    public MyBooksRecyclerViewAdapter(Context mContext, BookList mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    /**
     * initialize view holder
     * @param parent
     * @param viewType
     * @return a view holder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.my_books_item,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    /**
     * Called for each viewholder to bind it to the adapter
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_title.setText(mData.get(position).getTitle());
        holder.tv_author.setText(mData.get(position).getAuthor());
        holder.tv_isbn.setText(mData.get(position).getIsbn());
        holder.tv_status.setText(mData.get(position).getStatus().toString());
//        holder.img_book.setImageResource(mData.get(position).getImg_book());

    }

    /**
     * returns the size of items we want to display
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_author;
        private TextView tv_isbn;
        private TextView tv_status;
        private ImageView img_book;

        /**
         * Sets up views when displaying our data
         * @param itemView a parent view of the item layout
         */
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.myBooks_book_title);
            tv_author = (TextView) itemView.findViewById(R.id.myBooks_author);
            tv_isbn = (TextView) itemView.findViewById(R.id.myBooks_isbn);
            tv_status = (TextView) itemView.findViewById(R.id.myBooks_status);
            img_book = (ImageView) itemView.findViewById(R.id.myBooks_img_book);
        }
    }
}
