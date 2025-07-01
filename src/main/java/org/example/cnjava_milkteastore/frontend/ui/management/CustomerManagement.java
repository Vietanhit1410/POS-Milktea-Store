package org.example.cnjava_milkteastore.frontend.ui.management;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.customer.dto.CustomerDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.theme.Theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerManagement extends JPanel {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnReload;
    private JPanel formPanel;
    private JTextField tfID, tfCustomerCode, tfCustomerName, tfCustomerPhone, tfPoints;
    private boolean isEditing = false;
    private int editingRow = -1;
    private List<CustomerDTO> allCustomers;
    private GetAPI<CustomerDTO> customerAPI = new GetAPI<>(new TypeReference<>() {}, CustomerDTO.class, new TypeReference<>() {});

    public CustomerManagement() {
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

        JLabel title = new JLabel("👥 Customer Management", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.DARK_PINK);

        JLabel tabLabel = new JLabel("🛍️ Customer Tab", SwingConstants.RIGHT);
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
                BorderFactory.createLineBorder(Theme.DARK_PINK), "Customer Information",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));
        formPanel.setVisible(false);

        tfID = createField("ID");
        tfCustomerCode = createField("Customer Code");
        tfCustomerName = createField("Customer Name");
        tfCustomerPhone = createField("Customer Phone");
        tfPoints = createField("Points");

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        Theme.configureButton(btnSave);
        Theme.configureButton(btnCancel);

        btnSave.addActionListener(e -> saveCustomer());
        btnCancel.addActionListener(e -> formPanel.setVisible(false));

        formPanel.add(tfID);
        formPanel.add(tfCustomerCode);
        formPanel.add(tfCustomerName);
        formPanel.add(tfCustomerPhone);
        formPanel.add(tfPoints);
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
        String[] columns = {"ID", "Customer Code", "Customer Name", "Customer Phone", "Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(Theme.FONT_TEXT);
        customerTable.setRowHeight(25);
        customerTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        customerTable.setBackground(Theme.LIGHTER_PINK);
        customerTable.setForeground(Color.BLACK);
        customerTable.setGridColor(Theme.DARK_PINK);
        customerTable.setSelectionBackground(Theme.DARK_PINK.brighter());
        customerTable.setSelectionForeground(Color.WHITE);
        customerTable.setShowGrid(true);
        customerTable.setIntercellSpacing(new Dimension(1, 1));

        // Adjust column widths
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Customer Code
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Customer Name
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Customer Phone
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Points

        // Center-align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        customerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        customerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Customer Code
        customerTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Customer Phone
        customerTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Points

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBackground(Theme.BG_PINK);
        scrollPane.getViewport().setBackground(Theme.LIGHTER_PINK);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), "Customer List",
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
        editingRow = customerTable.getSelectedRow();

        if (editing) {
            if (editingRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a row to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tfID.setText(tableModel.getValueAt(editingRow, 0).toString());
            tfCustomerCode.setText(tableModel.getValueAt(editingRow, 1).toString());
            tfCustomerName.setText(tableModel.getValueAt(editingRow, 2).toString());
            tfCustomerPhone.setText(tableModel.getValueAt(editingRow, 3).toString());
            tfPoints.setText(tableModel.getValueAt(editingRow, 4).toString());

            tfID.setEditable(false);
            tfCustomerCode.setEditable(false);
        } else {
            for (JTextField tf : new JTextField[]{tfID, tfCustomerCode, tfCustomerName, tfCustomerPhone, tfPoints}) {
                tf.setText("");
                tf.setEditable(true);
            }
            tfID.setText("0");
            tfCustomerCode.setText("0");
            tfID.setEditable(false);
            tfCustomerCode.setEditable(true);
        }

        formPanel.setVisible(true);
    }

    private void saveCustomer() {
        try {
            // Validate and parse input fields
            String idText = tfID.getText().trim();
            String customerCode = tfCustomerCode.getText().trim();
            String customerName = tfCustomerName.getText().trim();
            String customerPhone = tfCustomerPhone.getText();
            String pointsText = tfPoints.getText().trim();

            // Validation
            if (idText.isEmpty() || customerCode.isEmpty() || customerName.isEmpty() || customerPhone.isEmpty() || pointsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idText);
                if (id <= 0) {
                    throw new NumberFormatException("ID must be positive.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!customerPhone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Customer phone must be a 10-digit number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int points;
            try {
                points = Integer.parseInt(pointsText);
                if (points < 0) {
                    throw new NumberFormatException("Points cannot be negative.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Points format. Please enter a non-negative integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create CustomerDTO
            CustomerDTO customer = new CustomerDTO(id, customerCode, customerName, customerPhone, points);

            // Update table model
            Object[] rowData = {
                    customer.getID(),
                    customer.getCustomerCode(),
                    customer.getCustomerName(),
                    customer.getCustomerPhone(),
                    customer.getPoints()
            };

            // Save to API
            customerAPI.postToApi(customer, success -> {
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
            JOptionPane.showMessageDialog(this, "Error saving customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this customer?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int customerId = Integer.parseInt(customerTable.getValueAt(row, 0).toString());
                customerAPI.deleteByIdFromApi(customerId);
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
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
        customerAPI.fetchFromApi(customerDTOS -> {
            this.allCustomers = customerDTOS;
            loadData();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing rows
        for (CustomerDTO customer : allCustomers) {
            tableModel.addRow(new Object[]{
                    customer.getID(),
                    customer.getCustomerCode(),
                    customer.getCustomerName(),
                    customer.getCustomerPhone(),
                    customer.getPoints()
            });
        }
    }
}