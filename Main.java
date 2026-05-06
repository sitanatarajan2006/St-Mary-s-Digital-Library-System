import data.BookData;
import data.DbConnection;
import libsystem.Books;

public class Main {

    public static void main(String[] args) {

        DbConnection.createTables();

        BookData bookData = new BookData();

        Books book = new Books(1, "Introduction to Java", "John Smith", "Programming", "Available");

        bookData.addBook(book);

        for (Books b : bookData.getAllBooks()) {
            System.out.println(b.getBookId() + " | " + b.getTitle() + " | " + b.getAuthor());
        }
    }
}