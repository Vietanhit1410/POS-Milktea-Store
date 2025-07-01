package org.example.cnjava_milkteastore.frontend.ui.management;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.account.dto.AccountDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.theme.Theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccountManagement extends JPanel {

    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnReload;
    private JPanel formPanel;
    private JTextField tfID, tfUserName, tfRole, tfEmployeeID;
    private JPasswordField tfPassword; // Use JPasswordField for password
    private boolean isEditing = false;
    private int editingRow = -1;
    private List<AccountDTO> allAccounts;
    private GetAPI<AccountDTO> accountAPI = new GetAPI<>(new TypeReference<>() {}, AccountDTO.class, new TypeReference<>() {});

    public AccountManagement() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_PINK);

        add(createTitlePanel(), BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Theme.BG_PINK);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 20));

        contentWrapper.add(createFormPanel(), BorderLayout.NORTH);
        contentWrapper.add(createTablePanel(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        reloadData();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Theme.BG_PINK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("🔒 Account Management", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.DARK_PINK);

        JLabel tabLabel = new JLabel("👤 Account Tab", SwingConstants.RIGHT);
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabLabel.setForeground(Color.DARK_GRAY);

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(tabLabel, BorderLayout.EAST);
        return titlePanel;
    }

    private JPanel createFormPanel() {
        formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.setBackground(Theme.LIGHTER_PINK);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK), "Account Information",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));
        formPanel.setVisible(false);

        tfID = createField("ID");
        tfUserName = createField("User Name");
        tfPassword = new JPasswordField();
        tfPassword.setFont(Theme.FONT_TEXT);
        tfPassword.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK), "Password",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                Theme.FONT_TEXT, Theme.DARK_PINK));
        tfRole = createField("Role");
        tfEmployeeID = createField("Employee ID");

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        Theme.configureButton(btnSave);
        Theme.configureButton(btnCancel);

        btnSave.addActionListener(e -> saveAccount());
        btnCancel.addActionListener(e -> formPanel.setVisible(false));

        formPanel.add(tfID);
        formPanel.add(tfUserName);
        formPanel.add(tfPassword);
        formPanel.add(tfRole);
        formPanel.add(tfEmployeeID);
        formPanel.add(btnSave);
        formPanel.add(btnCancel);

        return formPanel;
    }

    private JTextField createField(String title) {
        JTextField tf = new JTextField();
        tf.setFont(Theme.FONT_TEXT);
        tf.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK), title,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                Theme.FONT_TEXT, Theme.DARK_PINK));
        return tf;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "User Name", "Password", "Role", "Employee ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Object getValueAt(int row, int column) {
                // Mask password in table
                if (column == 2) {
                    return "********";
                }
                return super.getValueAt(row, column);
            }
        };
        accountTable = new JTable(tableModel);
        accountTable.setFont(Theme.FONT_TEXT);
        accountTable.setRowHeight(25);
        accountTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        accountTable.setBackground(Theme.LIGHTER_PINK);
        accountTable.setForeground(Color.BLACK);
        accountTable.setGridColor(Theme.DARK_PINK);
        accountTable.setSelectionBackground(Theme.DARK_PINK.brighter());
        accountTable.setSelectionForeground(Color.WHITE);
        accountTable.setShowGrid(true);
        accountTable.setIntercellSpacing(new Dimension(1, 1));

        // Adjust column widths
        accountTable.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        accountTable.getColumnModel().getColumn(1).setPreferredWidth(150); // User Name
        accountTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Password
        accountTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Role
        accountTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Employee ID

        // Center-align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        accountTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        accountTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Role
        accountTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Employee ID

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBackground(Theme.BG_PINK);
        scrollPane.getViewport().setBackground(Theme.LIGHTER_PINK);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), "Account List",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));

        return scrollPane;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panel.setBackground(Theme.BG_PINK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnAdd = new JButton("➕ Add");
        btnEdit = new JButton("✏️ Edit");
        btnDelete = new JButton("❌ Delete");
        btnReload = new JButton("🔃 Reload");

        JButton[] buttons = {btnAdd, btnEdit, btnDelete, btnReload};
        for (JButton btn : buttons) {
            Theme.configureButton(btn);
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        }

        btnAdd.addActionListener(e -> showForm(false));
        btnEdit.addActionListener(e -> showForm(true));
        btnDelete.addActionListener(e -> deleteSelected());
        btnReload.addActionListener(e -> reloadData());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnReload);

        return panel;
    }

    private void showForm(boolean editing) {
        isEditing = editing;
        editingRow = accountTable.getSelectedRow();

        if (editing) {
            if (editingRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a row to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tfID.setText(tableModel.getValueAt(editingRow, 0).toString());
            tfUserName.setText(tableModel.getValueAt(editingRow, 1).toString());
            tfPassword.setText(""); // Don't pre-fill password for security
            tfRole.setText(tableModel.getValueAt(editingRow, 3).toString());
            tfEmployeeID.setText(tableModel.getValueAt(editingRow, 4).toString());

            tfID.setEditable(false);
            tfUserName.setEditable(false);
        } else {
            for (JTextField tf : new JTextField[]{tfID, tfUserName, tfRole, tfEmployeeID}) {
                tf.setText("");
                tf.setEditable(true);
            }
            tfPassword.setText("");
            tfID.setText("0");
            tfID.setEditable(false);
        }

        formPanel.setVisible(true);
    }

    private void saveAccount() {
        try {
            // Validate and parse input fields
            String idText = tfID.getText().trim();
            String userName = tfUserName.getText().trim();
            String password = new String(tfPassword.getPassword()).trim();
            String role = tfRole.getText().trim();
            String employeeIdText = tfEmployeeID.getText().trim();

            // Validation
            if (idText.isEmpty() || userName.isEmpty() || password.isEmpty() || role.isEmpty() || employeeIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idText);
                if (id < 0) throw new NumberFormatException("ID must be positive.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("Employee")) {
                JOptionPane.showMessageDialog(this, "Role must be 'Admin' or 'Employee'.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int employeeID;
            try {
                employeeID = Integer.parseInt(employeeIdText);
                if (employeeID <= 0) throw new NumberFormatException("Employee ID must be positive.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Employee ID format. Please enter a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create AccountDTO
            AccountDTO account = new AccountDTO(id, userName, password, role, employeeID);

            // Update table model
            Object[] rowData = {
                    account.getID(),
                    account.getUserName(),
                    "********", // Mask password in table
                    account.getRole(),
                    account.getEmployeeID()
            };

            // Save to API
            accountAPI.postToApi(account, success -> {
                if (isEditing && editingRow >= 0) {
                    for (int i = 0; i < rowData.length; i++) {
                        tableModel.setValueAt(rowData[i], editingRow, i);
                    }
                } else {
                    tableModel.addRow(rowData);
                }
                reloadData();
            });

            formPanel.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int row = accountTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this account?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int accountId = Integer.parseInt(accountTable.getValueAt(row, 0).toString());
                accountAPI.deleteByIdFromApi(accountId);
                JOptionPane.showMessageDialog(this, "Account deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                tableModel.removeRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a row to delete.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void reloadData() {
        accountAPI.fetchFromApi(accountDTOS -> {
            this.allAccounts = accountDTOS;
            loadData();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing rows
        for (AccountDTO account : allAccounts) {
            tableModel.addRow(new Object[]{
                    account.getID(),
                    account.getUserName(),
                    "********", // Mask password
                    account.getRole(),
                    account.getEmployeeID()
            });
        }
    }
}