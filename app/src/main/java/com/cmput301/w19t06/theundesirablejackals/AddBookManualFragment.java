package com.cmput301.w19t06.theundesirablejackals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmput301.w19t06.theundesirablejackals.activities.R;

/**
 * The manual entry for add a book, fragment that loads when AddBookActivity is called
 * Author: Kaya Thiessen
 */
public class AddBookManualFragment extends Fragment {
    View view;
    private Button buttonAddBook;
    private Button buttonAddPhoto;
    public AddBookManualFragment() {
    }

    /**
     * on create view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.add_book_manual,container,false);
        buttonAddBook = (Button) view.findViewById(R.id.buttonAddBookActivityAddBook);
        buttonAddPhoto = (Button) view.findViewById(R.id.buttonAddBookActivityAddPhotos);
        return view;
    }
}
