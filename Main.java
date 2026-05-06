import data.DbConnection;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        try (Connection conn = DbConnection.connect()) {

            if (conn != null) {
                System.out.println("Connected to database successfully");
                DbConnection.createTables();
            } else {
                System.out.println("Connection failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}