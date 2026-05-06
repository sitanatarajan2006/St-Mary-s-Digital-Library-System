package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import libsystem.Members;

public class MembersPanel {

    private VBox view;

    private TextField memberIdField;
    private TextField memberNameField;
    private TextField emailField;
    private ComboBox<String> membershipTypeBox;
    private TextField searchField;

    private TableView<Members> table;

    public MembersPanel() {

        view = new VBox(15);
        view.setStyle("-fx-padding: 20;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(10);

        memberIdField = new TextField();
        memberNameField = new TextField();
        emailField = new TextField();

        membershipTypeBox = new ComboBox<>();
        membershipTypeBox.getItems().addAll(
                "-- Select Membership Type --",
                "Student",
                "Staff"
        );
        membershipTypeBox.getSelectionModel().selectFirst();

        inputGrid.add(new Label("Member ID"), 0, 0);
        inputGrid.add(new Label("Name"), 1, 0);
        inputGrid.add(new Label("Email"), 2, 0);
        inputGrid.add(new Label("Membership Type"), 3, 0);

        inputGrid.add(memberIdField, 0, 1);
        inputGrid.add(memberNameField, 1, 1);
        inputGrid.add(emailField, 2, 1);
        inputGrid.add(membershipTypeBox, 3, 1);

        VBox searchBox = new VBox(8);

        Label searchLabel = new Label("Search Members");
        Label searchHint = new Label("Search by ID or name");

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