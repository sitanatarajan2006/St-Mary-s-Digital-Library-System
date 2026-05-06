package ui;

import java.util.List;

import data.MemberData;
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
import libsystem.Members;

public class MembersPanel {

    private VBox view;

    private TextField memberIdField;
    private TextField memberNameField;
    private TextField emailField;
    private ComboBox<String> membershipTypeBox;
    private TextField searchField;

    private TableView<Members> table;
    private ObservableList<Members> memberList;

    private MemberData memberData;

    public MembersPanel() {

        memberData = new MemberData();

        view = new VBox(15);
        view.setStyle("-fx-padding: 20;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(10);

        memberIdField = new TextField();
        memberIdField.setPromptText("Numeric ID");

        memberNameField = new TextField();
        memberNameField.setPromptText("Member name");

        emailField = new TextField();
        emailField.setPromptText("Email address");

        membershipTypeBox = new ComboBox<>();
        membershipTypeBox.getItems().addAll(
                "-- Select Membership Type --",
                "Student",
                "Staff"
        );
        membershipTypeBox.getSelectionModel().selectFirst();
        membershipTypeBox.setMaxWidth(Double.MAX_VALUE);

        inputGrid.add(new Label("Member ID"), 0, 0);
        inputGrid.add(new Label("Name"), 1, 0);
        inputGrid.add(new Label("Email"), 2, 0);
        inputGrid.add(new Label("Membership Type"), 3, 0);

        inputGrid.add(memberIdField, 0, 1);
        inputGrid.add(memberNameField, 1, 1);
        inputGrid.add(emailField, 2, 1);
        inputGrid.add(membershipTypeBox, 3, 1);

        VBox searchBox = new VBox(8);
        searchBox.setStyle("-fx-padding: 10 0 10 0;");

        Label searchLabel = new Label("Search Members");
        Label searchHint = new Label("Search by ID or name");

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

        TableColumn<Members, Integer> idColumn = new TableColumn<>("Member ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        TableColumn<Members, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));

        TableColumn<Members, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Members, String> typeColumn = new TableColumn<>("Membership Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        table.getColumns().addAll(idColumn, nameColumn, emailColumn, typeColumn);

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

        loadMembers();

        addButton.setOnAction(e -> addMember());

        updateButton.setOnAction(e -> updateMember());

        deleteButton.setOnAction(e -> deleteMember());

        refreshButton.setOnAction(e -> {
            loadMembers();
            clearFields();
            searchField.setText("");
        });

        searchButton.setOnAction(e -> searchMembers());

        searchField.setOnAction(e -> searchMembers());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> fillFieldsFromTable(newSelection));
    }

    private void loadMembers() {

        List<Members> members = memberData.getAllMembers();

        memberList = FXCollections.observableArrayList(members);

        table.setItems(memberList);
    }

    private boolean validateMemberForm() {

        if (memberIdField.getText().trim().isEmpty()) {

            showError("Please enter a Member ID");

            return false;
        }

        try {

            Integer.parseInt(memberIdField.getText().trim());

        } catch (Exception e) {

            showError("Member ID must be numeric");

            return false;
        }

        if (memberNameField.getText().trim().isEmpty()) {

            showError("Please enter a member name");

            return false;
        }

        if (emailField.getText().trim().isEmpty()) {

            showError("Please enter an email address");

            return false;
        }

        if (!emailField.getText().trim().contains("@") || !emailField.getText().trim().contains(".")) {

            showError("Please enter a valid email address");

            return false;
        }

        if (membershipTypeBox.getSelectionModel().getSelectedIndex() == 0) {

            showError("Please select a valid membership type");

            return false;
        }

        return true;
    }

    private boolean memberIdExists(int memberId) {

        List<Members> members = memberData.getAllMembers();

        for (Members member : members) {

            if (member.getMemberId() == memberId) {

                return true;
            }
        }

        return false;
    }

    private void addMember() {

        if (!validateMemberForm()) {

            return;
        }

        try {

            int memberId = Integer.parseInt(memberIdField.getText().trim());

            if (memberIdExists(memberId)) {

                showError("Member ID already exists");

                return;
            }

            Members member = new Members(
                    memberId,
                    memberNameField.getText().trim(),
                    emailField.getText().trim(),
                    membershipTypeBox.getSelectionModel().getSelectedItem()
            );

            memberData.addMember(member);

            loadMembers();

            clearFields();

            showInfo("Member added successfully");

        } catch (Exception e) {

            showError("Invalid input");
        }
    }

    private void updateMember() {

        if (!validateMemberForm()) {

            return;
        }

        try {

            Members member = new Members(
                    Integer.parseInt(memberIdField.getText().trim()),
                    memberNameField.getText().trim(),
                    emailField.getText().trim(),
                    membershipTypeBox.getSelectionModel().getSelectedItem()
            );

            memberData.updateMember(member);

            loadMembers();

            clearFields();

            showInfo("Member updated successfully");

        } catch (Exception e) {

            showError("Invalid input");
        }
    }

    private void deleteMember() {

        if (memberIdField.getText().trim().isEmpty()) {

            showError("Select a valid member");

            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.setTitle("Confirm Delete");

        confirm.setHeaderText(null);

        confirm.setContentText("Delete selected member?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {

            return;
        }

        try {

            int memberId = Integer.parseInt(memberIdField.getText().trim());

            memberData.deleteMember(memberId);

            loadMembers();

            clearFields();

            showInfo("Member deleted successfully");

        } catch (Exception e) {

            showError("Select a valid member");
        }
    }

    private void searchMembers() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadMembers();

            return;
        }

        List<Members> members = memberData.searchMembers(keyword);

        memberList = FXCollections.observableArrayList(members);

        table.setItems(memberList);
    }

    private void fillFieldsFromTable(Members member) {

        if (member != null) {

            memberIdField.setText(String.valueOf(member.getMemberId()));

            memberNameField.setText(member.getMemberName());

            emailField.setText(member.getEmail());

            membershipTypeBox.getSelectionModel().select(member.getMembershipType());

            memberIdField.setEditable(false);
        }
    }

    private void clearFields() {

        memberIdField.setText("");

        memberNameField.setText("");

        emailField.setText("");

        membershipTypeBox.getSelectionModel().selectFirst();

        memberIdField.setEditable(true);

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