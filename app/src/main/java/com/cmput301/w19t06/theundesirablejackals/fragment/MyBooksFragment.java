package com.cmput301.w19t06.theundesirablejackals.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.R;

/*
* Created by Mohamed on 21/02/2019
* */
public class MyBooksFragment extends Fragment {
    private Button addBookButton;
    public MyBooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.my_books_fragment,container,false);

        //create and Handle Add book Button
        addBookButton = (Button) view.findViewById(R.id.addBook_button);
        return view;
    }

}
