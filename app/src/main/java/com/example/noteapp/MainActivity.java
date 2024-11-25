package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClick {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DataBaseHelper dataBaseHelper;
    ArrayList<NoteData> noteDataList;
    NoteAdapter adapter;
    ImageView changeOrder, darkNightMode;
    Boolean isLinear = false;
    EditText searchView;
    boolean isDarkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the views
        recyclerView = findViewById(R.id.recycler_view);
        floatingActionButton = findViewById(R.id.add_note);
        changeOrder = findViewById(R.id.changeOrder);
        searchView = findViewById(R.id.searchView);
        darkNightMode = findViewById(R.id.switchBtn);
        dataBaseHelper = new DataBaseHelper(this);
        noteDataList = new ArrayList<>();


        //dark and night mode
        darkNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDarkMode) {
                    isDarkMode = false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                } else {
                    isDarkMode = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }


            }
        });

        //fetching data  initialy and set on view
        getData();

        //for changing the order of layout from linear to StaggeredGridLayoutManager and vice virsa
        changeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLinear) {
                    isLinear = false;
                    changeOrder.setImageResource(R.drawable.grid_view_icon);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                } else {
                    isLinear = true;
                    changeOrder.setImageResource(R.drawable.linear_view_icon);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }
        });

        //button for creating note
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUpdateActivity.class);
                startActivity(intent);

            }
        });

        // for searching note
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());//calling filter methhod to filter data from list
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();// fetching data onResume method so that view is updated with new item

    }


    //this method fetch all note from database
    protected void getData() {


//        fetching data from data base by calling method of DBhelper class and showing to mainActivity initially
        noteDataList = dataBaseHelper.fetchData();

        //setting recycler view adapter
        adapter = new NoteAdapter(noteDataList, MainActivity.this, this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

    }

    //search for
    private void filter(String query) {
        List<NoteData> filteredList = new ArrayList<>();
        for (NoteData data : noteDataList) {
            if (data.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(data);
            } else if (data.getContent().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(data);
            } else if (data.getDate().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(data);
            }
        }
        adapter.filterList(filteredList); //calling method of adapter class
    }


    // delete method of itemClick iterface for deleting note
    @Override
    public void delete(int id) {

        dataBaseHelper.deleteNote(id);

    }


}