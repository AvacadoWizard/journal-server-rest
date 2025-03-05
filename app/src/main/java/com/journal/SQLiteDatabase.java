
// package com.journal;

// import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;
// public class SQLiteDatabase {
//     private Connection connection;
//     private Statement statement;

//     public SQLiteDatabase() {
//         connection = connectToDatabase("journal.db");
//         if (connection != null) {
//             try {
//                 statement = connection.createStatement();
//                 createTable();
//             } catch (SQLException e) {
//                 System.out.println("Error creating statement: " + e.getMessage());
//             }
//         }
//     }

//     private Connection connectToDatabase(String databaseString) {
//         Connection conn = null;
//         try {
//             String url = "jdbc:sqlite:" + databaseString;
//             conn = DriverManager.getConnection(url);
//             System.out.println("Connection to SQLite has been established.");
//         } catch (SQLException e) {
//             System.out.println("Failed to connect! " + e.getMessage());
//         }
//         return conn;
//     }

//     private void createTable() {
//         try {
//             statement.execute("CREATE TABLE IF NOT EXISTS entries (" +
//                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                     "title TEXT NOT NULL, " +
//                     "date_published TEXT NOT NULL, " +
//                     "entry_text TEXT NOT NULL)");
//         } catch (SQLException e) {
//             System.out.println("Error creating table: " + e.getMessage());
//         }
//     }

//     public List<Entry> getAllEntries() {
//         List<Entry> entries = new ArrayList<>();
//         try {
//             ResultSet resultSet = statement.executeQuery("SELECT * FROM entries");
//             while (resultSet.next()) {
//                 Entry entry = new Entry(
//                         resultSet.getString("title"),
//                         resultSet.getString("date_published"),
//                         resultSet.getString("entry_text")
//                 );
//                 entries.add(entry);
//             }
//         } catch (SQLException e) {
//             System.out.println("Error retrieving entries: " + e.getMessage());
//         }
//         return entries;
//     }

//     public void insertEntry(Entry entry) {
//         try {
//             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO entries (title, date_published, entry_text) VALUES (?, ?, ?)");
//             preparedStatement.setString(1, entry.getTitle());
//             preparedStatement.setString(2, entry.getDatePublished().toString());
//             preparedStatement.setString(3, entry.getEntryText());
//             preparedStatement.executeUpdate();
//         } catch (SQLException e) {
//             System.out.println("Error inserting entry: " + e.getMessage());
//         }
//     }

//     public void closeConnection() {
//         try {
//             connection.close();
//         } catch (SQLException e) {
//             System.out.println("Error closing connection: " + e.getMessage());
//         }
//     }
// }