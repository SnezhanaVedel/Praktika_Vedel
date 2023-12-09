package com.example.librarychain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class InventoryAddActivity extends AppCompatActivity {
    private Spinner bookSpinner, storeSpinner;
    private EditText quantityEditText;
    private TextView quantityInfoTextView;
    private RadioGroup operationRadioGroup; // Новая переменная для RadioGroup
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add);

        dbHelper = new SQLiteHelper(this);

        bookSpinner = findViewById(R.id.bookSpinner);
        storeSpinner = findViewById(R.id.storeSpinner);
        quantityEditText = findViewById(R.id.quantityEditText);
        quantityInfoTextView = findViewById(R.id.quantityInfoTextView);
        operationRadioGroup = findViewById(R.id.operationRadioGroup); // Инициализация RadioGroup

        // Заполнение Spinner данными из таблиц "книги" и "магазины"
        populateSpinner("книги", bookSpinner);
        populateSpinner("магазины", storeSpinner);

        // Обработчик события выбора элемента в Spinner для обновления информации о количестве книг
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                updateQuantityInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Ничего не делаем в этом случае
            }
        });

        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                updateQuantityInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Ничего не делаем в этом случае
            }
        });

        Button addButton = findViewById(R.id.addInventoryButton);
        addButton.setOnClickListener(view -> addInventory());
    }

    private void populateSpinner(String tableName, Spinner spinner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        List<String> items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                if (tableName.equals("книги")) {
                    String itemName = cursor.getString(cursor.getColumnIndex("название"));
                    items.add(itemName);
                } else {
                    String itemName = cursor.getString(cursor.getColumnIndex("адрес"));
                    items.add(itemName);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateQuantityInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectedBook = bookSpinner.getSelectedItem().toString();
        String selectedStore = storeSpinner.getSelectedItem().toString();

        Cursor cursor = db.rawQuery("SELECT количество_книг FROM учет WHERE название_книги = ? AND адрес_магазина = ?",
                new String[]{selectedBook, selectedStore});

        int quantity = 0;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex("количество_книг"));
        }

        quantityInfoTextView.setText("Количество книг в магазине: " + quantity);

        cursor.close();
        db.close();
    }

    private void addInventory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selectedBook = bookSpinner.getSelectedItem().toString();
        String selectedStore = storeSpinner.getSelectedItem().toString();
        int quantityChange = Integer.parseInt(quantityEditText.getText().toString());

        // Получаем выбранную радиокнопку
        RadioButton selectedOperationButton = findViewById(operationRadioGroup.getCheckedRadioButtonId());
        String selectedOperation = selectedOperationButton.getText().toString();

        // Проверяем, существует ли запись с выбранным сочетанием книга-магазин
        Cursor cursor = db.rawQuery("SELECT * FROM учет WHERE название_книги = ? AND адрес_магазина = ?",
                new String[]{selectedBook, selectedStore});

        if (cursor.moveToFirst()) {
            // Если запись существует, обновляем количество книг в ней в зависимости от операции
            int currentQuantity = cursor.getInt(cursor.getColumnIndex("количество_книг"));

            if (selectedOperation.equals("Поступление")) {
                currentQuantity += quantityChange;
            } else if (selectedOperation.equals("Продажа")) {
                currentQuantity -= quantityChange;
//                System.out.println(currentQuantity);
                if (currentQuantity < 0) {

                    Toast.makeText(this, "Недостаточно книг в наличии", Toast.LENGTH_LONG).show();
                    return;
                }
////                 Проверка на минимальное количество книг (0)
//                currentQuantity = Math.max(currentQuantity, 0);
            }

            ContentValues updateValues = new ContentValues();
            updateValues.put("количество_книг", currentQuantity);

            int rowsAffected = db.update("учет", updateValues,
                    "название_книги = ? AND адрес_магазина = ?",
                    new String[]{selectedBook, selectedStore});

            if (rowsAffected > 0) {
                Toast.makeText(this, "Запись в учете обновлена успешно!", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                finish(); // закрыть активити после добавления
            } else {
                Toast.makeText(this, "Ошибка при обновлении записи в учете", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Если запись не существует, создаем новую запись
            ContentValues insertValues = new ContentValues();


            // Устанавливаем количество книг в зависимости от операции
            if (selectedOperation.equals("Поступление")) {
                insertValues.put("название_книги", selectedBook);
                insertValues.put("адрес_магазина", selectedStore);
                insertValues.put("количество_книг", quantityChange);

                long newRowId = db.insert("учет", null, insertValues);

                if (newRowId != -1) {
                    Toast.makeText(this, "Запись в учете добавлена успешно!", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    db.close();
                    finish(); // закрыть активити после добавления
                } else {
                    Toast.makeText(this, "Ошибка при добавлении записи в учет", Toast.LENGTH_SHORT).show();
                }
            } else if (selectedOperation.equals("Продажа")) {
                if (quantityChange < 0) {
                    Toast.makeText(this, "Недостаточно книг в наличии", Toast.LENGTH_LONG).show();
                    return;
                }
                // Проверка на минимальное количество книг (0)
//                insertValues.put("количество_книг", quantityChange);
            }


        }


    }
}
