package data;

import libsystem.Books;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookData {

    public void addBook(Books book) {
        String sql = "INSERT INTO books (book_id, title, author, category, availability_status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, book.getBookId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getCategory());
            pstmt.setString(5, book.getAvailabilityStatus());

            pstmt.executeUpdate();
            System.out.println("Book added successfully");

        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    public List<Books> getAllBooks() {
        List<Books> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DbConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Books book = new Books(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("availability_status")
                );

                books.add(book);
            }

        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }

        return books;
    }
}