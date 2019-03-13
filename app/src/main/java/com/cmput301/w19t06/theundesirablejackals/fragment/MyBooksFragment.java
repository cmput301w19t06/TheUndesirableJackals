package com.cmput301.w19t06.theundesirablejackals.fragment;
/**
 * Fragment to show the books a user owns
 * The user can add new books and check the book's status
 * @see MyBookModdelClass, MyBooksRecyclerViewAdapter
 */



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.util.ArrayList;
import java.util.List;

/*
* Created by Mohamed on 21/02/2019
 */
public class MyBooksFragment extends Fragment {
    private Button addBookButton;
    private View view;
    private BooksRecyclerViewAdapter booksRecyclerViewAdapter;

    public MyBooksFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_books_fragment,container,false);

        ItemTouchHelper itemTouchhelper;
        SwipeController swipeController;
        RecyclerView.LayoutManager mainLayoutManager;
        RecyclerView booksRecyclerView;

        //create and Handle Add book Button
        addBookButton = (Button) view.findViewById(R.id.addBook_button);



        //Setting up the main page recyclerView using findViewById
        booksRecyclerView = (RecyclerView) view.findViewById(R.id.myBooks_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        booksRecyclerView.setHasFixedSize(true);

        // use a linear layout manager because the list is easy to display in
        //linear fashion
        mainLayoutManager = new LinearLayoutManager(getContext());
        booksRecyclerView.setLayoutManager(mainLayoutManager);

        //create a click listener that calls back to here, allows us to
        // create new activities from 'THIS' context without passing 'THIS'
        //into the recyclerView directly.
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Book clickedBook = booksRecyclerViewAdapter.getItem(position);
                //Do something with the book, maybe view it in detail?
                Toast.makeText(getActivity(), "Owned book clicked at " + ((Integer) position).toString(), Toast.LENGTH_LONG).show();


            }
        };

        //create the adapter to manage the data and the recyclerView,
        //give it the above listener
        booksRecyclerViewAdapter = new BooksRecyclerViewAdapter(listener);
        booksRecyclerView.setAdapter(booksRecyclerViewAdapter);

        //interactivity helpers (touch for edit, swipe for delete)
        swipeController = new SwipeController(booksRecyclerViewAdapter);
        itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(booksRecyclerView);


        //If we got any data from file, add it to the
        //(now finished with setup) recyclerViewAdapter
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user.getOwnedBooks() != null) {
                    booksRecyclerViewAdapter.addItems(user.getOwnedBooks());
                }

            }
        });

        return view;

    }

    /**
     * adds a book in the lstBook by creating an object of MyBooksModelClass
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //an empty list of books
//        lstBook = new ArrayList<>();
//        lstBook.add(new MyBooksModelClass("To kill a Mockingbird", "Harper Lee", "9781545704325","AVAILABLE",R.drawable.book_icon));
//        lstBook.add(new MyBooksModelClass("I'll Let You Go", "Bruce Wagner", "9781545073452","AVAILABLE",R.drawable.book_icon));
//        lstBook.add(new MyBooksModelClass("Laughing Gas", "P.G Wodehouse", "9781545875658","AVAILABLE",R.drawable.book_icon));
    }
}
