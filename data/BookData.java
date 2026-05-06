package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import libsystem.Books;

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
    public void updateBook(Books book) {
    String sql = "UPDATE books SET title = ?, author = ?, category = ?, availability_status = ? WHERE book_id = ?";

    try (Connection conn = DbConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, book.getTitle());
        pstmt.setString(2, book.getAuthor());
        pstmt.setString(3, book.getCategory());
        pstmt.setString(4, book.getAvailabilityStatus());
        pstmt.setInt(5, book.getBookId());

        pstmt.executeUpdate();
        System.out.println("Book updated successfully");

    } catch (Exception e) {
        System.out.println("Error updating book: " + e.getMessage());
    }
}

    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = DbConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();

            System.out.println("Book deleted successfully");

        } catch (Exception e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    public List<Books> searchBooks(String keyword) {
        List<Books> books = new ArrayList<>();

        String sql = "SELECT * FROM books WHERE " +
                "title LIKE ? OR author LIKE ? OR category LIKE ? OR CAST(book_id AS TEXT) LIKE ?";

        try (Connection conn = DbConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchText = "%" + keyword + "%";

            pstmt.setString(1, searchText);
            pstmt.setString(2, searchText);
            pstmt.setString(3, searchText);
            pstmt.setString(4, searchText);

            ResultSet rs = pstmt.executeQuery();

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
            System.out.println("Error searching books: " + e.getMessage());
        }

        return books;
    }
}