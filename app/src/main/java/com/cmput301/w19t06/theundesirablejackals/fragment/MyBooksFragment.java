package com.cmput301.w19t06.theundesirablejackals.fragment;
/**
 * Fragment to show the books a user owns
 * The user can add new books and check the book's status
 * @see MyBookModdelClass, MyBooksRecyclerViewAdapter
 */



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.adapter.MyBooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.MyBooksModelClass;
import com.cmput301.w19t06.theundesirablejackals.R;

import java.util.ArrayList;
import java.util.List;

/*
* Created by Mohamed on 21/02/2019
 */
public class MyBooksFragment extends Fragment {
    private Button addBookButton;
    View v ;
    private RecyclerView myBooksRecyclerView;
    private List<MyBooksModelClass> lstBook;
    public MyBooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.my_books_fragment,container,false);

        //create and Handle Add book Button
        addBookButton = (Button) view.findViewById(R.id.addBook_button);
        myBooksRecyclerView = (RecyclerView) view.findViewById(R.id.myBooks_recyclerview);
        myBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyBooksRecyclerViewAdapter myBooksRecyclerViewAdapter = new MyBooksRecyclerViewAdapter(getContext(), lstBook);
        myBooksRecyclerView.setAdapter(myBooksRecyclerViewAdapter);

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
        lstBook = new ArrayList<>();
        lstBook.add(new MyBooksModelClass("To kill a Mockingbird", "Harper Lee", "9781545704325","AVAILABLE",R.drawable.book_icon));
        lstBook.add(new MyBooksModelClass("I'll Let You Go", "Bruce Wagner", "9781545073452","AVAILABLE",R.drawable.book_icon));
        lstBook.add(new MyBooksModelClass("Laughing Gas", "P.G Wodehouse", "9781545875658","AVAILABLE",R.drawable.book_icon));
    }
}
