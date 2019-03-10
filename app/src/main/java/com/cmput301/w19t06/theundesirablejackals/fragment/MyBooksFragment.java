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
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.adapter.MyBooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.book.Book;
//import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.activities.R;

import java.util.ArrayList;
import java.util.List;

/*
* Created by Mohamed on 21/02/2019
* */
public class MyBooksFragment extends Fragment {
    private Button addBookButton;
    View v ;
    private RecyclerView myBooksRecyclerView;
    private List<Book> lstBook;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstBook = new ArrayList<>();
        lstBook.add(new Book("To kill a Mockingbird", "Harper Lee", "9781545704325"));
        lstBook.add(new Book("Hello World", "Franky Johnson", "9781545703452"));
        lstBook.add(new Book("How to pet a Jackal", "Kaya", "9781545704325"));
    }
}
