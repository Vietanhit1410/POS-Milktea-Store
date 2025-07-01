package org.example.cnjava_milkteastore.frontend.ui.management;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.example.cnjava_milkteastore.backend.invoicedetail.dto.InvoiceDetailDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.theme.Theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoiceManagement extends JPanel {
    private JTable invoiceTable;
    private JTable invoiceDetailTable;
    private DefaultTableModel invoiceTableModel;
    private DefaultTableModel invoiceDetailTableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnReload, btnSearch;
    private JTextField tfSearch;
    private JTextField tfInvoiceCode, tfCustomerID, tfEmployeeID, tfDateCreated;
    private JPanel formPanel;

    // Components for date search
    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    private boolean isEditing = false;
    private int editingRow = -1;

    private List<InvoiceDTO> allInvoices = new ArrayList<>();
    private final Map<Integer, List<InvoiceDetailDTO>> invoiceDetailMap = new HashMap<>();

    private final GetAPI<InvoiceDTO> invoiceAPI = new GetAPI<>(new TypeReference<>() {}, InvoiceDTO.class, new TypeReference<>() {});
    private final GetAPI<InvoiceDetailDTO> invoiceDetailAPI = new GetAPI<>(new TypeReference<>() {}, InvoiceDetailDTO.class, new TypeReference<>() {});

    public InvoiceManagement() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_PINK);

        initializeDateComponents();

        add(createTitlePanel(), BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Theme.BG_PINK);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 20));

        contentWrapper.add(createFormPanel(), BorderLayout.NORTH);

        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.setBackground(Theme.BG_PINK);
        tableContainer.add(createInvoiceTablePanel(), BorderLayout.CENTER);
        tableContainer.add(createDetailTablePanel(), BorderLayout.SOUTH); // Bảng chi tiết ở đây
        contentWrapper.add(tableContainer, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        invoiceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow != -1) {
                    String invoiceCode = (String) invoiceTableModel.getValueAt(selectedRow, 0);
                    allInvoices.stream()
                            .filter(inv -> inv.getInvoiceCode().equals(invoiceCode))
                            .findFirst()
                            .ifPresent(invoice -> loadInvoiceDetails(invoice.getID()));
                } else {
                    invoiceDetailTableModel.setRowCount(0);
                }
            }
        });

        reloadData();
    }

    // ... (Các phương thức khác không thay đổi)

    private JScrollPane createInvoiceTablePanel() {
        invoiceTableModel = new DefaultTableModel(new Object[]{
                "Invoice Code", "Date Created", "Customer ID", "Employee ID",
                "Qty Drink", "Qty Food", "Price Drink", "Price Food", "Total Price"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceTable = new JTable(invoiceTableModel);
        invoiceTable.setFont(Theme.FONT_TEXT);
        invoiceTable.setRowHeight(25);
        invoiceTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        invoiceTable.setBackground(Theme.LIGHTER_PINK);

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK), "Invoice List",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK
        ));
        return scrollPane;
    }

    private JScrollPane createDetailTablePanel() {
        invoiceDetailTableModel = new DefaultTableModel(new Object[]{
                "Detail ID", "Invoice ID", "Product ID", "Product Name", "Type", "Quantity", "Price", "Subtotal"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceDetailTable = new JTable(invoiceDetailTableModel);

        // Apply consistent styling to match invoiceTable
        invoiceDetailTable.setFont(Theme.FONT_TEXT);
        invoiceDetailTable.setRowHeight(25);
        invoiceDetailTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        invoiceDetailTable.setBackground(Theme.LIGHTER_PINK);
        invoiceDetailTable.setForeground(Color.BLACK); // Ensure text color is consistent
        invoiceDetailTable.setGridColor(Theme.DARK_PINK); // Match grid color
        invoiceDetailTable.setSelectionBackground(Theme.DARK_PINK.brighter()); // Match selection background
        invoiceDetailTable.setSelectionForeground(Color.WHITE); // Match selection text color
        invoiceDetailTable.setShowGrid(true); // Ensure grid is visible
        invoiceDetailTable.setIntercellSpacing(new Dimension(1, 1)); // Consistent cell spacing

        // Adjust column widths for better appearance
        invoiceDetailTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Detail ID
        invoiceDetailTable.getColumnModel().getColumn(1).setPreferredWidth(80); // Invoice ID
        invoiceDetailTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Product ID
        invoiceDetailTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Product Name (wider for readability)
        invoiceDetailTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Type
        invoiceDetailTable.getColumnModel().getColumn(5).setPreferredWidth(60); // Quantity
        invoiceDetailTable.getColumnModel().getColumn(6).setPreferredWidth(80); // Price
        invoiceDetailTable.getColumnModel().getColumn(7).setPreferredWidth(80); // Subtotal

        // Center-align specific columns for better readability
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        invoiceDetailTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Detail ID
        invoiceDetailTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Invoice ID
        invoiceDetailTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Product ID
        invoiceDetailTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Type
        invoiceDetailTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Quantity

        // Right-align price and subtotal for numerical consistency
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        invoiceDetailTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer); // Price
        invoiceDetailTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer); // Subtotal

        JScrollPane scrollPane = new JScrollPane(invoiceDetailTable);
        scrollPane.setPreferredSize(new Dimension(0, 180));
        scrollPane.setBackground(Theme.BG_PINK); // Match background of scroll pane
        scrollPane.getViewport().setBackground(Theme.LIGHTER_PINK); // Match viewport background

        // Apply consistent border and title styling
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), // Thicker border for emphasis
                "Invoice Details",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Theme.DARK_PINK
        ));

        return scrollPane;
    }

    // ... (Các phương thức còn lại không thay đổi và được giữ nguyên như phiên bản trước)

    private void initializeDateComponents() {
        yearComboBox = new JComboBox<>();
        monthComboBox = new JComboBox<>();
        dayComboBox = new JComboBox<>();

        int currentYear = LocalDate.now().getYear();
        yearComboBox.addItem(null); // Add a null option for not filtering by date
        for (int i = currentYear - 5; i <= currentYear + 1; i++) yearComboBox.addItem(i);
        monthComboBox.addItem(null);
        for (int i = 1; i <= 12; i++) monthComboBox.addItem(i);
        dayComboBox.addItem(null);
        for (int i = 1; i <= 31; i++) dayComboBox.addItem(i);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Theme.BG_PINK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Invoice Management", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.DARK_PINK);

        JLabel tabLabel = new JLabel("Invoice Tab", SwingConstants.RIGHT);
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabLabel.setForeground(Color.DARK_GRAY);

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(tabLabel, BorderLayout.EAST);
        return titlePanel;
    }

    private JPanel createFormPanel() {
        formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.setBackground(Theme.LIGHTER_PINK);
        formPanel.setBorder(BorderFactory.createTitledBorder("Invoice Information"));
        formPanel.setVisible(false);

        tfInvoiceCode = createField("Invoice Code");
        tfCustomerID = createField("Customer ID");
        tfEmployeeID = createField("Employee ID");
        tfDateCreated = createField("Date Created");
        tfDateCreated.setEditable(false);

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        Theme.configureButton(btnSave);
        Theme.configureButton(btnCancel);

        btnSave.addActionListener(e -> saveInvoice());
        btnCancel.addActionListener(e -> formPanel.setVisible(false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Theme.LIGHTER_PINK);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        formPanel.add(tfInvoiceCode);
        formPanel.add(tfCustomerID);
        formPanel.add(tfEmployeeID);
        formPanel.add(tfDateCreated);
        formPanel.add(buttonPanel);

        return formPanel;
    }

    private JTextField createField(String title) {
        JTextField tf = new JTextField();
        tf.setFont(Theme.FONT_TEXT);
        tf.setBorder(BorderFactory.createTitledBorder(title));
        return tf;
    }

    private JPanel createControlPanel() {
        JPanel mainControlPanel = new JPanel(new BorderLayout(10, 10));
        mainControlPanel.setBackground(Theme.BG_PINK);
        mainControlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Theme.BG_PINK);

        tfSearch = new JTextField(15);
        Theme.configureTextField(tfSearch);
        tfSearch.setBorder(BorderFactory.createTitledBorder("Search by Invoice Code"));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(Theme.BG_PINK);
        datePanel.setBorder(BorderFactory.createTitledBorder("Search by Date"));
        datePanel.add(new JLabel("Năm:"));
        datePanel.add(yearComboBox);
        datePanel.add(new JLabel("Tháng:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("Ngày:"));
        datePanel.add(dayComboBox);

        btnSearch = new JButton("🔍 Search");
        Theme.configureButton(btnSearch);
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSearch.addActionListener(e -> performSearch());

        searchPanel.add(tfSearch);
        searchPanel.add(datePanel);
        searchPanel.add(btnSearch);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Theme.BG_PINK);

        btnAdd = new JButton("➕ Add");
        btnEdit = new JButton("✏️ Edit");
        btnDelete = new JButton("❌ Delete");
        btnReload = new JButton("🔃 Reload");

        JButton[] buttons = {btnAdd, btnEdit, btnDelete, btnReload};
        for (JButton btn : buttons) {
            Theme.configureButton(btn);
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        }

        btnAdd.addActionListener(e -> handleProtectedAction(() -> showForm(false)));
        btnEdit.addActionListener(e -> handleProtectedAction(() -> showForm(true)));
        btnDelete.addActionListener(e -> handleProtectedAction(this::deleteSelected));
        btnReload.addActionListener(e -> reloadData());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReload);

        mainControlPanel.add(searchPanel, BorderLayout.WEST);
        mainControlPanel.add(buttonPanel, BorderLayout.EAST);

        return mainControlPanel;
    }

    private void performSearch() {
        String codeInput = tfSearch.getText().trim();
        Integer year = (Integer) yearComboBox.getSelectedItem();
        Integer month = (Integer) monthComboBox.getSelectedItem();
        Integer day = (Integer) dayComboBox.getSelectedItem();

        List<InvoiceDTO> filteredInvoices;

        if (!codeInput.isEmpty()) {
            filteredInvoices = allInvoices.stream()
                    .filter(i -> i.getInvoiceCode().equalsIgnoreCase(codeInput))
                    .collect(Collectors.toList());
            yearComboBox.setSelectedItem(null);
            monthComboBox.setSelectedItem(null);
            dayComboBox.setSelectedItem(null);
        } else if (year != null && month != null && day != null) {
            try {
                LocalDate selectedDate = LocalDate.of(year, month, day);
                filteredInvoices = allInvoices.stream()
                        .filter(i -> i.getDateCreated().toLocalDate().equals(selectedDate))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            Stream<InvoiceDTO> stream = allInvoices.stream();
            if (year != null) {
                stream = stream.filter(i -> i.getDateCreated().getYear() == year);
            }
            if (month != null) {
                stream = stream.filter(i -> i.getDateCreated().getMonthValue() == month);
            }
            if (day != null) {
                stream = stream.filter(i -> i.getDateCreated().getDayOfMonth() == day);
            }
            filteredInvoices = stream.collect(Collectors.toList());
        }

        loadInvoicesToTable(filteredInvoices);
    }

    private void handleProtectedAction(Runnable action) {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                this, passwordField, "Enter Admin Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if ("admin".equals(password)) {
                action.run();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password!", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showForm(boolean isEditing) {
        this.isEditing = isEditing;
        if (isEditing) {
            editingRow = invoiceTable.getSelectedRow();
            if (editingRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select an invoice to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tfInvoiceCode.setText(invoiceTableModel.getValueAt(editingRow, 0).toString());
            tfDateCreated.setText(invoiceTableModel.getValueAt(editingRow, 1).toString());
            tfCustomerID.setText(invoiceTableModel.getValueAt(editingRow, 2).toString());
            tfEmployeeID.setText(invoiceTableModel.getValueAt(editingRow, 3).toString());
            tfInvoiceCode.setEditable(false);
        } else {
            tfInvoiceCode.setText("");
            tfCustomerID.setText("");
            tfEmployeeID.setText("");
            tfDateCreated.setText("");
            tfInvoiceCode.setEditable(true);
        }
        formPanel.setVisible(true);
    }

    private void saveInvoice() {
        String[] data = {
                tfInvoiceCode.getText(), tfDateCreated.getText(), tfCustomerID.getText(), tfEmployeeID.getText(),
                "0", "0", "0", "0", "0"
        };

        if (isEditing) {
            invoiceTableModel.setValueAt(data[0], editingRow, 0);
            invoiceTableModel.setValueAt(data[1], editingRow, 1);
            invoiceTableModel.setValueAt(data[2], editingRow, 2);
            invoiceTableModel.setValueAt(data[3], editingRow, 3);
        } else {
            invoiceTableModel.addRow(data);
        }
        formPanel.setVisible(false);
    }

    private void deleteSelected() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this invoice?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                invoiceTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an invoice to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void reloadData() {
        invoiceAPI.fetchFromApi(invoiceDTOS -> {
            this.allInvoices = invoiceDTOS;
            loadInvoicesToTable(this.allInvoices);
            invoiceDetailTableModel.setRowCount(0);
        });

        invoiceDetailAPI.fetchFromApi(detailList -> {
            invoiceDetailMap.clear();
            for (InvoiceDetailDTO detail : detailList) {
                invoiceDetailMap.computeIfAbsent(detail.getInvoiceID(), k -> new ArrayList<>()).add(detail);
            }
        });
        tfSearch.setText("");
        yearComboBox.setSelectedItem(null);
        monthComboBox.setSelectedItem(null);
        dayComboBox.setSelectedItem(null);
    }

    private void loadInvoicesToTable(List<InvoiceDTO> invoices) {
        invoiceTableModel.setRowCount(0);
        for (InvoiceDTO invoice : invoices) {
            invoiceTableModel.addRow(new Object[]{
                    invoice.getInvoiceCode(), invoice.getDateCreated(), invoice.getCustomerID(),
                    invoice.getEmployeeID(), invoice.getTotalQuantityDrink(), invoice.getTotalQuantityFood(),
                    invoice.getTotalPriceDrink(), invoice.getTotalPriceFood(), invoice.getTotalPrice()
            });
        }
    }

    private void loadInvoiceDetails(int invoiceID) {
        invoiceDetailTableModel.setRowCount(0);
        List<InvoiceDetailDTO> details = invoiceDetailMap.get(invoiceID);
        if (details != null) {
            for (InvoiceDetailDTO detail : details) {
                float subtotal = detail.getPrice() * detail.getQuantity();
                invoiceDetailTableModel.addRow(new Object[]{
                        detail.getID(), detail.getInvoiceID(), detail.getProductID(),
                        detail.getProductName(), detail.getProductType(), detail.getQuantity(),
                        detail.getPrice(), subtotal
                });
            }
        }
    }
}