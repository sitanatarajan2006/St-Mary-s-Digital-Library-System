import data.BorrowData;
import data.DbConnection;
import libsystem.Borrow;

public class Main {

    public static void main(String[] args) {

        DbConnection.createTables();

        BorrowData borrowData = new BorrowData();

        Borrow record = new Borrow(
                1,
                1,
                1,
                "2026-05-06",
                "2026-05-20",
                "Borrowed"
        );

        borrowData.addBorrowRecord(record);

        System.out.println("All borrow records:");

        for (Borrow r : borrowData.getAllBorrowRecords()) {

            System.out.println(
                    r.getRecordId() +
                    " | Book: " +
                    r.getBookId() +
                    " | Member: " +
                    r.getMemberId() +
                    " | " +
                    r.getReturnStatus()
            );
        }
    }
}