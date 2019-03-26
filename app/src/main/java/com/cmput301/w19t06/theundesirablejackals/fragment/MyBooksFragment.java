package com.cmput301.w19t06.theundesirablejackals.fragment;
/**
 * Fragment to show the books a user owns
 * The user can add new books and check the book's status
 * @see MyBookModdelClass, MyBooksRecyclerViewAdapter
 */



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.ViewOwnedBookActivity;
import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.util.HashMap;

/*
* Created by Mohamed on 21/02/2019
 */
public class MyBooksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private Button addBookButton;
    private View view;
    private BooksRecyclerViewAdapter booksRecyclerViewAdapter;
    private SwipeRefreshLayout ownedBooksSwipeRefreshLayout;

    public MyBooksFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_books_fragment,container,false);

        ItemTouchHelper itemTouchhelper;
        SwipeController swipeController;
        RecyclerView.LayoutManager mainLayoutManager;
        final RecyclerView booksRecyclerView;

        //Setting up the main page recyclerView using findViewById
        booksRecyclerView = (RecyclerView) view.findViewById(R.id.myBooks_recyclerview);
        ownedBooksSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ownedBooksSwipeRefreshLayout);

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
                Book clickedBook = booksRecyclerViewAdapter.getBook(position);
                BookInformation clickedbookInformation = booksRecyclerViewAdapter.getInformation(position);
                Intent intent = new Intent(getActivity(), ViewOwnedBookActivity.class);
                intent.putExtra(ViewOwnedBookActivity.OWNED_BOOK_FROM_RECYCLER_VIEW, clickedBook);
                intent.putExtra(ViewOwnedBookActivity.OWNED_INFO_FROM_RECYCLER_VIEW, clickedbookInformation);
                startActivity(intent);

            }
        };

        //create the adapter to manage the data and the recyclerView,
        //give it the above listener
        booksRecyclerViewAdapter = new BooksRecyclerViewAdapter();
        ((MainHomeViewActivity)getActivity()).setOwnedBooksAdapter(booksRecyclerViewAdapter);
        booksRecyclerViewAdapter.setMyListener(listener);
        booksRecyclerView.setAdapter(booksRecyclerViewAdapter);
        ownedBooksSwipeRefreshLayout.setOnRefreshListener(this);
        ownedBooksSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                ownedBooksSwipeRefreshLayout.setRefreshing(true);
                getBooks();
                ownedBooksSwipeRefreshLayout.setRefreshing(false);

            }
        });

        //interactivity helpers (touch for edit, swipe for delete)
        swipeController = new SwipeController(booksRecyclerViewAdapter);
        itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(booksRecyclerView);


        return view;

    }

    /**
     * adds a book in the lstBook by creating an object of MyBooksModelClass
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void getBooks(){
        final DatabaseHelper databaseHelper = new DatabaseHelper();
        booksRecyclerViewAdapter.setDataSet(new BookInformationPairing());
        booksRecyclerViewAdapter.setDataCopy(new BookInformationPairing());
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user != null && user.getOwnedBooks() != null && user.getOwnedBooks().getBooks() != null) {
                    final HashMap<String, Object> map = user.getOwnedBooks().getBooks();
                    if(map.size() > 0) {
                        for (String isbn : map.keySet()) {
                            final String information = map.get(isbn).toString();
                            databaseHelper.getBookFromDatabase(isbn, new BookCallback() {
                                @Override
                                public void onCallback(final Book book) {
                                    databaseHelper.getBookInformation(information, new BookInformationCallback() {
                                        @Override
                                        public void onCallback(BookInformation bookInformation) {
                                            booksRecyclerViewAdapter.addItem(book, bookInformation);
                                        }
                                    });
                                }
                            });
                        }
                    }

                }

            }
        });
    }


    @Override
    public void onRefresh(){
        ownedBooksSwipeRefreshLayout.setRefreshing(true);
        getBooks();
        ownedBooksSwipeRefreshLayout.setRefreshing(false);
    }
}
