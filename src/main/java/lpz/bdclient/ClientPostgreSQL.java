package lpz.bdclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientPostgreSQL implements JDBCClient {
    private static ClientPostgreSQL instance;
    private Properties dbProperties;
    private String login = null;
    private String password = null;
    private String dbUrl = null;
    private String dbSchema = null;
    Connection externalConnection = null;

    public static ClientPostgreSQL getInstance() {
        return instance == null ? instance = new ClientPostgreSQL() : instance;
    }

    private ClientPostgreSQL() {
        try {
            dbProperties = getDbProperties(getClass().getResource("/properties/config.properties").openStream());
            if (dbProperties != null) {
                Class.forName(dbProperties.getProperty("db.driver"));
                dbUrl = dbProperties.getProperty("db.url");
                dbSchema = dbProperties.getProperty("db.schema");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Не найден файл настроек");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties getDbProperties(InputStream configFileInput) {
        Properties property = new Properties();
        try {
            property.load(configFileInput);
            return property;
        } catch (FileNotFoundException e) {
            System.out.println("Не найден файл настроек");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean accessToDB(String login, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, login, password);
            connection.close();
            this.login = login;
            this.password = password;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        createTablesIfNeeded();
        return true;
    }

    @Override
    public String getLogin() {
        return login;
    }

    public Connection getConnection() throws SQLException {
        externalConnection = null;
        externalConnection = DriverManager.getConnection(dbUrl, login, password);
        return externalConnection;
    }

    public void closeConnection() throws SQLException {
        if (externalConnection != null) {
            externalConnection.close();
        }
    }

    @Override
    public List<String> getTableNames() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(dbProperties.getProperty("tableNamesSql"));
            statement.setString(1, dbSchema);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<String> arrayList = new ArrayList<>();
            while (resultSet.next()) {
                arrayList.add(resultSet.getString(1));
            }
            return arrayList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String> stringListQuery(String query, String nameOfColToGet) {
        Connection connection = null;
        ResultSet resultSet;
        ArrayList<String> finalList = new ArrayList<>();
        try {

            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(query.trim());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                finalList.add(resultSet.getString(nameOfColToGet));
            }
//            System.out.println(finalList);
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return finalList;
    }

//    public ArrayList<String> stringListQuery(String query) {
//        Connection connection = null;
//        ResultSet resultSet;
//        ArrayList<String> finalList = new ArrayList<>();
//        try {
//            connection = DriverManager.getConnection(dbUrl, login, password);
//            PreparedStatement statement = connection.prepareStatement(query.trim());
//            resultSet = statement.executeQuery();
//
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//
//            while (resultSet.next()) {
//                StringBuilder row = new StringBuilder();
//                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = metaData.getColumnName(i);
//                    String columnValue = resultSet.getString(i);
//                    row.append(columnName).append(": ").append(columnValue).append(", ");
//                }
//                // Удаляем последнюю запятую и пробел
//                if (row.length() > 2) {
//                    row.delete(row.length() - 2, row.length());
//                }
//                finalList.add(row.toString());
//            }
//
//            System.out.println(finalList);
//            resultSet.close();
//            statement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return finalList;
//    }


    public String getInfoByColoumnQuery(String query, String colName) {
        Connection connection = null;
        ResultSet resultSet;
        String result = null;

        try {
            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(query.trim());
            statement.setString(1, colName); // Подставьте значение id в запрос

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString(1); // Используйте соответствующий индекс или имя столбца
            }

//            System.out.println(result);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    @Override
    public ResultSet getTable(String selectedTable) {
        Connection connection = null;
        String orderBy = "";
        switch (selectedTable) {
            case "магазины": orderBy = "order by id ASC"; break;
            case "книги": orderBy = "order by id ASC"; break;
            case "учет": {
//                orderBy = "order by id_магазина ASC";
                break;
            }
            default:
                System.out.println("orderBy detection error");
        }
        try {
            String query = String.format(dbProperties.getProperty("getTableSql"), dbSchema, selectedTable, orderBy);
            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(query.toString());
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean updateTable(String selectedTable, String columnChangeName, String newRecord, String columnSearchName, String columnSearch) {
        Connection connection = null;
        try {
            String query = String.format(dbProperties.getProperty("updateTable"), dbSchema, selectedTable, columnChangeName, columnSearchName, columnSearch);
            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, convertStringToInteger(newRecord));
            return statement.executeUpdate() != -1 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean deleteRowTable(String selectedTable, String columnSearchName, String columnSearch) {
        Connection connection = null;
        try {
            String query = String.format(dbProperties.getProperty("deleteRowTable"), dbSchema, selectedTable, columnSearchName, columnSearch);
            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeUpdate() != -1 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean simpleQuery(String selectedTable, String sql) {
        Connection connection = null;
        try {
            String query = String.format(sql, dbSchema, selectedTable);
            connection = DriverManager.getConnection(dbUrl, login, password);
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeUpdate() != -1 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object convertStringToInteger(String str) {
        try {
            return new Integer(str);
        } catch (NumberFormatException e) {
            return str;
        }
    }

// TODO: УДАЛИТЬ sout
    public void createTablesIfNeeded() {
        List<String> tableNames = getTableNames();
        Connection connection = null;

        if (tableNames == null || !tableNames.contains("магазины")) {

            boolean created = simpleQuery("public", createStoresTable);
            if (created) {
                System.out.println("Table 'shoes' created.");
                boolean inserted = simpleQuery("public", insertIntoStores);
                if (inserted) {
                    System.out.println("Initial data inserted into table 'shoes'.");
                } else {
                    System.out.println("Error inserting initial data into table 'shoes'.");
                }
            } else {
                System.out.println("Error creating table 'shoes'.");
            }
        }

        if (tableNames == null || !tableNames.contains("книги")) {
            boolean created = simpleQuery("public", createBooksTable);
            if (created) {
                System.out.println("Table 'employees' created.");
                boolean inserted = simpleQuery("public", insertIntoBooks);
                if (inserted) {
                    System.out.println("Initial data inserted into table 'employees'.");
                } else {
                    System.out.println("Error inserting initial data into table 'employees'.");
                }

            } else {
                System.out.println("Error creating table 'employees'.");
            }
        }

        if (tableNames == null || !tableNames.contains("учет")) {
            boolean created = simpleQuery("public", createInventoryTable);
//            simpleQuery("public", "CREATE UNIQUE INDEX idx_название_адрес ON учет (название_книги, адрес_магазина)");
            if (created) {
                System.out.println("Table 'employees' created.");
                boolean inserted = simpleQuery("public", insertIntoInventory);
                if (inserted) {
                    System.out.println("Initial data inserted into table 'employees'.");
                } else {
                    System.out.println("Error inserting initial data into table 'employees'.");
                }

            } else {
                System.out.println("Error creating table 'employees'.");
            }
        }
    }


    /**
     * SQL запросы
     **/

    private final String createStoresTable =
            "CREATE TABLE магазины (" +
                    "id SERIAL PRIMARY KEY, " +
                    "адрес VARCHAR(255) UNIQUE, " +
                    "телефон VARCHAR(50), " +
                    "электронная_почта VARCHAR(100), " +
                    "рабочие_часы VARCHAR(100));";

    private final String createBooksTable =
            "CREATE TABLE книги (" +
                    "id SERIAL PRIMARY KEY, " +
                    "название VARCHAR(255) UNIQUE, " +
                    "автор VARCHAR(255), " +
                    "жанр VARCHAR(100), " +
                    "цена DECIMAL(10, 2));";


    private final String createInventoryTable =
            "CREATE TABLE учет (" +
                    "название_книги VARCHAR(255)," +
                    "адрес_магазина VARCHAR(255)," +
                    "количество_книг INT," +
                    "PRIMARY KEY (название_книги, адрес_магазина)," +
                    "FOREIGN KEY (название_книги) REFERENCES книги(название)," +
                    "FOREIGN KEY (адрес_магазина) REFERENCES магазины(адрес)" +
                    ");";


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


//    private final String insertIntoInventory =
//            "INSERT INTO учет (id_книги, id_магазина, количество_книг) VALUES " +
//                    "(1, 1, 5), " +
//                    "(2, 1, 3), " +
//                    "(3, 2, 6), " +
//                    "(4, 2, 4), " +
//                    "(5, 3, 7), " +
//                    "(6, 3, 2), " +
//                    "(7, 4, 8), " +
//                    "(8, 4, 5), " +
//                    "(9, 5, 9), " +
//                    "(10, 5, 3), " +
//                    "(11, 6, 4), " +
//                    "(12, 6, 6), " +
//                    "(13, 7, 2), " +
//                    "(14, 7, 7), " +
//                    "(15, 8, 5), " +
//                    "(16, 8, 8), " +
//                    "(17, 9, 3), " +
//                    "(18, 9, 9), " +
//                    "(19, 10, 6), " +
//                    "(20, 10, 4);";

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