package com.example.starview;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PersonActivity extends AppCompatActivity {

ImageView imageView;
TextView Name, Category, Age, Place, Info;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Bundle arguments = getIntent().getExtras();


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

        imageView = findViewById(R.id.imageView);
        Name = findViewById(R.id.name);
        Category = findViewById(R.id.category);
        Age = findViewById(R.id.age);
        Place = findViewById(R.id.birthPlace);
        Info = findViewById(R.id.Info);
        Star star;

        if(arguments.getString("Name") == null){
            star = (Star) arguments.getSerializable(Star.class.getSimpleName());
            Picasso.get().load(star.getPhoto()).into(imageView);
            Name.setText(star.getName());
            Category.setText(star.getCategory());
            Age.setText(star.getAllAge() + "\n" + star.getAge());

            Cursor cursor = mDb.rawQuery("SELECT Birthplace, Info FROM ListStar WHERE Name ='" + star.getName()+ "'" , null);

            if (cursor != null) { // проверка наличия в бд
                if (cursor.moveToFirst()) { // помещение курсора
                Place.setText(cursor.getString(cursor.getColumnIndex("Birthplace")));
                    Info.setText(cursor.getString(cursor.getColumnIndex("Info")).replace("\\t", "\t"));
                }
            }

            cursor.close();
        }
        else {

            String NameOfStar = arguments.get("Name").toString();
            Cursor cursor = mDb.rawQuery("SELECT Photo, Birthplace, Birth, Category, Info FROM ListStar WHERE Name ='" + NameOfStar + "'" , null);

            if (cursor != null) { // проверка наличия в бд
                if (cursor.moveToFirst()) { // помещение курсора
                    Picasso.get().load(cursor.getString(cursor.getColumnIndex("Photo"))).into(imageView);
                    Name.setText(NameOfStar);
                    Category.setText(cursor.getString(cursor.getColumnIndex("Category")));
                    Place.setText(cursor.getString(cursor.getColumnIndex("Birthplace")));
                    Info.setText(cursor.getString(cursor.getColumnIndex("Info")).replace("\\t", "\t"));
                    Star star1 = new Star(cursor.getString(cursor.getColumnIndex("Birth")));
                    Age.setText(star1.getAllAge() + "\n" + star1.getAge());
                }
            }

            cursor.close();

        }

    }


}