package com.example.librarychain;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookAddActivity extends AppCompatActivity {
    private EditText titleEditText, authorEditText, genreEditText, priceEditText;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        dbHelper = new SQLiteHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        genreEditText = findViewById(R.id.genreEditText);
        priceEditText = findViewById(R.id.priceEditText);

        Button addButton = findViewById(R.id.addBookButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook();
            }
        });
    }

    private void addBook() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("название", titleEditText.getText().toString());
        values.put("автор", authorEditText.getText().toString());
        values.put("жанр", genreEditText.getText().toString());
        values.put("цена", Double.parseDouble(priceEditText.getText().toString()));

        long newRowId = db.insert("книги", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Книга добавлена успешно!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при добавлении книги", Toast.LENGTH_SHORT).show();
        }

        db.close();
        finish(); // закрыть активити после добавления
    }
}
