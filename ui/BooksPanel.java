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
    private JTextField categoryField;
    private JTextField statusField;
    private JTextField searchField;

    private JTable table;
    private DefaultTableModel tableModel;

    private BookData bookData;

    public BooksPanel() {

        bookData = new BookData();

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 6, 10, 10));

        bookIdField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();
        categoryField = new JTextField();
        statusField = new JTextField();
        searchField = new JTextField();

        topPanel.add(new JLabel("Book ID"));
        topPanel.add(new JLabel("Title"));
        topPanel.add(new JLabel("Author"));
        topPanel.add(new JLabel("Category"));
        topPanel.add(new JLabel("Status"));
        topPanel.add(new JLabel("Search"));

        topPanel.add(bookIdField);
        topPanel.add(titleField);
        topPanel.add(authorField);
        topPanel.add(categoryField);
        topPanel.add(statusField);
        topPanel.add(searchField);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("Book ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Category");
        tableModel.addColumn("Status");

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        JButton searchButton = new JButton("Search");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(searchButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();

        addButton.addActionListener(e -> addBook());

        updateButton.addActionListener(e -> updateBook());

        deleteButton.addActionListener(e -> deleteBook());

        refreshButton.addActionListener(e -> loadBooks());

        searchButton.addActionListener(e -> searchBooks());

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

    private void addBook() {

        try {

            Books book = new Books(
                    Integer.parseInt(bookIdField.getText()),
                    titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    statusField.getText()
            );

            bookData.addBook(book);

            loadBooks();

            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void updateBook() {

        try {

            Books book = new Books(
                    Integer.parseInt(bookIdField.getText()),
                    titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    statusField.getText()
            );

            bookData.updateBook(book);

            loadBooks();

            clearFields();

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

                int bookId = Integer.parseInt(bookIdField.getText());

                bookData.deleteBook(bookId);

                loadBooks();

                clearFields();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, "Select a valid book");
            }
        }
    }

    private void searchBooks() {

        tableModel.setRowCount(0);

        List<Books> books = bookData.searchBooks(searchField.getText());

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

            bookIdField.setText(tableModel.getValueAt(row, 0).toString());
            titleField.setText(tableModel.getValueAt(row, 1).toString());
            authorField.setText(tableModel.getValueAt(row, 2).toString());
            categoryField.setText(tableModel.getValueAt(row, 3).toString());
            statusField.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    private void clearFields() {

        bookIdField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        statusField.setText("");
    }
}