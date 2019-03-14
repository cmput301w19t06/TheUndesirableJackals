/**
 * The Fragment for displaying all the books available in a database
 */

package com.cmput301.w19t06.theundesirablejackals.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.adapter.BooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.RecyclerViewClickListener;
import com.cmput301.w19t06.theundesirablejackals.adapter.SwipeController;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

import java.util.ArrayList;
import java.util.List;
/*
 * Created by Mohamed on 21/02/2019
 */
public class LibraryFragment extends Fragment {
    View view;
    private BooksRecyclerViewAdapter libraryRecyclerViewAdapter;


    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.library_fragment,container,false);

        ItemTouchHelper itemTouchhelper;
        SwipeController swipeController;
        RecyclerView.LayoutManager mainLayoutManager;
        RecyclerView libraryRecyclerView;



        //Setting up the main page recyclerView using findViewById
        libraryRecyclerView = (RecyclerView) view.findViewById(R.id.library_recyclerview);

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
                Book clickedBook = libraryRecyclerViewAdapter.getItem(position);
                //Do something with the book, maybe view it in detail?
                Toast.makeText(getActivity(), "Library book clicked at " + ((Integer) position).toString(), Toast.LENGTH_LONG).show();

            }
        };

        //create the adapter to manage the data and the recyclerView,
        //give it the above listener
        libraryRecyclerViewAdapter = ((MainHomeViewActivity)getActivity()).getLibraryBooksAdapter();
        libraryRecyclerViewAdapter.setMyListener(listener);
        libraryRecyclerView.setAdapter(libraryRecyclerViewAdapter);

        //interactivity helpers (touch for edit, swipe for delete)
        swipeController = new SwipeController(libraryRecyclerViewAdapter);
        itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(libraryRecyclerView);


        //If we got any data from file, add it to the
        //(now finished with setup) recyclerViewAdapter
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user.getFavouriteBooks() != null) {
                    libraryRecyclerViewAdapter.setDataSet(user.getFavouriteBooks());
                }

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
        //an empty list of books
//        lstBook = new ArrayList<>();
//        lstBook.add(new LibraryModelClass("To kill a Mockingbird", "Harper Lee", "9781545704325","AVAILABLE",R.drawable.book_icon));
        //lstBook.add(new LibraryModelClass("I'll Let You Go", "Bruce Wagner", "9781545073452","AVAILABLE",R.drawable.book_icon));
        //lstBook.add(new LibraryModelClass("Laughing Gas", "P.G Wodehouse", "9781545875658","AVAILABLE",R.drawable.book_icon));


    }
}
