package com.cmput301.w19t06.theundesirablejackals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301.w19t06.theundesirablejackals.activities.R;

/**
 * The ISBN reader segment of Addbook --> links to the actual reader
 * Author:Kaya Thiessen
 */
public class AddBookCameraFragment extends Fragment {
    View view;
    public AddBookCameraFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.add_book_camera,container,false);
        return view;
    }
}

