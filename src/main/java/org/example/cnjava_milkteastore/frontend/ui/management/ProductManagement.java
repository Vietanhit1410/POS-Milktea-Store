package org.example.cnjava_milkteastore.frontend.ui.management;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.theme.Theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductManagement extends JPanel {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnReload; // Added btnReload
    private JPanel formPanel;
    private boolean isEditing = false;
    private int editingRow = -1;
    private List<ProductDTO> allProducts;
    GetAPI<ProductDTO> productAPI = new GetAPI<>(new TypeReference<>() {}, ProductDTO.class, new TypeReference<>() {});

    private JTextField tfID, tfCode, tfStock, tfType, tfPrice, tfPoints, tfName, tfImage;

    public ProductManagement() {
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
        productAPI.fetchFromApi(productDTOS -> {
            this.allProducts = productDTOS;
            loadData();
        });
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Theme.BG_PINK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("📦 Product Management", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.DARK_PINK);

        JLabel tabLabel = new JLabel("🛒 Product Tab", SwingConstants.RIGHT);
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabLabel.setForeground(Color.DARK_GRAY);

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(tabLabel, BorderLayout.EAST);
        return titlePanel;
    }

    private JPanel createFormPanel() {
        formPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        formPanel.setBackground(Theme.LIGHTER_PINK);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK), "Product Information",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));
        formPanel.setVisible(false);

        tfID = createField("ID");
        tfCode = createField("Code");
        tfStock = createField("Stock");
        tfType = createField("Type");
        tfPrice = createField("Price");
        tfPoints = createField("Points");
        tfName = createField("Name");
        tfImage = createField("Image");

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        Theme.configureButton(btnSave);
        Theme.configureButton(btnCancel);

        btnSave.addActionListener(e -> saveProduct());
        btnCancel.addActionListener(e -> formPanel.setVisible(false));

        formPanel.add(tfID);
        formPanel.add(tfCode);
        formPanel.add(tfStock);
        formPanel.add(tfType);
        formPanel.add(tfPrice);
        formPanel.add(tfPoints);
        formPanel.add(tfName);
        formPanel.add(tfImage);
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
        String[] columns = {"ID", "Code", "Stock", "Type", "Price", "Points", "Name", "Image"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setFont(Theme.FONT_TEXT);
        productTable.setRowHeight(25);
        productTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        productTable.setBackground(Theme.LIGHTER_PINK);
        productTable.setForeground(Color.BLACK);
        productTable.setGridColor(Theme.DARK_PINK);
        productTable.setSelectionBackground(Theme.DARK_PINK.brighter());
        productTable.setSelectionForeground(Color.WHITE);
        productTable.setShowGrid(true);
        productTable.setIntercellSpacing(new Dimension(1, 1));

        // Adjust column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        productTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Code
        productTable.getColumnModel().getColumn(2).setPreferredWidth(60); // Stock
        productTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Type
        productTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Price
        productTable.getColumnModel().getColumn(5).setPreferredWidth(60); // Points
        productTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Name
        productTable.getColumnModel().getColumn(7).setPreferredWidth(200); // Image

        // Center-align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        productTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Code
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Stock
        productTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Type
        productTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Points

        // Right-align numerical columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        productTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Price

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBackground(Theme.BG_PINK);
        scrollPane.getViewport().setBackground(Theme.LIGHTER_PINK);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), "Product List",
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
        btnReload = new JButton("🔃 Reload"); // Added Reload button

        JButton[] buttons = {btnAdd, btnEdit, btnDelete, btnReload}; // Include btnReload
        for (JButton btn : buttons) {
            Theme.configureButton(btn);
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        }

        btnAdd.addActionListener(e -> showForm(false));
        btnEdit.addActionListener(e -> showForm(true));
        btnDelete.addActionListener(e -> deleteSelected());
        btnReload.addActionListener(e -> reloadData()); // Action listener for Reload

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnReload); // Add button to panel

        return panel;
    }

    private void showForm(boolean editing) {
        isEditing = editing;
        editingRow = productTable.getSelectedRow();

        if (editing) {
            if (editingRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a row to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tfID.setText(tableModel.getValueAt(editingRow, 0).toString());
            tfCode.setText(tableModel.getValueAt(editingRow, 1).toString());
            tfStock.setText(tableModel.getValueAt(editingRow, 2).toString());
            tfType.setText(tableModel.getValueAt(editingRow, 3).toString());
            tfPrice.setText(tableModel.getValueAt(editingRow, 4).toString());
            tfPoints.setText(tableModel.getValueAt(editingRow, 5).toString());
            tfName.setText(tableModel.getValueAt(editingRow, 6).toString());
            tfImage.setText(tableModel.getValueAt(editingRow, 7).toString());

            tfID.setEditable(false);
            tfCode.setEditable(false);
        } else {
            for (JTextField tf : new JTextField[]{tfID, tfCode, tfStock, tfType, tfPrice, tfPoints, tfName, tfImage}) {
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

    private void saveProduct() {
        try {
            // Validate and parse input fields
            String idText = tfID.getText().trim();
            String code = tfCode.getText().trim();
            String stockText = tfStock.getText().trim();
            String type = tfType.getText().trim();
            String priceText = tfPrice.getText().trim();
            String pointsText = tfPoints.getText().trim();
            String name = tfName.getText().trim();
            String image = tfImage.getText().trim();

            // Validation
            if (idText.isEmpty() || code.isEmpty() || stockText.isEmpty() || type.isEmpty() ||
                    priceText.isEmpty() || pointsText.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields except Image are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id;
            int stock;
            float price;
            int points;

            try {
                id = Integer.parseInt(idText);
                if (id < 0) throw new NumberFormatException("ID must be positive.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                stock = Integer.parseInt(stockText);
                if (stock < 0) throw new NumberFormatException("Stock cannot be negative.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Stock format. Please enter a non-negative integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                price = Float.parseFloat(priceText);
                if (price < 0) throw new NumberFormatException("Price cannot be negative.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Price format. Please enter a non-negative number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                points = Integer.parseInt(pointsText);
                if (points < 0) throw new NumberFormatException("Points cannot be negative.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Points format. Please enter a non-negative integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create ProductDTO
            ProductDTO product = new ProductDTO(id, code, stock, type, price, points, name, image.isEmpty() ? null : image);

            // Update table model
            Object[] rowData = {
                    product.getID(),
                    product.getProductCode(),
                    product.getStock(),
                    product.getProductType(),
                    product.getPrice(),
                    product.getPoints(),
                    product.getProductName(),
                    product.getProductImage()
            };

            // Optionally save to API (assuming GetAPI has a save method)
             productAPI.postToApi(product, success -> {
                     if (isEditing && editingRow >= 0) {
                         for (int i = 0; i < rowData.length; i++) {
                             tableModel.setValueAt(rowData[i], editingRow, i);
                         }
                     } else {
                         tableModel.addRow(rowData);
                     }
                     reloadData();
             });
            // Refresh the table data from API
            formPanel.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this product?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int productId = Integer.parseInt(productTable.getValueAt(row, 0).toString());
                productAPI.deleteByIdFromApi(productId);
                JOptionPane.showMessageDialog(this, "Delete Product Successful ", "Error", JOptionPane.ERROR_MESSAGE);
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
        productAPI.fetchFromApi(productDTOS -> {
            this.allProducts = productDTOS;
            loadData();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing rows
        for (ProductDTO product : allProducts) {
            tableModel.addRow(new Object[]{
                    product.getID(),
                    product.getProductCode(),
                    product.getStock(),
                    product.getProductType(),
                    product.getPrice(),
                    product.getPoints(),
                    product.getProductName(),
                    product.getProductImage()
            });
        }
    }
}