package ui;

import data.MemberData;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import libsystem.Members;

public class MembersPanel extends JPanel {

    private JTextField memberIdField;
    private JTextField memberNameField;
    private JTextField emailField;
    private JComboBox<String> membershipTypeBox;
    private JTextField searchField;

    private JTable table;
    private DefaultTableModel tableModel;

    private MemberData memberData;

    public MembersPanel() {

        memberData = new MemberData();

        setLayout(new BorderLayout(15, 15));

        JPanel mainTopPanel = new JPanel(new BorderLayout(15, 15));
        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 15, 15));

        memberIdField = new JTextField();
        memberNameField = new JTextField();
        emailField = new JTextField();

        membershipTypeBox = new JComboBox<>(new String[]{
                "-- Select Membership Type --",
                "Student",
                "Staff"
        });

        inputPanel.add(new JLabel("Member ID"));
        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Email"));
        inputPanel.add(new JLabel("Membership Type"));

        inputPanel.add(memberIdField);
        inputPanel.add(memberNameField);
        inputPanel.add(emailField);
        inputPanel.add(membershipTypeBox);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Members"));

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");

        JLabel searchLabel = new JLabel("Search by ID or name");

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

        tableModel.addColumn("Member ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Membership Type");

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

        loadMembers();

        addButton.addActionListener(e -> addMember());

        updateButton.addActionListener(e -> updateMember());

        deleteButton.addActionListener(e -> deleteMember());

        refreshButton.addActionListener(e -> {
            loadMembers();
            clearFields();
            searchField.setText("");
        });

        searchButton.addActionListener(e -> searchMembers());

        searchField.addActionListener(e -> searchMembers());

        table.getSelectionModel().addListSelectionListener(e -> fillFieldsFromTable());
    }

    private void loadMembers() {

        tableModel.setRowCount(0);

        List<Members> members = memberData.getAllMembers();

        for (Members member : members) {

            tableModel.addRow(new Object[]{
                    member.getMemberId(),
                    member.getMemberName(),
                    member.getEmail(),
                    member.getMembershipType()
            });
        }
    }

    private boolean validateMemberForm() {

        if (memberIdField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a Member ID");

            return false;
        }

        try {

            Integer.parseInt(memberIdField.getText().trim());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Member ID must be numeric");

            return false;
        }

        if (memberNameField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a member name");

            return false;
        }

        if (emailField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter an email address");

            return false;
        }

        if (!emailField.getText().trim().contains("@") || !emailField.getText().trim().contains(".")) {

            JOptionPane.showMessageDialog(this, "Please enter a valid email address");

            return false;
        }

        if (membershipTypeBox.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please select a valid membership type");

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

                JOptionPane.showMessageDialog(this, "Member ID already exists");

                return;
            }

            Members member = new Members(
                    memberId,
                    memberNameField.getText().trim(),
                    emailField.getText().trim(),
                    membershipTypeBox.getSelectedItem().toString()
            );

            memberData.addMember(member);

            loadMembers();

            clearFields();

            JOptionPane.showMessageDialog(this, "Member added successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
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
                    membershipTypeBox.getSelectedItem().toString()
            );

            memberData.updateMember(member);

            loadMembers();

            clearFields();

            JOptionPane.showMessageDialog(this, "Member updated successfully");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void deleteMember() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                int memberId = Integer.parseInt(memberIdField.getText().trim());

                memberData.deleteMember(memberId);

                loadMembers();

                clearFields();

                JOptionPane.showMessageDialog(this, "Member deleted successfully");

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, "Select a valid member");
            }
        }
    }

    private void searchMembers() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadMembers();

            return;
        }

        tableModel.setRowCount(0);

        List<Members> members = memberData.searchMembers(keyword);

        for (Members member : members) {

            tableModel.addRow(new Object[]{
                    member.getMemberId(),
                    member.getMemberName(),
                    member.getEmail(),
                    member.getMembershipType()
            });
        }
    }

    private void fillFieldsFromTable() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            int modelRow = table.convertRowIndexToModel(row);

            memberIdField.setText(tableModel.getValueAt(modelRow, 0).toString());

            memberNameField.setText(tableModel.getValueAt(modelRow, 1).toString());

            emailField.setText(tableModel.getValueAt(modelRow, 2).toString());

            membershipTypeBox.setSelectedItem(tableModel.getValueAt(modelRow, 3).toString());

            memberIdField.setEditable(false);
        }
    }

    private void clearFields() {

        memberIdField.setText("");

        memberNameField.setText("");

        emailField.setText("");

        membershipTypeBox.setSelectedIndex(0);

        memberIdField.setEditable(true);

        table.clearSelection();
    }
}