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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.adapter.LibraryRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.LibraryModelClass;

import java.util.ArrayList;
import java.util.List;
/*
 * Created by Mohamed on 21/02/2019
 */
public class LibraryFragment extends Fragment {
    View view;
    private RecyclerView libraryRecyclerView;
    private List<LibraryModelClass> lstBook;
    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.library_fragment,container,false);
        libraryRecyclerView = (RecyclerView) view.findViewById(R.id.library_recyclerview);
        libraryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LibraryRecyclerViewAdapter libraryRecyclerViewAdapter = new LibraryRecyclerViewAdapter(getContext(), lstBook);
        libraryRecyclerView.setAdapter(libraryRecyclerViewAdapter);
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
        lstBook = new ArrayList<>();
        lstBook.add(new LibraryModelClass("To kill a Mockingbird", "Harper Lee", "9781545704325","AVAILABLE",R.drawable.book_icon));
        //lstBook.add(new LibraryModelClass("I'll Let You Go", "Bruce Wagner", "9781545073452","AVAILABLE",R.drawable.book_icon));
        //lstBook.add(new LibraryModelClass("Laughing Gas", "P.G Wodehouse", "9781545875658","AVAILABLE",R.drawable.book_icon));
    }
}
