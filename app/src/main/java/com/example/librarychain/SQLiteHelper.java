package com.example.librarychain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "your_database.db";
    private static final int DATABASE_VERSION = 1;

    // Конструктор
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Метод создания базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы магазины
        db.execSQL("CREATE TABLE магазины (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "адрес TEXT UNIQUE, " +
                "телефон TEXT, " +
                "электронная_почта TEXT, " +
                "рабочие_часы TEXT);");

        // Создание таблицы книги
        db.execSQL("CREATE TABLE книги (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "название TEXT UNIQUE, " +
                "автор TEXT, " +
                "жанр TEXT, " +
                "цена REAL);");

        // Создание таблицы учет
        db.execSQL("CREATE TABLE учет (" +
                "название_книги TEXT," +
                "адрес_магазина TEXT," +
                "количество_книг INTEGER," +
                "PRIMARY KEY (название_книги, адрес_магазина)," +
                "FOREIGN KEY (название_книги) REFERENCES книги(название)," +
                "FOREIGN KEY (адрес_магазина) REFERENCES магазины(адрес)" +
                ");");


        db.execSQL(insertIntoStores);
        db.execSQL(insertIntoBooks);
        db.execSQL(insertIntoInventory);
    }

    // Метод обновления базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных при изменении версии
        // (в данном примере - просто удаление и создание заново)
        db.execSQL("DROP TABLE IF EXISTS магазины");
        db.execSQL("DROP TABLE IF EXISTS книги");
        db.execSQL("DROP TABLE IF EXISTS учет");
        onCreate(db);
    }

    public String[] getTableNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%' AND name NOT LIKE 'sqlite_%'", null);

        List<String> tableNamesList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(cursor.getColumnIndex("name"));
                tableNamesList.add(tableName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tableNamesList.toArray(new String[0]);
    }


    private final String insertIntoStores =
            "INSERT INTO магазины (адрес, телефон, электронная_почта, рабочие_часы) VALUES " +
                    "('ул. Пушкина, 10', '123-456-7890', 'info@bookstore.com', '9:00 - 18:00'), " +
                    "('пр. Ленина, 20', '234-567-8901', 'contact@bookstore.com', '10:00 - 19:00'), " +
                    "('ул. Гоголя, 30', '345-678-9012', 'gogol@bookstore.com', '9:00 - 18:00'), " +
                    "('ул. Чехова, 40', '456-789-0123', 'chekhov@bookstore.com', '10:00 - 19:00'), " +
                    "('пр. Мира, 50', '567-890-1234', 'mir@bookstore.com', '9:00 - 20:00'), " +
                    "('ул. Садовая, 60', '678-901-2345', 'sad@bookstore.com', '10:00 - 18:00'), " +
                    "('ул. Лесная, 70', '789-012-3456', 'les@bookstore.com', '9:00 - 17:00'), " +
                    "('ул. Озерная, 80', '890-123-4567', 'lake@bookstore.com', '10:00 - 20:00'), " +
                    "('ул. Горная, 90', '901-234-5678', 'mount@bookstore.com', '9:00 - 19:00'), " +
                    "('ул. Полевая, 100', '912-345-6789', 'field@bookstore.com', '10:00 - 18:00'), " +
                    "('пр. Весенний, 110', '923-456-7890', 'spring@bookstore.com', '9:00 - 20:00'), " +
                    "('ул. Зимняя, 120', '934-567-8901', 'winter@bookstore.com', '10:00 - 19:00'), " +
                    "('пр. Осенний, 130', '945-678-9012', 'autumn@bookstore.com', '9:00 - 18:00'), " +
                    "('ул. Летняя, 140', '956-789-0123', 'summer@bookstore.com', '10:00 - 20:00'), " +
                    "('ул. Заречная, 150', '967-890-1234', 'river@bookstore.com', '9:00 - 19:00'), " +
                    "('ул. Южная, 160', '978-901-2345', 'south@bookstore.com', '10:00 - 18:00'), " +
                    "('ул. Северная, 170', '989-012-3456', 'north@bookstore.com', '9:00 - 17:00'), " +
                    "('ул. Восточная, 180', '990-123-4567', 'east@bookstore.com', '10:00 - 20:00'), " +
                    "('ул. Западная, 190', '991-234-5678', 'west@bookstore.com', '9:00 - 19:00'), " +
                    "('ул. Центральная, 200', '992-345-6789', 'center@bookstore.com', '10:00 - 18:00');";

    private final String insertIntoBooks =
            "INSERT INTO книги (название, автор, жанр, цена) VALUES " +
                    "('Война и Мир', 'Лев Толстой', 'Роман', 500.00), " +
                    "('Мастер и Маргарита', 'Михаил Булгаков', 'Роман', 450.00), " +
                    "('Преступление и наказание', 'Федор Достоевский', 'Роман', 550.00), " +
                    "('Идиот', 'Федор Достоевский', 'Роман', 500.00), " +
                    "('Анна Каренина', 'Лев Толстой', 'Роман', 480.00), " +
                    "('Собачье сердце', 'Михаил Булгаков', 'Рассказ', 300.00), " +
                    "('Отцы и дети', 'Иван Тургенев', 'Роман', 350.00), " +
                    "('Герой нашего времени', 'Михаил Лермонтов', 'Роман', 400.00), " +
                    "('Мертвые души', 'Николай Гоголь', 'Поэма', 360.00), " +
                    "('Обломов', 'Иван Гончаров', 'Роман', 420.00), " +
                    "('Дубровский', 'Александр Пушкин', 'Роман', 380.00), " +
                    "('Евгений Онегин', 'Александр Пушкин', 'Роман в стихах', 410.00), " +
                    "('Братья Карамазовы', 'Федор Достоевский', 'Роман', 560.00), " +
                    "('Записки юного врача', 'Михаил Булгаков', 'Рассказы', 320.00), " +
                    "('Белая гвардия', 'Михаил Булгаков', 'Роман', 450.00), " +
                    "('Беглец', 'Александр Пушкин', 'Поэма', 340.00), " +
                    "('Дама с собачкой', 'Антон Чехов', 'Рассказ', 290.00), " +
                    "('Чайка', 'Антон Чехов', 'Пьеса', 310.00), " +
                    "('Три сестры', 'Антон Чехов', 'Пьеса', 330.00), " +
                    "('Вишнёвый сад', 'Антон Чехов', 'Пьеса', 350.00);";

    private final String insertIntoInventory =
            "INSERT INTO учет (название_книги, адрес_магазина, количество_книг) VALUES " +
                    "('Война и Мир', 'ул. Пушкина, 10', 5), " +
                    "('Мастер и Маргарита', 'ул. Пушкина, 10', 3), " +
                    "('Преступление и наказание', 'пр. Ленина, 20', 6), " +
                    "('Идиот', 'пр. Ленина, 20', 4), " +
                    "('Анна Каренина', 'ул. Гоголя, 30', 7), " +
                    "('Собачье сердце', 'ул. Гоголя, 30', 2), " +
                    "('Отцы и дети', 'ул. Чехова, 40', 8), " +
                    "('Герой нашего времени', 'ул. Чехова, 40', 5), " +
                    "('Мертвые души', 'пр. Мира, 50', 9), " +
                    "('Обломов', 'пр. Мира, 50', 3), " +
                    "('Дубровский', 'ул. Садовая, 60', 4), " +
                    "('Евгений Онегин', 'ул. Садовая, 60', 6), " +
                    "('Братья Карамазовы', 'ул. Лесная, 70', 2), " +
                    "('Записки юного врача', 'ул. Лесная, 70', 7), " +
                    "('Белая гвардия', 'ул. Озерная, 80', 5), " +
                    "('Беглец', 'ул. Озерная, 80', 8), " +
                    "('Дама с собачкой', 'ул. Горная, 90', 3), " +
                    "('Чайка', 'ул. Горная, 90', 9), " +
                    "('Три сестры', 'ул. Полевая, 100', 6), " +
                    "('Вишнёвый сад', 'ул. Полевая, 100', 4);";

}
