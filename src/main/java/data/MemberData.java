package data;

import libsystem.Members;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberData {

    public void addMember(Members member) {
        String sql = "INSERT INTO members (member_id, member_name, email, membership_type) VALUES (?, ?, ?, ?)";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, member.getMemberId());
            pstmt.setString(2, member.getMemberName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getMembershipType());

            pstmt.executeUpdate();
            System.out.println("Member added successfully");

        } catch (Exception e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    public List<Members> getAllMembers() {
        List<Members> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try (Connection conn = DbConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Members member = new Members(
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("email"),
                        rs.getString("membership_type")
                );

                members.add(member);
            }

        } catch (Exception e) {
            System.out.println("Error loading members: " + e.getMessage());
        }

        return members;
    }

    public void updateMember(Members member) {
        String sql = "UPDATE members SET member_name = ?, email = ?, membership_type = ? WHERE member_id = ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getMemberName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getMembershipType());
            pstmt.setInt(4, member.getMemberId());

            pstmt.executeUpdate();
            System.out.println("Member updated successfully");

        } catch (Exception e) {
            System.out.println("Error updating member: " + e.getMessage());
        }
    }

    public void deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            pstmt.executeUpdate();

            System.out.println("Member deleted successfully");

        } catch (Exception e) {
            System.out.println("Error deleting member: " + e.getMessage());
        }
    }

    public List<Members> searchMembers(String keyword) {
        List<Members> members = new ArrayList<>();

        String sql = "SELECT * FROM members WHERE " +
                "member_name LIKE ? OR CAST(member_id AS TEXT) LIKE ?";

        try (Connection conn = DbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchText = "%" + keyword + "%";

            pstmt.setString(1, searchText);
            pstmt.setString(2, searchText);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Members member = new Members(
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("email"),
                        rs.getString("membership_type")
                );

                members.add(member);
            }

        } catch (Exception e) {
            System.out.println("Error searching members: " + e.getMessage());
        }

        return members;
    }
}