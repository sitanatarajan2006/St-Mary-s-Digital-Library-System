package ui;

import data.BookData;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import libsystem.Books;

public class BooksPanel extends JPanel {

    private JTextField bookIdField;
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> statusBox;
    private JTextField searchField;

    private JTable table;
    private DefaultTableModel tableModel;

    private BookData bookData;

    public BooksPanel() {

        bookData = new BookData();

        setLayout(new BorderLayout(15, 15));

        JPanel mainTopPanel = new JPanel(new BorderLayout(15, 15));

        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 15, 15));

        bookIdField = new JTextField();

        titleField = new JTextField();

        authorField = new JTextField();

        categoryBox = new JComboBox<>(new String[]{
                "-- Select Category --",
                "Programming",
                "Science",
                "History",
                "Engineering",
                "Business",
                "Mathematics",
                "Literature",
                "Other"
        });

        statusBox = new JComboBox<>(new String[]{
                "-- Select Status --",
                "Available",
                "Borrowed",
                "Reserved",
                "Overdue"
        });

        inputPanel.add(new JLabel("Book ID"));
        inputPanel.add(new JLabel("Title"));
        inputPanel.add(new JLabel("Author"));
        inputPanel.add(new JLabel("Category"));
        inputPanel.add(new JLabel("Status"));

        inputPanel.add(bookIdField);
        inputPanel.add(titleField);
        inputPanel.add(authorField);
        inputPanel.add(categoryBox);
        inputPanel.add(statusBox);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");

        JLabel searchLabel = new JLabel("Search by ID, title, author, or category");

        searchPanel.add(searchLabel, BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        mainTopPanel.add(inputPanel, BorderLayout.CENTER);
        mainTopPanel.add(searchPanel, BorderLayout.SOUTH);

        add(mainTopPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        tableModel.addColumn("Book ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Category");
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

        loadBooks();

        addButton.addActionListener(e -> addBook());

        updateButton.addActionListener(e -> updateBook());

        deleteButton.addActionListener(e -> deleteBook());

        refreshButton.addActionListener(e -> {
            loadBooks();
            clearFields();
            searchField.setText("");
        });

        searchButton.addActionListener(e -> searchBooks());

        searchField.addActionListener(e -> searchBooks());

        table.getSelectionModel().addListSelectionListener(e -> fillFieldsFromTable());
    }

    private void loadBooks() {

        tableModel.setRowCount(0);

        List<Books> books = bookData.getAllBooks();

        for (Books book : books) {

            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getAvailabilityStatus()
            });
        }
    }

    private boolean validateBookForm() {

        if (bookIdField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a Book ID");

            return false;
        }

        try {

            Integer.parseInt(bookIdField.getText().trim());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Book ID must be numeric");

            return false;
        }

        if (titleField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a title");

            return false;
        }

        if (authorField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter an author");

            return false;
        }

        if (categoryBox.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please select a valid category");

            return false;
        }

        if (statusBox.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please select a valid status");

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

                JOptionPane.showMessageDialog(this, "Book ID already exists");

                return;
            }

            Books book = new Books(
                    bookId,
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    categoryBox.getSelectedItem().toString(),
                    statusBox.getSelectedItem().toString()
            );

            bookData.addBook(book);

            loadBooks();

            clearFields();

            JOptionPane.showMessageDialog(this, "Book added successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
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
                    categoryBox.getSelectedItem().toString(),
                    statusBox.getSelectedItem().toString()
            );

            bookData.updateBook(book);

            loadBooks();

            clearFields();

            JOptionPane.showMessageDialog(this, "Book updated successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void deleteBook() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                int bookId = Integer.parseInt(bookIdField.getText().trim());

                bookData.deleteBook(bookId);

                loadBooks();

                clearFields();

                JOptionPane.showMessageDialog(this, "Book deleted successfully");

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, "Select a valid book");
            }
        }
    }

    private void searchBooks() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadBooks();

            return;
        }

        tableModel.setRowCount(0);

        List<Books> books = bookData.searchBooks(keyword);

        for (Books book : books) {

            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getAvailabilityStatus()
            });
        }
    }

    private void fillFieldsFromTable() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            int modelRow = table.convertRowIndexToModel(row);

            bookIdField.setText(tableModel.getValueAt(modelRow, 0).toString());

            titleField.setText(tableModel.getValueAt(modelRow, 1).toString());

            authorField.setText(tableModel.getValueAt(modelRow, 2).toString());

            categoryBox.setSelectedItem(tableModel.getValueAt(modelRow, 3).toString());

            statusBox.setSelectedItem(tableModel.getValueAt(modelRow, 4).toString());

            bookIdField.setEditable(false);
        }
    }

    private void clearFields() {

        bookIdField.setText("");

        titleField.setText("");

        authorField.setText("");

        categoryBox.setSelectedIndex(0);

        statusBox.setSelectedIndex(0);

        bookIdField.setEditable(true);

        table.clearSelection();
    }
}