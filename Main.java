import data.DbConnection;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        Connection conn = DbConnection.connect();

        if (conn != null) {
            System.out.println("Connected to database successfully");
        } else {
            System.out.println("Connection failed");
        }
    }
}