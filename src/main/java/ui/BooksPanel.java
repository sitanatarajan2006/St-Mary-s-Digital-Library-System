package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    public BooksPanel() {

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

        HBox buttonRow = new HBox(15);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button refreshButton = new Button("Refresh");

        buttonRow.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton);

        view.getChildren().addAll(inputGrid, searchBox, table, buttonRow);
    }

    public VBox getView() {

        return view;
    }
}