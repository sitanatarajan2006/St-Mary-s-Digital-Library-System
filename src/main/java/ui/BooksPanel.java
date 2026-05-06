package ui;

import java.util.List;

import data.BookData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        bookIdField.setPromptText("Numeric ID");

        titleField = new TextField();
        titleField.setPromptText("Book title");

        authorField = new TextField();
        authorField.setPromptText("Author name");

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
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        statusBox = new ComboBox<>();
        statusBox.getItems().addAll(
                "-- Select Status --",
                "Available",
                "Borrowed",
                "Reserved",
                "Overdue"
        );
        statusBox.getSelectionModel().selectFirst();
        statusBox.setMaxWidth(Double.MAX_VALUE);

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
        searchBox.setStyle("-fx-padding: 10 0 10 0;");

        Label searchLabel = new Label("Search Books");
        Label searchHint = new Label("Search by ID, title, author, or category");

        searchField = new TextField();
        searchField.setPromptText("Enter search keyword");

        Button searchButton = new Button("Search");
        searchButton.setMinWidth(100);

        HBox searchRow = new HBox(10);
        searchRow.getChildren().addAll(searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchBox.getChildren().addAll(searchLabel, searchHint, searchRow);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

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

        VBox.setVgrow(table, Priority.ALWAYS);

        HBox buttonRow = new HBox(15);
        buttonRow.setStyle("-fx-padding: 10 0 0 0;");

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button refreshButton = new Button("Refresh");

        addButton.setMinWidth(100);
        updateButton.setMinWidth(100);
        deleteButton.setMinWidth(100);
        refreshButton.setMinWidth(100);

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

        searchButton.setOnAction(e -> searchBooks());

        searchField.setOnAction(e -> searchBooks());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> fillFieldsFromTable(newSelection));
    }

    private void loadBooks() {

        List<Books> books = bookData.getAllBooks();

        bookList = FXCollections.observableArrayList(books);

        table.setItems(bookList);
    }

    private boolean validateBookForm() {

        if (bookIdField.getText().trim().isEmpty()) {

            showError("Please enter a Book ID");

            return false;
        }

        try {

            Integer.parseInt(bookIdField.getText().trim());

        } catch (Exception e) {

            showError("Book ID must be numeric");

            return false;
        }

        if (titleField.getText().trim().isEmpty()) {

            showError("Please enter a title");

            return false;
        }

        if (authorField.getText().trim().isEmpty()) {

            showError("Please enter an author");

            return false;
        }

        if (categoryBox.getSelectionModel().getSelectedIndex() == 0) {

            showError("Please select a valid category");

            return false;
        }

        if (statusBox.getSelectionModel().getSelectedIndex() == 0) {

            showError("Please select a valid status");

            return false;
        }

        return true;
    }

    private boolean bookIdExists(int bookId) {

        List<Books> books = bookData.getAllBooks();

        for (Books book : books) {

            if (book.getBookId() == bookId) {

                return true;
            }
        }

        return false;
    }

    private void addBook() {

        if (!validateBookForm()) {

            return;
        }

        try {

            int bookId = Integer.parseInt(bookIdField.getText().trim());

            if (bookIdExists(bookId)) {

                showError("Book ID already exists");

                return;
            }

            Books book = new Books(
                    bookId,
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    categoryBox.getSelectionModel().getSelectedItem(),
                    statusBox.getSelectionModel().getSelectedItem()
            );

            bookData.addBook(book);

            loadBooks();

            clearFields();

            showInfo("Book added successfully");

        } catch (Exception e) {

            showError("Invalid input");
        }
    }

    private void updateBook() {

        if (!validateBookForm()) {

            return;
        }

        try {

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

            showInfo("Book updated successfully");

        } catch (Exception e) {

            showError("Invalid input");
        }
    }

    private void deleteBook() {

        if (bookIdField.getText().trim().isEmpty()) {

            showError("Select a valid book");

            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.setTitle("Confirm Delete");

        confirm.setHeaderText(null);

        confirm.setContentText("Delete selected book?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {

            return;
        }

        try {

            int bookId = Integer.parseInt(bookIdField.getText().trim());

            bookData.deleteBook(bookId);

            loadBooks();

            clearFields();

            showInfo("Book deleted successfully");

        } catch (Exception e) {

            showError("Select a valid book");
        }
    }

    private void searchBooks() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadBooks();

            return;
        }

        List<Books> books = bookData.searchBooks(keyword);

        bookList = FXCollections.observableArrayList(books);

        table.setItems(bookList);
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

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showInfo(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Information");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    public VBox getView() {

        return view;
    }
}