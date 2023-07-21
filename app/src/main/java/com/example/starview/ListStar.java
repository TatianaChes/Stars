package com.example.starview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListStar extends AppCompatActivity {
    ArrayList<Star> stars = new ArrayList<Star>();
    // Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    TextInputEditText search;
    StarAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_star);
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        // начальная инициализация списка
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);

// определяем слушателя нажатия элемента в списке
        StarAdapter.OnStarClickListener starClickListener = new StarAdapter.OnStarClickListener() {
            @Override
            public void onStarClick(Star star, int position) {

                Intent intent = new Intent(ListStar.this, PersonActivity.class);
                intent.putExtra(Star.class.getSimpleName(), star);
                startActivity(intent);
              /*  Toast.makeText(getApplicationContext(), "Был выбран пункт " + star.getName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        };

         search = findViewById(R.id.Search);
         search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        // создаем адаптер
       adapter = new StarAdapter(this, stars, starClickListener);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Star> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Star s : stars) {
            //if the existing elements contains the search input
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }
    private void setInitialData(){


        Cursor cursor = mDb.rawQuery("SELECT * FROM ListStar", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            stars.add(new Star (cursor.getString(3), cursor.getString(1), cursor.getString(4), cursor.getString(5)));
            cursor.moveToNext();
        }
        cursor.close();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListStar.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}