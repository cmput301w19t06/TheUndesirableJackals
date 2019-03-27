/**
 * Fragment for holding books that an owner borrows from the Library tab
 */

package com.cmput301.w19t06.theundesirablejackals.fragment;

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

import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.activities.ViewBorrowedBookActivity;
import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.util.HashMap;

public class BorrowedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    View view;
    private BooksRecyclerViewAdapter borrowedRecyclerViewAdapter;
    private SwipeRefreshLayout borrowedBoosSwipeRefreshLayout;


    public BorrowedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.borrowed_fragment,container,false);

//        ItemTouchHelper itemTouchhelper;
//        SwipeController swipeController;
        RecyclerView.LayoutManager mainLayoutManager;
        RecyclerView borrowedRecyclerView;


        //Setting up the main page recyclerView using findViewById
        borrowedRecyclerView = (RecyclerView) view.findViewById(R.id.borrowed_recyclerview);
        borrowedBoosSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.borrowedBoosSwipeRefreshLayout);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        borrowedRecyclerView.setHasFixedSize(true);

        // use a linear layout manager because the list is easy to display in
        //linear fashion
        mainLayoutManager = new LinearLayoutManager(getContext());
        borrowedRecyclerView.setLayoutManager(mainLayoutManager);

        //create a click listener that calls back to here, allows us to
        // create new activities from 'THIS' context without passing 'THIS'
        //into the recyclerView directly.
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Book clickedBook = borrowedRecyclerViewAdapter.getBook(position);
                BookInformation clickedbookInformation = borrowedRecyclerViewAdapter.getInformation(position);
                Intent intent = new Intent(getActivity(), ViewBorrowedBookActivity.class);
                intent.putExtra(ViewBorrowedBookActivity.BORROWED_BOOK_FROM_RECYCLER_VIEW, clickedBook);
                intent.putExtra(ViewBorrowedBookActivity.BORROWED_INFO_FROM_RECYCLER_VIEW, clickedbookInformation);
                startActivity(intent);
            }
        };

        //create the adapter to manage the data and the recyclerView,
        //give it the above listener
        borrowedRecyclerViewAdapter = new BooksRecyclerViewAdapter();
        ((MainHomeViewActivity)getActivity()).setBorrowedBooksAdapter(borrowedRecyclerViewAdapter);
        borrowedRecyclerViewAdapter.setMyListener(listener);
        borrowedRecyclerView.setAdapter(borrowedRecyclerViewAdapter);
        borrowedBoosSwipeRefreshLayout.setOnRefreshListener(this);
        borrowedBoosSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                borrowedBoosSwipeRefreshLayout.setRefreshing(true);
                getBooks();
                borrowedBoosSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //interactivity helpers (touch for edit, swipe for delete)
//        swipeController = new SwipeController(borrowedRecyclerViewAdapter);
//        itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(borrowedRecyclerView);


        //If we got any data from file, add it to the
        //(now finished with setup) recyclerViewAdapter


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

    @Override
    public void onRefresh() {
        borrowedBoosSwipeRefreshLayout.setRefreshing(true);
        getBooks();
        borrowedBoosSwipeRefreshLayout.setRefreshing(false);
    }

    private void getBooks(){
        final DatabaseHelper databaseHelper = new DatabaseHelper();
        borrowedRecyclerViewAdapter.setDataSet(new BookInformationPairing());
        borrowedRecyclerViewAdapter.setDataCopy(new BookInformationPairing());
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user != null && user.getBorrowedBooks() != null && user.getBorrowedBooks().getBooks() != null) {
                    final HashMap<String, Object> map = user.getBorrowedBooks().getBooks();
                    if(map.size() > 0) {
                        for (String isbn : map.keySet()) {
                            final String information = map.get(isbn).toString();
                            databaseHelper.getBookFromDatabase(isbn, new BookCallback() {
                                @Override
                                public void onCallback(final Book book) {
                                    databaseHelper.getBookInformation(information, new BookInformationCallback() {
                                        @Override
                                        public void onCallback(BookInformation bookInformation) {
                                            borrowedRecyclerViewAdapter.addItem(book, bookInformation);
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
}
