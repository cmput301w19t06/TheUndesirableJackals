package com.cmput301.w19t06.theundesirablejackals;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
* Created by Mohamed on 21/02/2019
* */
public class MyBooksFragment extends Fragment {
    View view;
    public MyBooksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.my_books_fragment,container,false);
        return view;
    }
}
