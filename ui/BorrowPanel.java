package ui;

import data.BorrowData;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import libsystem.Borrow;

public class BorrowPanel extends JPanel {

    private JTextField recordIdField;
    private JTextField bookIdField;
    private JTextField memberIdField;
    private JTextField borrowDateField;
    private JTextField dueDateField;
    private JComboBox<String> statusBox;
    private JTextField searchField;
    private JTextField startDateField;
    private JTextField endDateField;

    private JTable table;
    private DefaultTableModel tableModel;

    private BorrowData borrowData;

    public BorrowPanel() {

        borrowData = new BorrowData();

        setLayout(new BorderLayout(15, 15));

        JPanel mainTopPanel = new JPanel(new BorderLayout(15, 15));
        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 6, 15, 15));

        recordIdField = new JTextField();
        bookIdField = new JTextField();
        memberIdField = new JTextField();
        borrowDateField = new JTextField();
        dueDateField = new JTextField();

        statusBox = new JComboBox<>(new String[]{
                "-- Select Status --",
                "Borrowed",
                "Returned",
                "Overdue"
        });

        inputPanel.add(new JLabel("Record ID"));
        inputPanel.add(new JLabel("Book ID"));
        inputPanel.add(new JLabel("Member ID"));
        inputPanel.add(new JLabel("Borrow Date (YYYY-MM-DD)"));
        inputPanel.add(new JLabel("Due Date (YYYY-MM-DD)"));
        inputPanel.add(new JLabel("Status"));

        inputPanel.add(recordIdField);
        inputPanel.add(bookIdField);
        inputPanel.add(memberIdField);
        inputPanel.add(borrowDateField);
        inputPanel.add(dueDateField);
        inputPanel.add(statusBox);

        JPanel searchPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search and Filter Borrow Records"));

        JPanel keywordSearchPanel = new JPanel(new BorderLayout(10, 10));

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");

        JLabel searchLabel = new JLabel("Search by record ID, book ID, member ID, or status");

        keywordSearchPanel.add(searchLabel, BorderLayout.NORTH);
        keywordSearchPanel.add(searchField, BorderLayout.CENTER);
        keywordSearchPanel.add(searchButton, BorderLayout.EAST);

        JPanel dateFilterPanel = new JPanel(new GridLayout(2, 5, 10, 10));

        startDateField = new JTextField();
        endDateField = new JTextField();

        JButton filterButton = new JButton("Filter Date Range");
        JButton overdueButton = new JButton("Show Overdue");

        dateFilterPanel.add(new JLabel("Start Date (YYYY-MM-DD)"));
        dateFilterPanel.add(new JLabel("End Date (YYYY-MM-DD)"));
        dateFilterPanel.add(new JLabel(""));
        dateFilterPanel.add(new JLabel(""));
        dateFilterPanel.add(new JLabel(""));

        dateFilterPanel.add(startDateField);
        dateFilterPanel.add(endDateField);
        dateFilterPanel.add(filterButton);
        dateFilterPanel.add(overdueButton);
        dateFilterPanel.add(new JLabel(""));

        searchPanel.add(keywordSearchPanel);
        searchPanel.add(dateFilterPanel);

        mainTopPanel.add(inputPanel, BorderLayout.CENTER);
        mainTopPanel.add(searchPanel, BorderLayout.SOUTH);

        add(mainTopPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        tableModel.addColumn("Record ID");
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Member ID");
        tableModel.addColumn("Borrow Date");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Status");

        table = new JTable(tableModel);

        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadBorrowRecords();

        addButton.addActionListener(e -> addBorrowRecord());

        updateButton.addActionListener(e -> updateBorrowRecord());

        deleteButton.addActionListener(e -> deleteBorrowRecord());

        refreshButton.addActionListener(e -> {
            loadBorrowRecords();
            clearFields();
            searchField.setText("");
            startDateField.setText("");
            endDateField.setText("");
        });

        searchButton.addActionListener(e -> searchBorrowRecords());

        searchField.addActionListener(e -> searchBorrowRecords());

        filterButton.addActionListener(e -> filterByDateRange());

        overdueButton.addActionListener(e -> showOverdueRecords());

        table.getSelectionModel().addListSelectionListener(e -> fillFieldsFromTable());
    }

    private void loadBorrowRecords() {

        tableModel.setRowCount(0);

        List<Borrow> records = borrowData.getAllBorrowRecords();

        for (Borrow record : records) {

            tableModel.addRow(new Object[]{
                    record.getRecordId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    record.getReturnStatus()
            });
        }
    }

    private boolean validateBorrowForm() {

        if (recordIdField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a Record ID");

            return false;
        }

        if (bookIdField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a Book ID");

            return false;
        }

        if (memberIdField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a Member ID");

            return false;
        }

        try {

            Integer.parseInt(recordIdField.getText().trim());
            Integer.parseInt(bookIdField.getText().trim());
            Integer.parseInt(memberIdField.getText().trim());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Record ID, Book ID and Member ID must be numeric");

            return false;
        }

        if (borrowDateField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a borrow date");

            return false;
        }

        if (dueDateField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a due date");

            return false;
        }

        if (!isValidDateFormat(borrowDateField.getText().trim())) {

            JOptionPane.showMessageDialog(this, "Borrow date must be in YYYY-MM-DD format");

            return false;
        }

        if (!isValidDateFormat(dueDateField.getText().trim())) {

            JOptionPane.showMessageDialog(this, "Due date must be in YYYY-MM-DD format");

            return false;
        }

        if (statusBox.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please select a valid status");

            return false;
        }

        return true;
    }

    private boolean isValidDateFormat(String date) {

        return date.matches("\\d{4}-\\d{2}-\\d{2}");
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

                JOptionPane.showMessageDialog(this, "Record ID already exists");

                return;
            }

            Borrow record = new Borrow(
                    recordId,
                    Integer.parseInt(bookIdField.getText().trim()),
                    Integer.parseInt(memberIdField.getText().trim()),
                    borrowDateField.getText().trim(),
                    dueDateField.getText().trim(),
                    statusBox.getSelectedItem().toString()
            );

            borrowData.addBorrowRecord(record);

            loadBorrowRecords();

            clearFields();

            JOptionPane.showMessageDialog(this, "Borrow record added successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
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
                    statusBox.getSelectedItem().toString()
            );

            borrowData.updateBorrowRecord(record);

            loadBorrowRecords();

            clearFields();

            JOptionPane.showMessageDialog(this, "Borrow record updated successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void deleteBorrowRecord() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected borrow record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                int recordId = Integer.parseInt(recordIdField.getText().trim());

                borrowData.deleteBorrowRecord(recordId);

                loadBorrowRecords();

                clearFields();

                JOptionPane.showMessageDialog(this, "Borrow record deleted successfully");

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, "Select a valid borrow record");
            }
        }
    }

    private void searchBorrowRecords() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadBorrowRecords();

            return;
        }

        tableModel.setRowCount(0);

        List<Borrow> records = borrowData.searchBorrowRecords(keyword);

        for (Borrow record : records) {

            tableModel.addRow(new Object[]{
                    record.getRecordId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    record.getReturnStatus()
            });
        }
    }

    private void filterByDateRange() {

        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();

        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {

            JOptionPane.showMessageDialog(this, "Start and end dates must be in YYYY-MM-DD format");

            return;
        }

        tableModel.setRowCount(0);

        List<Borrow> records = borrowData.filterByDateRange(startDate, endDate);

        for (Borrow record : records) {

            tableModel.addRow(new Object[]{
                    record.getRecordId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    record.getReturnStatus()
            });
        }
    }

    private void showOverdueRecords() {

        tableModel.setRowCount(0);

        List<Borrow> records = borrowData.getOverdueRecords();

        for (Borrow record : records) {

            tableModel.addRow(new Object[]{
                    record.getRecordId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    record.getReturnStatus()
            });
        }
    }

    private void fillFieldsFromTable() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            int modelRow = table.convertRowIndexToModel(row);

            recordIdField.setText(tableModel.getValueAt(modelRow, 0).toString());

            bookIdField.setText(tableModel.getValueAt(modelRow, 1).toString());

            memberIdField.setText(tableModel.getValueAt(modelRow, 2).toString());

            borrowDateField.setText(tableModel.getValueAt(modelRow, 3).toString());

            dueDateField.setText(tableModel.getValueAt(modelRow, 4).toString());

            statusBox.setSelectedItem(tableModel.getValueAt(modelRow, 5).toString());

            recordIdField.setEditable(false);
        }
    }

    private void clearFields() {

        recordIdField.setText("");

        bookIdField.setText("");

        memberIdField.setText("");

        borrowDateField.setText("");

        dueDateField.setText("");

        statusBox.setSelectedIndex(0);

        recordIdField.setEditable(true);

        table.clearSelection();
    }
}