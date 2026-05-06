import data.BookData;
import data.DbConnection;
import libsystem.Books;

public class Main {

    public static void main(String[] args) {

        DbConnection.createTables();

        BookData bookData = new BookData();

        Books updatedBook = new Books(1, "Advanced Java Programming", "John Smith", "Programming", "Available");

        bookData.updateBook(updatedBook);

        System.out.println("Search results:");

        for (Books b : bookData.searchBooks("Java")) {
            System.out.println(b.getBookId() + " | " + b.getTitle() + " | " + b.getAuthor());
        }
    }
}