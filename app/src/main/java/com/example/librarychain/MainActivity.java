package com.example.librarychain;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SQLiteHelper dbHelper;
    private TableLayout tableLayout;
    private Spinner tableSelector;
    private Button addRowBtn;
    private String selectedTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLiteHelper(this);

        tableLayout = findViewById(R.id.tableLayout);
        tableSelector = findViewById(R.id.tableSelector);
        addRowBtn = findViewById(R.id.addRowBtn);

        // Получение списка имен таблиц
        String[] tableNames = dbHelper.getTableNames();

        // Настройка адаптера для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tableNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSelector.setAdapter(adapter);

        // Обработка выбора элемента в Spinner
        tableSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Получение выбранной таблицы
                selectedTableName = tableSelector.getSelectedItem().toString();
                System.out.println(selectedTableName);
                if (LoginActivity.userIsAdmin) {
                    addRowBtn.setEnabled(true);
                } else {
                    addRowBtn.setEnabled(false);
                }

                // Очистка текущего содержимого TableLayout
                tableLayout.removeAllViews();

                if (selectedTableName.equals("учет")) {
                    addRowBtn.setText("Обновить запись");
                    addRowBtn.setEnabled(true);
                } else {
                    addRowBtn.setText("Добавить запись");
                }

                // Загрузка данных выбранной таблицы
                readDataFromTable(selectedTableName, tableLayout);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ничего не делаем, если ничего не выбрано
            }
        });

        addRowBtn.setOnClickListener(view -> {
            openAddRowActivity();
        });

        // Загрузка данных по умолчанию (например, первой таблицы)
        if (tableNames.length > 0) {
            selectedTableName = tableNames[0];
            readDataFromTable(tableNames[0], tableLayout);
        }
    }

    private void readDataFromTable(String tableName, TableLayout tableLayout) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        if (cursor.moveToFirst()) {
            // Создаем заголовок таблицы
            TableRow headerRow = new TableRow(this);
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                addHeaderToRow(headerRow, columnName);
            }
            tableLayout.addView(headerRow);

            do {
                // Создаем строку с данными
                TableRow dataRow = new TableRow(this);
                for (String columnName : columnNames) {
                    String data = cursor.getString(cursor.getColumnIndex(columnName));
                    addDataToRow(dataRow, data);
                }
                tableLayout.addView(dataRow);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    private void addHeaderToRow(TableRow row, String header) {
        TextView textView = new TextView(this);
        textView.setText(header);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }

    private void addDataToRow(TableRow row, String data) {
        TextView textView = new TextView(this);
        textView.setText(data);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }

    private void openAddRowActivity() {
        Intent intent;
        switch (selectedTableName) {
            case "магазины" : {
                intent = new Intent(this, StoreAddActivity.class);
                startActivity(intent);
                break;
            }
            case "книги": {
                intent = new Intent(this, BookAddActivity.class);
                startActivity(intent);
                break;
            }
            case "учет": {
                intent = new Intent(this, InventoryAddActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void refreshTable(String tableName) {
        tableLayout.removeAllViews(); // Очищаем текущие данные в таблице
        readDataFromTable(tableName, tableLayout); // Заново читаем данные из таблицы и добавляем их в таблицу
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Обновляем таблицу каждый раз, когда активити становится видимым
        refreshTable(selectedTableName);
    }



    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
