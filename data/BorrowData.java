package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import libsystem.Borrow;

public class BorrowData {

    public void addBorrowRecord(Borrow record) {

        String sql = "INSERT INTO borrow_records (record_id, book_id, member_id, borrow_date, due_date, return_status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getRecordId());
            pstmt.setInt(2, record.getBookId());
            pstmt.setInt(3, record.getMemberId());
            pstmt.setString(4, record.getBorrowDate());
            pstmt.setString(5, record.getDueDate());
            pstmt.setString(6, record.getReturnStatus());

            pstmt.executeUpdate();

            System.out.println("Borrow record added successfully");

        } catch (Exception e) {
            System.out.println("Error adding borrow record: " + e.getMessage());
        }
    }

    public List<Borrow> getAllBorrowRecords() {

        List<Borrow> records = new ArrayList<>();

        String sql = "SELECT * FROM borrow_records";

        try (Connection conn = DbConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Borrow record = new Borrow(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_status")
                );

                records.add(record);
            }

        } catch (Exception e) {
            System.out.println("Error loading borrow records: " + e.getMessage());
        }

        return records;
    }

    public void updateBorrowRecord(Borrow record) {

        String sql = "UPDATE borrow_records SET " +
                "book_id = ?, " +
                "member_id = ?, " +
                "borrow_date = ?, " +
                "due_date = ?, " +
                "return_status = ? " +
                "WHERE record_id = ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getBookId());
            pstmt.setInt(2, record.getMemberId());
            pstmt.setString(3, record.getBorrowDate());
            pstmt.setString(4, record.getDueDate());
            pstmt.setString(5, record.getReturnStatus());
            pstmt.setInt(6, record.getRecordId());

            pstmt.executeUpdate();

            System.out.println("Borrow record updated successfully");

        } catch (Exception e) {
            System.out.println("Error updating borrow record: " + e.getMessage());
        }
    }

    public void deleteBorrowRecord(int recordId) {

        String sql = "DELETE FROM borrow_records WHERE record_id = ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recordId);

            pstmt.executeUpdate();

            System.out.println("Borrow record deleted successfully");

        } catch (Exception e) {
            System.out.println("Error deleting borrow record: " + e.getMessage());
        }
    }

    public List<Borrow> searchBorrowRecords(String keyword) {

        List<Borrow> records = new ArrayList<>();

        String sql = "SELECT * FROM borrow_records WHERE " +
                "CAST(record_id AS TEXT) LIKE ? OR " +
                "CAST(book_id AS TEXT) LIKE ? OR " +
                "CAST(member_id AS TEXT) LIKE ? OR " +
                "return_status LIKE ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchText = "%" + keyword + "%";

            pstmt.setString(1, searchText);
            pstmt.setString(2, searchText);
            pstmt.setString(3, searchText);
            pstmt.setString(4, searchText);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Borrow record = new Borrow(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_status")
                );

                records.add(record);
            }

        } catch (Exception e) {
            System.out.println("Error searching borrow records: " + e.getMessage());
        }

        return records;
    }

    public List<Borrow> filterByDateRange(String startDate, String endDate) {

        List<Borrow> records = new ArrayList<>();

        String sql = "SELECT * FROM borrow_records WHERE borrow_date BETWEEN ? AND ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Borrow record = new Borrow(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_status")
                );

                records.add(record);
            }

        } catch (Exception e) {
            System.out.println("Error filtering borrow records: " + e.getMessage());
        }

        return records;
    }

    public List<Borrow> getOverdueRecords() {

        List<Borrow> records = new ArrayList<>();

        String sql = "SELECT * FROM borrow_records " +
                "WHERE due_date < date('now') " +
                "AND return_status != 'Returned'";

        try (Connection conn = DbConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Borrow record = new Borrow(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_status")
                );

                records.add(record);
            }

        } catch (Exception e) {
            System.out.println("Error loading overdue records: " + e.getMessage());
        }

        return records;
    }
}