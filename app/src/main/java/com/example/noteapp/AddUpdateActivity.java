package com.example.noteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddUpdateActivity extends AppCompatActivity {
    EditText contentEditText;
    EditText tittleEditText;
    ImageView btn, btnShare, btnBack, btnDelete;
    DataBaseHelper dataBaseHelper;
    TextView noteHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        contentEditText = findViewById(R.id.noteContent);
        btn = findViewById(R.id.btnSave);
        tittleEditText = findViewById(R.id.noteTitle);
        noteHeading = findViewById(R.id.noteHeading);
        btnShare = findViewById(R.id.btnShare);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        dataBaseHelper = new DataBaseHelper(this);


        //creating and initializing intent
        Intent intent = getIntent();
        //getting values from passed intent
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");


        //checking title and content of note if its not null that means
        //it came for the update else create
        if (title != null && content != null) {

            //setting previeous value of note
            noteHeading.setText("Update Note.");
            contentEditText.setText(content);
            tittleEditText.setText(title);

            //updating the new values
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String updatedTitle = tittleEditText.getText().toString().trim();
                    String updatedContent = contentEditText.getText().toString().trim();

                    dataBaseHelper.updateNote(id, updatedTitle, updatedContent);
                    finish();

                }
            });
        } else {
            //creating new note
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String contentStr = contentEditText.getText().toString().trim();
                    String titleStr = tittleEditText.getText().toString().trim();
                    if (!contentStr.equals("") && !titleStr.equals("")) {
                        dataBaseHelper.addNote(titleStr, contentStr);

                        finish();

                    } else {
                        Toast.makeText(AddUpdateActivity.this, "Please add the Title and Content of Note", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

//for sharing note data
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String noteTitle = tittleEditText.getText().toString();
                String noteContent = contentEditText.getText().toString();

                if (!noteTitle.isEmpty() && !noteContent.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, noteTitle);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, noteContent);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));

                } else {
                    Toast.makeText(AddUpdateActivity.this, "can not share", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //for deleting note
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showing dialog box for confirm delete
                new AlertDialog.Builder(AddUpdateActivity.this)
                        .setMessage("Are you sure you want to delete this note?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dataBaseHelper.deleteNote(id);
                                Intent backIntent = new Intent(AddUpdateActivity.this, MainActivity.class);
                                backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(backIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();

            }
        });


//        for back btn
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}