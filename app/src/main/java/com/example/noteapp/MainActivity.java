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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClick {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DataBaseHelper dataBaseHelper;
    ArrayList<NoteData> noteDataList;
    NoteAdapter adapter;
    ImageView changeOrder, darkNightMode;
    Boolean isLinear = true;
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
        adapter = new NoteAdapter(noteDataList, MainActivity.this, this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


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

        //setting recycler view adapter
        recyclerView.setAdapter(adapter);

        //for changing the order of layout from linear to StaggeredGridLayoutManager and vice virsa
        changeOrder.setImageResource(R.drawable.grid_view_icon);

        changeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLinear) {
                    isLinear = false;
                    changeOrder.setImageResource(R.drawable.linear_view_icon);
                    changeOrder.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.textColor));
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                } else {
                    isLinear = true;
                    changeOrder.setImageResource(R.drawable.grid_view_icon);
                    changeOrder.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.textColor));

                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
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
                filter(s.toString());//calling filter methhod to filter note from list
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();// fetching data onResume method so that view is updated with new note
    }

    //this method fetch all note from database
    protected void getData() {
        noteDataList.clear();
//        fetching data from data base by calling method of DataBaseHelper class and showing to mainActivity initially
        noteDataList = dataBaseHelper.fetchData();
        Collections.reverse(noteDataList);
        adapter.updateNoteList(noteDataList);
        adapter.notifyDataSetChanged();

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

    // delete method of itemClick interface for deleting note
    @Override
    public void delete(int id) {

        dataBaseHelper.deleteNote(id);

    }


}