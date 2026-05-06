package ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import data.BorrowData;
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
import libsystem.Borrow;

public class BorrowPanel {

    private VBox view;

    private TextField recordIdField;
    private TextField bookIdField;
    private TextField memberIdField;
    private TextField borrowDateField;
    private TextField dueDateField;
    private ComboBox<String> statusBox;

    private TextField searchField;
    private TextField startDateField;
    private TextField endDateField;

    private TableView<Borrow> table;
    private ObservableList<Borrow> borrowList;

    private BorrowData borrowData;

    public BorrowPanel() {

        borrowData = new BorrowData();

        view = new VBox(15);
        view.setStyle("-fx-padding: 20;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(10);

        recordIdField = new TextField();
        recordIdField.setPromptText("Numeric ID");

        bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");

        memberIdField = new TextField();
        memberIdField.setPromptText("Member ID");

        borrowDateField = new TextField();
        borrowDateField.setPromptText("YYYY-MM-DD");

        dueDateField = new TextField();
        dueDateField.setPromptText("YYYY-MM-DD");

        statusBox = new ComboBox<>();
        statusBox.getItems().addAll(
                "-- Select Status --",
                "Borrowed",
                "Returned",
                "Overdue"
        );
        statusBox.getSelectionModel().selectFirst();
        statusBox.setMaxWidth(Double.MAX_VALUE);

        inputGrid.add(new Label("Record ID"), 0, 0);
        inputGrid.add(new Label("Book ID"), 1, 0);
        inputGrid.add(new Label("Member ID"), 2, 0);
        inputGrid.add(new Label("Borrow Date"), 3, 0);
        inputGrid.add(new Label("Due Date"), 4, 0);
        inputGrid.add(new Label("Status"), 5, 0);

        inputGrid.add(recordIdField, 0, 1);
        inputGrid.add(bookIdField, 1, 1);
        inputGrid.add(memberIdField, 2, 1);
        inputGrid.add(borrowDateField, 3, 1);
        inputGrid.add(dueDateField, 4, 1);
        inputGrid.add(statusBox, 5, 1);

        VBox searchBox = new VBox(8);
        searchBox.setStyle("-fx-padding: 10 0 10 0;");

        Label searchLabel = new Label("Search and Filter Borrow Records");
        Label searchHint = new Label("Search by record ID, book ID, member ID, or status");

        searchField = new TextField();
        searchField.setPromptText("Enter search keyword");

        Button searchButton = new Button("Search");
        searchButton.setMinWidth(100);

        HBox searchRow = new HBox(10);
        searchRow.getChildren().addAll(searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        startDateField = new TextField();
        startDateField.setPromptText("Start date YYYY-MM-DD");

        endDateField = new TextField();
        endDateField.setPromptText("End date YYYY-MM-DD");

        Button filterButton = new Button("Filter Date Range");
        filterButton.setMinWidth(140);

        Button overdueButton = new Button("Show Overdue");
        overdueButton.setMinWidth(120);

        HBox filterRow = new HBox(10);
        filterRow.getChildren().addAll(startDateField, endDateField, filterButton, overdueButton);
        HBox.setHgrow(startDateField, Priority.ALWAYS);
        HBox.setHgrow(endDateField, Priority.ALWAYS);

        searchBox.getChildren().addAll(searchLabel, searchHint, searchRow, filterRow);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Borrow, Integer> recordIdColumn = new TableColumn<>("Record ID");
        recordIdColumn.setCellValueFactory(new PropertyValueFactory<>("recordId"));

        TableColumn<Borrow, Integer> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Borrow, Integer> memberIdColumn = new TableColumn<>("Member ID");
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        TableColumn<Borrow, String> borrowDateColumn = new TableColumn<>("Borrow Date");
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        TableColumn<Borrow, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Borrow, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("returnStatus"));

        table.getColumns().addAll(
                recordIdColumn,
                bookIdColumn,
                memberIdColumn,
                borrowDateColumn,
                dueDateColumn,
                statusColumn
        );

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

        loadBorrowRecords();

        addButton.setOnAction(e -> addBorrowRecord());

        updateButton.setOnAction(e -> updateBorrowRecord());

        deleteButton.setOnAction(e -> deleteBorrowRecord());

        refreshButton.setOnAction(e -> {
            loadBorrowRecords();
            clearFields();
            searchField.setText("");
            startDateField.setText("");
            endDateField.setText("");
        });

        searchButton.setOnAction(e -> searchBorrowRecords());

        searchField.setOnAction(e -> searchBorrowRecords());

        filterButton.setOnAction(e -> filterByDateRange());

        overdueButton.setOnAction(e -> showOverdueRecords());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> fillFieldsFromTable(newSelection));
    }

    private void loadBorrowRecords() {

        List<Borrow> records = borrowData.getAllBorrowRecords();

        borrowList = FXCollections.observableArrayList(records);

        table.setItems(borrowList);
    }

    private boolean validateBorrowForm() {

        if (recordIdField.getText().trim().isEmpty()) {

            showError("Please enter a Record ID");

            return false;
        }

        if (bookIdField.getText().trim().isEmpty()) {

            showError("Please enter a Book ID");

            return false;
        }

        if (memberIdField.getText().trim().isEmpty()) {

            showError("Please enter a Member ID");

            return false;
        }

        try {

            Integer.parseInt(recordIdField.getText().trim());
            Integer.parseInt(bookIdField.getText().trim());
            Integer.parseInt(memberIdField.getText().trim());

        } catch (Exception e) {

            showError("Record ID, Book ID and Member ID must be numeric");

            return false;
        }

        if (borrowDateField.getText().trim().isEmpty()) {

            showError("Please enter a borrow date");

            return false;
        }

        if (dueDateField.getText().trim().isEmpty()) {

            showError("Please enter a due date");

            return false;
        }

        if (!isValidDate(borrowDateField.getText().trim())) {

            showError("Borrow date must be a valid date in YYYY-MM-DD format");

            return false;
        }

        if (!isValidDate(dueDateField.getText().trim())) {

            showError("Due date must be a valid date in YYYY-MM-DD format");

            return false;
        }

        LocalDate borrowDate = LocalDate.parse(borrowDateField.getText().trim());
        LocalDate dueDate = LocalDate.parse(dueDateField.getText().trim());

        if (dueDate.isBefore(borrowDate)) {

            showError("Due date must be later than borrow date");

            return false;
        }

        if (statusBox.getSelectionModel().getSelectedIndex() == 0) {

            showError("Please select a valid status");

            return false;
        }

        return true;
    }

    private boolean isValidDate(String date) {

        try {

            LocalDate.parse(date);

            return true;

        } catch (DateTimeParseException e) {

            return false;
        }
    }

    private boolean recordIdExists(int recordId) {

        List<Borrow> records = borrowData.getAllBorrowRecords();

        for (Borrow record : records) {

            if (record.getRecordId() == recordId) {

                return true;
            }
        }

        return false;
    }

    private void addBorrowRecord() {

        if (!validateBorrowForm()) {

            return;
        }

        try {

            int recordId = Integer.parseInt(recordIdField.getText().trim());

            if (recordIdExists(recordId)) {

                showError("Record ID already exists");

                return;
            }

            Borrow record = new Borrow(
                    recordId,
                    Integer.parseInt(bookIdField.getText().trim()),
                    Integer.parseInt(memberIdField.getText().trim()),
                    borrowDateField.getText().trim(),
                    dueDateField.getText().trim(),
                    statusBox.getSelectionModel().getSelectedItem()
            );

            borrowData.addBorrowRecord(record);

            loadBorrowRecords();

            clearFields();

            showInfo("Borrow record added successfully");

        } catch (Exception e) {

            showError("Invalid input");
        }
    }

    private void updateBorrowRecord() {

        if (!validateBorrowForm()) {

            return;
        }

        try {

            Borrow record = new Borrow(
                    Integer.parseInt(recordIdField.getText().trim()),
                    Integer.parseInt(bookIdField.getText().trim()),
                    Integer.parseInt(memberIdField.getText().trim()),
                    borrowDateField.getText().trim(),
                    dueDateField.getText().trim(),
                    statusBox.getSelectionModel().getSelectedItem()
            );

            borrowData.updateBorrowRecord(record);

            loadBorrowRecords();

            clearFields();

            showInfo("Borrow record updated successfully");

        } catch (Exception e) {

            showError("Invalid input");
        }
    }

    private void deleteBorrowRecord() {

        if (recordIdField.getText().trim().isEmpty()) {

            showError("Select a valid borrow record");

            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.setTitle("Confirm Delete");

        confirm.setHeaderText(null);

        confirm.setContentText("Delete selected borrow record?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {

            return;
        }

        try {

            int recordId = Integer.parseInt(recordIdField.getText().trim());

            borrowData.deleteBorrowRecord(recordId);

            loadBorrowRecords();

            clearFields();

            showInfo("Borrow record deleted successfully");

        } catch (Exception e) {

            showError("Select a valid borrow record");
        }
    }

    private void searchBorrowRecords() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadBorrowRecords();

            return;
        }

        List<Borrow> records = borrowData.searchBorrowRecords(keyword);

        borrowList = FXCollections.observableArrayList(records);

        table.setItems(borrowList);
    }

    private void filterByDateRange() {

        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();

        if (!isValidDate(startDate) || !isValidDate(endDate)) {

            showError("Start and end dates must be valid dates in YYYY-MM-DD format");

            return;
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (end.isBefore(start)) {

            showError("End date must be later than start date");

            return;
        }

        List<Borrow> records = borrowData.filterByDateRange(startDate, endDate);

        borrowList = FXCollections.observableArrayList(records);

        table.setItems(borrowList);
    }

    private void showOverdueRecords() {

        List<Borrow> records = borrowData.getOverdueRecords();

        borrowList = FXCollections.observableArrayList(records);

        table.setItems(borrowList);
    }

    private void fillFieldsFromTable(Borrow record) {

        if (record != null) {

            recordIdField.setText(String.valueOf(record.getRecordId()));

            bookIdField.setText(String.valueOf(record.getBookId()));

            memberIdField.setText(String.valueOf(record.getMemberId()));

            borrowDateField.setText(record.getBorrowDate());

            dueDateField.setText(record.getDueDate());

            statusBox.getSelectionModel().select(record.getReturnStatus());

            recordIdField.setEditable(false);
        }
    }

    private void clearFields() {

        recordIdField.setText("");

        bookIdField.setText("");

        memberIdField.setText("");

        borrowDateField.setText("");

        dueDateField.setText("");

        statusBox.getSelectionModel().selectFirst();

        recordIdField.setEditable(true);

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