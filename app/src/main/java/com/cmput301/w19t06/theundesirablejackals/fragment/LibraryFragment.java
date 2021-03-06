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

import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.activities.ShowBookOwnersActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.ViewLibraryBookActivity;
import com.cmput301.w19t06.theundesirablejackals.adapter.BookInformationPairing;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformationList;
import com.cmput301.w19t06.theundesirablejackals.book.BookList;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookInformationListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

/**
 * Fragment to show library books. Library books are considered as books that are added to the app
 * by users, either by scanning or manually entering details about a book
 * @author Mohammed
 * @author Devon Deweert
 * @see BooksRecyclerViewAdapter
 */
public class LibraryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener{
    public View view;
    private BooksRecyclerViewAdapter libraryRecyclerViewAdapter;
    private SwipeRefreshLayout librarySwipeRefreshLayout;


    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.library_fragment,container,false);

        RecyclerView.LayoutManager mainLayoutManager;
        RecyclerView libraryRecyclerView;

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



        //create the adapter to manage the data and the recyclerView,
        //give it the above listener
        libraryRecyclerViewAdapter = new BooksRecyclerViewAdapter();
        ((MainHomeViewActivity)getActivity()).setLibraryBooksAdapter(libraryRecyclerViewAdapter);
        //create a click listener that calls back to here, allows us to
        // create new activities from 'THIS' context without passing 'THIS'
        //into the recyclerView directly.
        libraryRecyclerViewAdapter.setMyListener(this);
        libraryRecyclerView.setAdapter(libraryRecyclerViewAdapter);
        librarySwipeRefreshLayout.setOnRefreshListener(this);
        librarySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                librarySwipeRefreshLayout.setRefreshing(true);
                getBooks();
                librarySwipeRefreshLayout.setRefreshing(false);

            }
        });


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

    /**
     * Uses Firebase to load in all the books and adding each of them to the recycler view
     */
    private void getBooks(){
        //If we got any data from file, add it to the
        //(now finished with setup) recyclerViewAdapter
        libraryRecyclerViewAdapter.setDataSet(new BookInformationPairing());
        libraryRecyclerViewAdapter.setDataCopy(new BookInformationPairing());
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
                    libraryRecyclerViewAdapter.setDataCopy(bookInformationPairing);
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


    @Override
    public void onClick(final View view, int position) {
        view.setClickable(false);
        final Book clickedBook = libraryRecyclerViewAdapter.getBook(position);
        final DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getAllBookInformations(clickedBook, new BookInformationListCallback() {
            @Override
            public void onCallback(BookInformationList bookInformationList) {
                if (bookInformationList != null) {

                    if (bookInformationList.size() > 1) {
                        // List all owners of the book
                        Intent intent = new Intent(getActivity(), ShowBookOwnersActivity.class);
                        intent.putExtra(ShowBookOwnersActivity.BOOK_OBJECT, clickedBook);
                        intent.putExtra(ShowBookOwnersActivity.LIST_OF_OWNERS, bookInformationList);
                        startActivity(intent);
                        view.setClickable(true);

                    } else if (bookInformationList.size() == 0) {
                        // book was added by someone in the past but is now deleted
                        // and no other copies exist in our database
                        ToastMessage.show(getActivity(),
                                "This book is not owned by any users.");
                        view.setClickable(true);

                    } else {

                        // If there is only one owner of the book and the book info status
                        // is either available or requested display his/her book
                        if (bookInformationList.get(0).getStatus().equals(BookStatus.AVAILABLE)
                                || bookInformationList.get(0).getStatus().equals(BookStatus.REQUESTED)) {

                            Intent intent = new Intent(getActivity(), ViewLibraryBookActivity.class);
                            intent.putExtra(ViewLibraryBookActivity.LIBRARY_BOOK_FROM_RECYCLER_VIEW,
                                    clickedBook);
                            intent.putExtra(ViewLibraryBookActivity.LIBRARY_INFO_FROM_RECYCLER_VIEW,
                                    bookInformationList.get(0));
                            startActivity(intent);
                            view.setClickable(true);

                        } else {
                            ToastMessage.show(getActivity(), "Book is currently unavailable.");
                            view.setClickable(true);
                        }


                    }


                } else {
                    // book was added by someone in the past but is now deleted
                    // and no other copies exist in our database
                    ToastMessage.show(getActivity(),
                            "Database error");
                    view.setClickable(true);
                }
            }
        });
    }
}
