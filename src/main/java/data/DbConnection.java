package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbConnection {

    private static final String URL = "jdbc:sqlite:library.db";

    // Connect to database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create tables
    public static void createTables() {

        String booksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INTEGER PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "availability_status TEXT NOT NULL" +
                ");";

        String membersTable = "CREATE TABLE IF NOT EXISTS members (" +
                "member_id INTEGER PRIMARY KEY," +
                "member_name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "membership_type TEXT NOT NULL" +
                ");";

        String borrowTable = "CREATE TABLE IF NOT EXISTS borrow_records (" +
                "record_id INTEGER PRIMARY KEY," +
                "book_id INTEGER NOT NULL," +
                "member_id INTEGER NOT NULL," +
                "borrow_date TEXT NOT NULL," +
                "due_date TEXT NOT NULL," +
                "return_status TEXT NOT NULL" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(booksTable);
            stmt.execute(membersTable);
            stmt.execute(borrowTable);

            System.out.println("Tables created successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}