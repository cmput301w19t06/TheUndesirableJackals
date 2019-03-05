package com.cmput301.w19t06.theundesirablejackals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
 * Created by Kaya on 02/02/2019
 * */
public class AddBookManualFragment extends Fragment {
    View view;
    private Button finalAddbtn;
    private Button photoaddbtn;
    public AddBookManualFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.add_book_manual,container,false);
        finalAddbtn = (Button) view.findViewById(R.id.finalAddBookButton_id);
        photoaddbtn = (Button) view.findViewById(R.id.photoAddButton_id);
        return view;


    }
}
