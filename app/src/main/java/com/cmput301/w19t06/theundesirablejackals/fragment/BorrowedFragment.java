/**
 * Fragment for holding books that an owner borrows from the Library tab
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

import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.adapter.BorrowedRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.adapter.MyBooksRecyclerViewAdapter;
import com.cmput301.w19t06.theundesirablejackals.classes.BorrowedModelClass;

import java.util.ArrayList;
import java.util.List;

public class BorrowedFragment extends Fragment {
    View view;
    private RecyclerView borrowedRecyclerView;
    private List<BorrowedModelClass> lstBook;
    public BorrowedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.borrowed_fragment,container,false);
        borrowedRecyclerView = (RecyclerView) view.findViewById(R.id.borrowed_recyclerview);
        borrowedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BorrowedRecyclerViewAdapter borrowedRecyclerViewAdapter = new BorrowedRecyclerViewAdapter(getContext(), lstBook);
        borrowedRecyclerView.setAdapter(borrowedRecyclerViewAdapter);
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
        lstBook.add(new BorrowedModelClass("Moby Dick", " Herman Melville", "978-1-509816-14-9","AVAILABLE",R.drawable.book_icon));
        lstBook.add(new BorrowedModelClass("Ulysses", "James Joyce", "978-1-891830-75-4","AVAILABLE",R.drawable.book_icon));
        lstBook.add(new BorrowedModelClass(" In Search of Lost Time", " Marcel Proust", "978-1-60309-025-4","AVAILABLE",R.drawable.book_icon));
    }
}
