package ui;

import java.util.List;

import data.BookData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import libsystem.Books;

public class BooksPanel {

    private VBox view;

    private TextField bookIdField;
    private TextField titleField;
    private TextField authorField;
    private ComboBox<String> categoryBox;
    private ComboBox<String> statusBox;
    private TextField searchField;

    private TableView<Books> table;
    private ObservableList<Books> bookList;

    private BookData bookData;

    public BooksPanel() {

        bookData = new BookData();

        view = new VBox(15);
        view.setStyle("-fx-padding: 20;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(10);

        bookIdField = new TextField();
        titleField = new TextField();
        authorField = new TextField();

        categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(
                "-- Select Category --",
                "Programming",
                "Science",
                "History",
                "Engineering",
                "Business",
                "Mathematics",
                "Literature",
                "Other"
        );
        categoryBox.getSelectionModel().selectFirst();

        statusBox = new ComboBox<>();
        statusBox.getItems().addAll(
                "-- Select Status --",
                "Available",
                "Borrowed",
                "Reserved",
                "Overdue"
        );
        statusBox.getSelectionModel().selectFirst();

        inputGrid.add(new Label("Book ID"), 0, 0);
        inputGrid.add(new Label("Title"), 1, 0);
        inputGrid.add(new Label("Author"), 2, 0);
        inputGrid.add(new Label("Category"), 3, 0);
        inputGrid.add(new Label("Status"), 4, 0);

        inputGrid.add(bookIdField, 0, 1);
        inputGrid.add(titleField, 1, 1);
        inputGrid.add(authorField, 2, 1);
        inputGrid.add(categoryBox, 3, 1);
        inputGrid.add(statusBox, 4, 1);

        VBox searchBox = new VBox(8);

        Label searchLabel = new Label("Search Books");
        Label searchHint = new Label("Search by ID, title, author, or category");

        searchField = new TextField();

        Button searchButton = new Button("Search");

        HBox searchRow = new HBox(10);
        searchRow.getChildren().addAll(searchField, searchButton);

        searchBox.getChildren().addAll(searchLabel, searchHint, searchRow);

        table = new TableView<>();

        TableColumn<Books, Integer> idColumn = new TableColumn<>("Book ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Books, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Books, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Books, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Books, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, statusColumn);

        HBox buttonRow = new HBox(15);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button refreshButton = new Button("Refresh");

        buttonRow.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton);

        view.getChildren().addAll(inputGrid, searchBox, table, buttonRow);

        loadBooks();

        addButton.setOnAction(e -> addBook());

        updateButton.setOnAction(e -> updateBook());

        deleteButton.setOnAction(e -> deleteBook());

        refreshButton.setOnAction(e -> {
            loadBooks();
            clearFields();
            searchField.setText("");
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> fillFieldsFromTable(newSelection));
    }

    private void loadBooks() {

        List<Books> books = bookData.getAllBooks();

        bookList = FXCollections.observableArrayList(books);

        table.setItems(bookList);
    }

    private void addBook() {

        Books book = new Books(
                Integer.parseInt(bookIdField.getText().trim()),
                titleField.getText().trim(),
                authorField.getText().trim(),
                categoryBox.getSelectionModel().getSelectedItem(),
                statusBox.getSelectionModel().getSelectedItem()
        );

        bookData.addBook(book);

        loadBooks();

        clearFields();
    }

    private void updateBook() {

        Books book = new Books(
                Integer.parseInt(bookIdField.getText().trim()),
                titleField.getText().trim(),
                authorField.getText().trim(),
                categoryBox.getSelectionModel().getSelectedItem(),
                statusBox.getSelectionModel().getSelectedItem()
        );

        bookData.updateBook(book);

        loadBooks();

        clearFields();
    }

    private void deleteBook() {

        int bookId = Integer.parseInt(bookIdField.getText().trim());

        bookData.deleteBook(bookId);

        loadBooks();

        clearFields();
    }

    private void fillFieldsFromTable(Books book) {

        if (book != null) {

            bookIdField.setText(String.valueOf(book.getBookId()));

            titleField.setText(book.getTitle());

            authorField.setText(book.getAuthor());

            categoryBox.getSelectionModel().select(book.getCategory());

            statusBox.getSelectionModel().select(book.getAvailabilityStatus());

            bookIdField.setEditable(false);
        }
    }

    private void clearFields() {

        bookIdField.setText("");

        titleField.setText("");

        authorField.setText("");

        categoryBox.getSelectionModel().selectFirst();

        statusBox.getSelectionModel().selectFirst();

        bookIdField.setEditable(true);

        table.getSelectionModel().clearSelection();
    }

    public VBox getView() {

        return view;
    }
}