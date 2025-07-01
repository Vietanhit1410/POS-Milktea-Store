package org.example.cnjava_milkteastore.frontend.ui.management;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.employee.dto.EmployeeDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.theme.Theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmployeeManagement extends JPanel {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnReload;
    private JPanel formPanel;
    private JTextField tfID, tfCode, tfName, tfAddress, tfGender, tfBirthDate, tfPhone;
    private boolean isEditing = false;
    private int editingRow = -1;
    private List<EmployeeDTO> allEmployees;
    private GetAPI<EmployeeDTO> employeeAPI = new GetAPI<>(new TypeReference<>() {}, EmployeeDTO.class, new TypeReference<>() {});

    public EmployeeManagement() {
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

        JLabel title = new JLabel("👨‍💼 Employee Management", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.DARK_PINK);

        JLabel tabLabel = new JLabel("📋 Employee Tab", SwingConstants.RIGHT);
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabLabel.setForeground(Color.DARK_GRAY);

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(tabLabel, BorderLayout.EAST);
        return titlePanel;
    }

    private JPanel createFormPanel() {
        formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBackground(Theme.LIGHTER_PINK);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK), "Employee Information",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));
        formPanel.setVisible(false);

        tfID = createField("ID");
        tfCode = createField("Employee Code");
        tfName = createField("Name");
        tfAddress = createField("Address");
        tfGender = createField("Gender");
        tfBirthDate = createField("Birth Date (yyyy-MM-dd)");
        tfPhone = createField("Phone");

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        Theme.configureButton(btnSave);
        Theme.configureButton(btnCancel);

        btnSave.addActionListener(e -> saveEmployee());
        btnCancel.addActionListener(e -> formPanel.setVisible(false));

        formPanel.add(tfID);
        formPanel.add(tfCode);
        formPanel.add(tfName);
        formPanel.add(tfAddress);
        formPanel.add(tfGender);
        formPanel.add(tfBirthDate);
        formPanel.add(tfPhone);
        formPanel.add(new JLabel());
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
        String[] columns = {"ID", "Code", "Name", "Address", "Gender", "Birth Date", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(Theme.FONT_TEXT);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        employeeTable.setBackground(Theme.LIGHTER_PINK);
        employeeTable.setForeground(Color.BLACK);
        employeeTable.setGridColor(Theme.DARK_PINK);
        employeeTable.setSelectionBackground(Theme.DARK_PINK.brighter());
        employeeTable.setSelectionForeground(Color.WHITE);
        employeeTable.setShowGrid(true);
        employeeTable.setIntercellSpacing(new Dimension(1, 1));

        // Adjust column widths
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Code
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Name
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Address
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Gender
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Birth Date
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Phone

        // Center-align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        employeeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        employeeTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Code
        employeeTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Gender
        employeeTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Birth Date
        employeeTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Phone

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBackground(Theme.BG_PINK);
        scrollPane.getViewport().setBackground(Theme.LIGHTER_PINK);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), "Employee List",
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
        editingRow = employeeTable.getSelectedRow();

        if (editing) {
            if (editingRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a row to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tfID.setText(tableModel.getValueAt(editingRow, 0).toString());
            tfCode.setText(tableModel.getValueAt(editingRow, 1).toString());
            tfName.setText(tableModel.getValueAt(editingRow, 2).toString());
            tfAddress.setText(tableModel.getValueAt(editingRow, 3).toString());
            tfGender.setText(tableModel.getValueAt(editingRow, 4).toString());
            tfBirthDate.setText(tableModel.getValueAt(editingRow, 5).toString());
            tfPhone.setText(tableModel.getValueAt(editingRow, 6).toString());

            tfID.setEditable(false);
            tfCode.setEditable(false);
        } else {
            for (JTextField tf : new JTextField[]{tfID, tfCode, tfName, tfAddress, tfGender, tfBirthDate, tfPhone}) {
                tf.setText("");
                tf.setEditable(true);
            }
            tfID.setText("0");
            tfCode.setText("0");
            tfID.setEditable(false);
            tfCode.setEditable(false);
        }

        formPanel.setVisible(true);
    }

    private void saveEmployee() {
        try {
            // Validate and parse input fields
            String idText = tfID.getText().trim();
            String code = tfCode.getText().trim();
            String name = tfName.getText().trim();
            String address = tfAddress.getText().trim();
            String gender = tfGender.getText().trim();
            String birthDateText = tfBirthDate.getText().trim();
            String phone = tfPhone.getText().trim();

            // Validation
            if (idText.isEmpty() || code.isEmpty() || name.isEmpty() || address.isEmpty() ||
                    gender.isEmpty() || birthDateText.isEmpty() || phone.isEmpty()) {
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

            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthDateText, DateTimeFormatter.ISO_LOCAL_DATE);
                if (birthDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Birth date cannot be in the future.");
                }
            } catch (DateTimeParseException | IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid Birth Date format. Use yyyy-MM-dd and ensure it's not in the future.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate gender
            if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
                JOptionPane.showMessageDialog(this, "Gender must be 'Male' or 'Female'.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone (basic format, e.g., 10 digits)
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone must be a 10-digit number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create EmployeeDTO
            EmployeeDTO employee = new EmployeeDTO(id, code, name, address, gender, birthDate, phone);

            // Update table model
            Object[] rowData = {
                    employee.getID(),
                    employee.getEmployeeCode(),
                    employee.getName(),
                    employee.getAddress(),
                    employee.getGender(),
                    employee.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    employee.getPhone()
            };

            // Save to API
            employeeAPI.postToApi(employee, success -> {
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
            JOptionPane.showMessageDialog(this, "Error saving employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int row = employeeTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int employeeId = Integer.parseInt(employeeTable.getValueAt(row, 0).toString());
                employeeAPI.deleteByIdFromApi(employeeId);
                JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
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
        employeeAPI.fetchFromApi(employeeDTOS -> {
            this.allEmployees = employeeDTOS;
            loadData();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing rows
        for (EmployeeDTO employee : allEmployees) {
            tableModel.addRow(new Object[]{
                    employee.getID(),
                    employee.getEmployeeCode(),
                    employee.getName(),
                    employee.getAddress(),
                    employee.getGender(),
                    employee.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    employee.getPhone()
            });
        }
    }
}