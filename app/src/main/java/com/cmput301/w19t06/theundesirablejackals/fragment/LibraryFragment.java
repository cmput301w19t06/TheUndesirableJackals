/**
 * The Fragment for displaying all the books available in a database
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
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.activities.ShowBookOwners;
import com.cmput301.w19t06.theundesirablejackals.activities.ViewLibraryBookActivity;
import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookInformationListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

/*
 * Created by Mohamed on 21/02/2019
 */
public class LibraryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    View view;
    private BooksRecyclerViewAdapter libraryRecyclerViewAdapter;
    private SwipeRefreshLayout librarySwipeRefreshLayout;


    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.library_fragment,container,false);

        ItemTouchHelper itemTouchhelper;
        SwipeController swipeController;
        RecyclerView.LayoutManager mainLayoutManager;
        final RecyclerView libraryRecyclerView;



        //Setting up the main page recyclerView using findViewById
        libraryRecyclerView = (RecyclerView) view.findViewById(R.id.libraryRecyclerView);
        librarySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.librarySwipeRefreshLayout);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        libraryRecyclerView.setHasFixedSize(true);

        // use a linear layout manager because the list is easy to display in
        //linear fashion
        mainLayoutManager = new LinearLayoutManager(getContext());
        libraryRecyclerView.setLayoutManager(mainLayoutManager);

        //create a click listener that calls back to here, allows us to
        // create new activities from 'THIS' context without passing 'THIS'
        //into the recyclerView directly.
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Book clickedBook = libraryRecyclerViewAdapter.getBook(position);
                final DatabaseHelper databaseHelper = new DatabaseHelper();
                databaseHelper.getAllBookInformations(clickedBook, new BookInformationListCallback() {
                    @Override
                    public void onCallback(BookInformationList bookInformationList) {
                        if (bookInformationList != null) {

                            if (bookInformationList.size() > 1) {
                                // List all owners of the book
                                Intent intent = new Intent(getActivity(), ShowBookOwners.class);
                                startActivity(intent);

                            } else if (bookInformationList.size() == 0) {
                                // book was added by someone in the past but is now deleted
                                // and no other copies exist in our database
                                ToastMessage.show(getActivity(),
                                        "Book is not owned by anyone at this moment");

                            } else {

                                // If there is only one owner of the book display his/her book

                                Intent intent = new Intent(getActivity(), ViewLibraryBookActivity.class);
                                intent.putExtra(ViewLibraryBookActivity.LIBRARY_BOOK_FROM_RECYCLER_VIEW,
                                                clickedBook);
                                intent.putExtra(ViewLibraryBookActivity.LIBRARY_INFO_FROM_RECYCLER_VIEW,
                                                bookInformationList.get(0));
                                startActivity(intent);
                            }


                        } else {
                            // book was added by someone in the past but is now deleted
                            // and no other copies exist in our database
                            ToastMessage.show(getActivity(),
                                    "Database error");
                        }
                    }
                });
            }
        };

//        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//                Book clickedBook = libraryRecyclerViewAdapter.getBook(position);
//                final DatabaseHelper databaseHelper = new DatabaseHelper();
//                databaseHelper.getAllBookInformations(clickedBook, new BookInformationListCallback() {
//                    @Override
//                    public void onCallback(BookInformationList bookInformationList) {
//                        if(bookInformationList != null){
//                            final BookInformation bookInformation = bookInformationList.get(0);
//                            databaseHelper.getCurrentUserInfoFromDatabase(new UserInformationCallback() {
//                                @Override
//                                public void onCallback(UserInformation userInformation) {
//                                    BookRequest bookRequest = new BookRequest(userInformation, bookInformation);
//                                    databaseHelper.makeBorrowRequest(bookRequest, new BooleanCallback() {
//                                        @Override
//                                        public void onCallback(boolean bool) {
//                                            Toast.makeText(getActivity(), "Library book clicked at " + ((Integer) position).toString(), Toast.LENGTH_LONG).show();
//                                            if(bool){
//                                                Toast.makeText(getActivity(), "Request sent to " + bookInformation.getOwner(), Toast.LENGTH_LONG).show();
//                                                Intent intent = new Intent(getActivity(), ShowBookOwners.class);
////                                                intent.putExtra(ViewOwnedBookActivity.OWNED_BOOK_FROM_RECYCLER_VIEW, clickedBook);
////                                                intent.putExtra(ViewOwnedBookActivity.OWNED_INFO_FROM_RECYCLER_VIEW, clickedbookInformation);
//                                                startActivity(intent);
//                                            }else{
//                                                Toast.makeText(getActivity(), "Request not sent", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//                                    });
//                                }
//                            });
//
//                        }
//                    }
//                });
//                //Do something with the book, maybe view it in detail?
//                Toast.makeText(getActivity(), "Library book clicked at " + ((Integer) position).toString(), Toast.LENGTH_LONG).show();
//            }
//        };

        //create the adapter to manage the data and the recyclerView,
        //give it the above listener
        libraryRecyclerViewAdapter = new BooksRecyclerViewAdapter();
        ((MainHomeViewActivity)getActivity()).setLibraryBooksAdapter(libraryRecyclerViewAdapter);
        libraryRecyclerViewAdapter.setMyListener(listener);
        libraryRecyclerView.setAdapter(libraryRecyclerViewAdapter);
        librarySwipeRefreshLayout.setOnRefreshListener(this);
        librarySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                librarySwipeRefreshLayout.setRefreshing(true);

                // TODO Fetching data from server
                getBooks();
                librarySwipeRefreshLayout.setRefreshing(false);

            }
        });


        //interactivity helpers (touch for edit, swipe for delete)
        swipeController = new SwipeController(libraryRecyclerViewAdapter);
        itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(libraryRecyclerView);


//        getBooks();


        return view;
    }

    /**
     * adds a book in the lstBook by creating an object of LibraryModelClass
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getBooks(){
        //If we got any data from file, add it to the
        //(now finished with setup) recyclerViewAdapter
        final DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getBooksAfterIsbn("0", 100, new BookListCallback() {
            @Override
            public void onCallback(BookList bookList) {
                if(bookList != null && bookList.getBooks() != null && bookList.getBooks().size() > 0) {
                    BookInformationPairing bookInformationPairing = new BookInformationPairing();
                    for (Book book : bookList.getBooks()) {
                        bookInformationPairing.addSingle(book);
                    }
                    libraryRecyclerViewAdapter.setDataSet(bookInformationPairing);
                }
            }
        });

    }

    @Override
    public void onRefresh(){
        librarySwipeRefreshLayout.setRefreshing(true);
        getBooks();
        librarySwipeRefreshLayout.setRefreshing(false);
    }
}
