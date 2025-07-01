package org.example.cnjava_milkteastore.frontend.ui.possystem;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.customer.dto.CustomerDTO; // ADDED: Import Customer DTO
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.example.cnjava_milkteastore.backend.invoicedetail.dto.InvoiceDetailDTO;
import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.cnjava_milkteastore.frontend.theme.*;


public class POSSystemView extends JFrame {

    // ... (Các thuộc tính không thay đổi)
    private JButton backButton;
    private JButton reloadButton;

    private JTextField searchField;
    private JButton searchButton;
    private JList<String> categoryList;
    private JPanel productDisplayPanel;
    private JPanel invoicePanel;
    private JLabel totalLabel;
    private JButton payCashButton;
    private JButton payQRButton;

    // ADDED: Customer related components
    private JTextField customerPhoneField;
    private JButton searchCustomerButton;
    private JLabel customerInfoLabel;
    private JSpinner redeemPointsSpinner;
    private JButton applyPointsButton;
    private CustomerDTO currentCustomer = null;
    private double pointsDiscount = 0.0;


    private double currentTotal = 0.0;
    private List<InvoiceItem> currentInvoiceItems = new ArrayList<>();
    private InvoiceDTO currentInvoice = new InvoiceDTO();
    private JPanel invoiceItemContainerPanel;

    GetAPI<ProductDTO> apiProductData = new GetAPI<>(new TypeReference<>() {},ProductDTO.class, new TypeReference<>() {
    });
    GetAPI<InvoiceDetailDTO> apiInvoiceDetail = new GetAPI<>(new TypeReference<>() {},InvoiceDetailDTO.class,new TypeReference<>() {});
    GetAPI<InvoiceDTO> apiInvoice = new GetAPI<>(new TypeReference<>() {},InvoiceDTO.class,new TypeReference<>() {});
    // ADDED: API for Customer
    GetAPI<CustomerDTO> apiCustomer = new GetAPI<>(new TypeReference<>() {}, CustomerDTO.class, new TypeReference<>() {});


    private List<ProductDTO> masterProductList = new ArrayList<>();


    public POSSystemView() {
        setTitle("Milk Tea Store POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                toFront();
                requestFocus();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        initComponents();
        layoutComponents();
        addEventListeners();
        addLoadingSpinner(this);
        apiProductData.fetchFromApi(this::loadSampleMasterData);
    }

    private void initComponents() {
        // Header
        backButton = new JButton("Back");
        reloadButton = new JButton("Reload");
        Theme.configureHeaderButton(backButton);
        Theme.configureHeaderButton(reloadButton);

        // Left Panel - Products
        searchField = new JTextField(20);
        searchField.setFont(Theme.FONT_TEXT);
        searchButton = new JButton("Search");
        Theme.configureButton(searchButton);


        DefaultListModel<String> categoryModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryModel);
        categoryList.setFont(Theme.FONT_TEXT);
        categoryList.setBackground(Theme.LIGHTER_PINK);
        categoryList.setSelectionBackground(Theme.DARK_PINK);
        categoryList.setSelectionForeground(Color.WHITE);
        categoryList.setFixedCellHeight(30);
        categoryList.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));


        productDisplayPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        productDisplayPanel.setBackground(Color.WHITE);


        // Right Panel - Invoice
        invoiceItemContainerPanel = new JPanel();
        invoiceItemContainerPanel.setLayout(new BoxLayout(invoiceItemContainerPanel, BoxLayout.Y_AXIS));
        invoiceItemContainerPanel.setBackground(Theme.LIGHTER_PINK);

        // MODIFIED: Initial total text
        totalLabel = new JLabel("Tổng: 0 VND");
        totalLabel.setFont(Theme.FONT_TOTAL);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setBorder(new EmptyBorder(10,0,10,10));


        payCashButton = new JButton("Cash");
        payQRButton = new JButton("Scan QR");
        Theme.configureButton(payCashButton);
        Theme.configureButton(payQRButton);
        payCashButton.setBackground(new Color(76, 175, 80));
        payCashButton.setForeground(Color.WHITE);
        payQRButton.setBackground(new Color(33, 150, 243));
        payQRButton.setForeground(Color.WHITE);

        // ADDED: Initialize customer components
        customerPhoneField = new JTextField(12);
        customerPhoneField.setFont(Theme.FONT_TEXT);
        searchCustomerButton = new JButton("Tìm");
        Theme.configureButton(searchCustomerButton);

        customerInfoLabel = new JLabel("Chưa có thông tin khách hàng.");
        customerInfoLabel.setFont(Theme.FONT_TEXT);

        SpinnerNumberModel pointsModel = new SpinnerNumberModel(0, 0, 0, 1); // value, min, max, step
        redeemPointsSpinner = new JSpinner(pointsModel);
        redeemPointsSpinner.setEnabled(false);

        applyPointsButton = new JButton("Đổi điểm");
        Theme.configureButton(applyPointsButton);
        applyPointsButton.setEnabled(false);
    }

    private void layoutComponents() {
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        headerPanel.setBackground(Theme.DARK_PINK);
        headerPanel.setBorder(new EmptyBorder(5,5,5,5));
        headerPanel.add(backButton);
        headerPanel.add(reloadButton);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content Panel ---
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.70);
        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(8);


        // --- Left Panel (Products Area) ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        leftPanel.setBackground(Theme.BG_PINK);

        // Search sub-panel
        JPanel searchPanel = new JPanel(new BorderLayout(5,0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search Product:");
        searchLabel.setFont(Theme.FONT_TEXT);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.setBorder(new EmptyBorder(0,0,10,0));
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Categories and Products sub-panel
        JSplitPane leftSubSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftSubSplitPane.setResizeWeight(0.001);
        leftSubSplitPane.setDividerSize(5);
        leftSubSplitPane.setBorder(null);

        JScrollPane categoryScrollPane = new JScrollPane(categoryList);
        categoryScrollPane.setBorder(BorderFactory.createLineBorder(Theme.DARK_PINK,1));
        leftSubSplitPane.setLeftComponent(categoryScrollPane);

        JScrollPane productScrollPane = new JScrollPane(productDisplayPanel);
        productScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        productScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftSubSplitPane.setRightComponent(productScrollPane);

        leftPanel.add(leftSubSplitPane, BorderLayout.CENTER);
        mainSplitPane.setLeftComponent(leftPanel);


        // --- Right Panel (Invoice Area) ---
        invoicePanel = new JPanel(new BorderLayout(10,10));
        invoicePanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        invoicePanel.setBackground(Theme.BG_PINK);

        // MODIFIED: Create a top panel for title and customer info
        JPanel invoiceTopPanel = new JPanel(new BorderLayout(10, 10));
        invoiceTopPanel.setOpaque(false);

        JLabel invoiceTitle = new JLabel("ORDER");
        invoiceTitle.setFont(Theme.FONT_TOTAL.deriveFont(Font.BOLD, 16f));
        invoiceTitle.setForeground(Theme.DARK_PINK);
        invoiceTitle.setHorizontalAlignment(SwingConstants.CENTER);
        invoiceTopPanel.add(invoiceTitle, BorderLayout.NORTH);

        // ADDED: Customer Panel
        JPanel customerOuterPanel = new JPanel(new BorderLayout());
        customerOuterPanel.setOpaque(false);
        customerOuterPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        JPanel customerSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        customerSearchPanel.setOpaque(false);
        customerSearchPanel.add(new JLabel("SĐT:"));
        customerSearchPanel.add(customerPhoneField);
        customerSearchPanel.add(searchCustomerButton);

        JPanel customerDetailsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        customerDetailsPanel.setOpaque(false);
        customerDetailsPanel.setBorder(new EmptyBorder(5,5,5,5));
        customerDetailsPanel.add(customerInfoLabel);

        JPanel redeemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        redeemPanel.setOpaque(false);
        redeemPanel.add(new JLabel("Điểm đổi:"));
        redeemPanel.add(redeemPointsSpinner);
        redeemPanel.add(applyPointsButton);
        customerDetailsPanel.add(redeemPanel);

        customerOuterPanel.add(customerSearchPanel, BorderLayout.NORTH);
        customerOuterPanel.add(customerDetailsPanel, BorderLayout.CENTER);

        invoiceTopPanel.add(customerOuterPanel, BorderLayout.CENTER);
        invoicePanel.add(invoiceTopPanel, BorderLayout.NORTH);


        JScrollPane invoiceScrollPane = new JScrollPane(invoiceItemContainerPanel);
        invoiceScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        invoiceScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        invoicePanel.add(invoiceScrollPane, BorderLayout.CENTER);

        // Panel for total and payment buttons
        JPanel bottomRightPanel = new JPanel(new BorderLayout(5,10));
        bottomRightPanel.setOpaque(false);
        bottomRightPanel.add(totalLabel, BorderLayout.NORTH);

        JPanel paymentButtonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        paymentButtonsPanel.setOpaque(false);
        paymentButtonsPanel.add(payCashButton);
        paymentButtonsPanel.add(payQRButton);
        bottomRightPanel.add(paymentButtonsPanel, BorderLayout.CENTER);
        bottomRightPanel.setBorder(new EmptyBorder(10,0,0,0));

        invoicePanel.add(bottomRightPanel, BorderLayout.SOUTH);
        mainSplitPane.setRightComponent(invoicePanel);

        add(mainSplitPane, BorderLayout.CENTER);
    }

    private void addEventListeners() {
        backButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(backButton);
            if (window != null) {
                window.dispose();
            }
        });

        reloadButton.addActionListener(e -> {
            addLoadingSpinner(this);
            apiProductData.fetchFromApi(this::loadSampleMasterData);
        });

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            filterProductsByName(searchText);
        });
        searchField.addActionListener(e -> searchButton.doClick());


        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    filterProductsByCategory(selectedCategory);
                }
            }
        });

        payCashButton.addActionListener(e -> processPayment("Cash"));
        payQRButton.addActionListener(e -> processPayment("QR Code"));

        // ADDED: Event listeners for customer functionality
        searchCustomerButton.addActionListener(e -> handleSearchCustomer());
        applyPointsButton.addActionListener(e -> handleApplyPoints());
    }

    public void addLoadingSpinner(JFrame frame) {
        JPanel spinnerPanel = new JPanel() {
            double angle = 0;
            Timer timer;

            {
                // Khởi tạo spinner quay
                timer = new Timer(16, e -> {
                    angle += (2 * Math.PI / 60); // quay đủ 1 vòng trong 1 giây
                    repaint();
                });
                timer.start();

                // Sau 1 giây thì dừng và gỡ bỏ spinner
                new Timer(1000, e -> {
                    timer.stop();                     // Dừng quay
                    frame.getLayeredPane().remove(this); // Gỡ spinner ra
                    frame.repaint();                 // Vẽ lại frame
                }).start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth(), h = getHeight();
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int r = 20;
                int x = w / 2;
                int y = h / 2;

                g2d.setStroke(new BasicStroke(4));
                g2d.setColor(Color.PINK);
                g2d.drawArc(x - r, y - r, r * 2, r * 2, (int) Math.toDegrees(angle), 270);
            }
        };

        spinnerPanel.setOpaque(false);
        spinnerPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        spinnerPanel.setLayout(null);

        frame.getLayeredPane().add(spinnerPanel, JLayeredPane.POPUP_LAYER);
        frame.repaint();
    }


    private void loadSampleMasterData(List<ProductDTO> products) {
        masterProductList.clear();
        masterProductList.addAll(products);

        // Update category list
        DefaultListModel<String> categoryModel = (DefaultListModel<String>) categoryList.getModel();
        categoryModel.clear();
        categoryModel.addElement("All Items");
        masterProductList.stream()
                .map(ProductDTO::getProductType)
                .distinct()
                .sorted()
                .forEach(categoryModel::addElement);

        categoryList.setSelectedIndex(0);
        filterProductsByCategory("All Items");

        // Clear current invoice and customer info
        currentInvoiceItems.clear();
        resetCustomerState(); // MODIFIED
        refreshInvoiceDisplay();
        updateTotalDisplay();
    }

    // ADDED: Method to handle searching for a customer
    private void handleSearchCustomer() {
        String phoneNumber = customerPhoneField.getText().trim();
        if (phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Assuming API endpoint like /customers/phone/{phoneNumber}
        apiCustomer.fetchFromApi(customers -> {
            CustomerDTO customer = null;
            for(CustomerDTO customerDTO : customers){
                if(customerDTO.getCustomerPhone().equals(phoneNumber)){
                    customer = customerDTO;
                    break;
                }
            }
            if (customer != null) {
                currentCustomer = customer;
                customerInfoLabel.setText(String.format("Tên: %s - Điểm: %d", customer.getCustomerName(), customer.getPoints()));
                redeemPointsSpinner.setEnabled(true);
                ((SpinnerNumberModel) redeemPointsSpinner.getModel()).setMaximum(customer.getPoints());
                applyPointsButton.setEnabled(true);
            } else {
                // Not found, ask to create new customer
                int response = JOptionPane.showConfirmDialog(
                        this,
                        "Không tìm thấy khách hàng. Bạn có muốn tạo mới?",
                        "Tạo khách hàng mới",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    showCreateCustomerDialog(phoneNumber);
                }
            }
        });
    }

    // ADDED: Method to show the create new customer dialog
    private void showCreateCustomerDialog(String phoneNumber) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        JTextField nameField = new JTextField(15);
        JTextField phoneField = new JTextField(phoneNumber);
        phoneField.setEditable(false); // Phone number is pre-filled

        panel.add(new JLabel("Tên khách hàng:"));
        panel.add(nameField);
        panel.add(new JLabel("Số điện thoại:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tạo khách hàng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            CustomerDTO newCustomer = new CustomerDTO(0,"", name, phoneNumber, 5); // ID and points are set by backend
            apiCustomer.postToApi(newCustomer, createdCustomer -> {
                if (createdCustomer != null) {
                    JOptionPane.showMessageDialog(this, "Tạo khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    handleSearchCustomer();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi tạo khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }


    // ADDED: Method to handle applying points
    private void handleApplyPoints() {
        if (currentCustomer == null) return;

        int pointsToRedeem = (Integer) redeemPointsSpinner.getValue();
        if (pointsToRedeem > currentCustomer.getPoints()) {
            JOptionPane.showMessageDialog(this, "Số điểm đổi không được lớn hơn điểm hiện có.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pointsDiscount = pointsToRedeem * 1000; // 1 point = 1000 VND
        updateTotalDisplay();
        JOptionPane.showMessageDialog(this, String.format("Đã áp dụng giảm giá %.0f VND.", pointsDiscount), "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // ADDED: Reset customer information
    private void resetCustomerState() {
        currentCustomer = null;
        pointsDiscount = 0.0;
        customerPhoneField.setText("");
        customerInfoLabel.setText("Chưa có thông tin khách hàng.");
        redeemPointsSpinner.setValue(0);
        redeemPointsSpinner.setEnabled(false);
        applyPointsButton.setEnabled(false);
        ((SpinnerNumberModel) redeemPointsSpinner.getModel()).setMaximum(0);
    }


    private void filterProductsByCategory(String category) {
        // ... (no changes in this method)
        productDisplayPanel.removeAll();
        List<ProductDTO> productsToShow;
        if ("All Items".equalsIgnoreCase(category)) {
            productsToShow = masterProductList;
        } else {
            productsToShow = masterProductList.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        displayProducts(productsToShow);
    }

    private void filterProductsByName(String searchText) {
        // ... (no changes in this method)
        productDisplayPanel.removeAll();
        if (searchText.isEmpty()) {
            filterProductsByCategory(categoryList.getSelectedValue() != null ? categoryList.getSelectedValue() : "All Items");
            return;
        }
        List<ProductDTO> productsToShow = masterProductList.stream()
                .filter(p -> p.getProductName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        displayProducts(productsToShow);
    }

    private void displayProducts(List<ProductDTO> products) {
        // ... (no changes in this method)
        productDisplayPanel.removeAll();
        for (ProductDTO product : products) {
            JButton productButton = new JButton();
            productButton.setLayout(new BorderLayout(5, 5)); // Layout chính cho nút
            productButton.setPreferredSize(new Dimension(150, 180)); // Tăng kích thước để chứa ảnh
            productButton.setBackground(Color.WHITE);
            productButton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            productButton.setFocusPainted(false);

            productButton.putClientProperty("productID", product.getID());

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            try {
                URL imageUrl = getClass().getResource("/images/" + product.getProductImage());
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image image = icon.getImage().getScaledInstance(130, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                } else {
                    imageLabel.setText("No Image");
                    System.err.println("Không tìm thấy ảnh: " + product.getProductImage());
                }
            } catch (Exception e) {
                imageLabel.setText("Image Error");
                e.printStackTrace();
            }
            productButton.add(imageLabel, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setOpaque(false);
            infoPanel.setBorder(new EmptyBorder(0,5,5,5));

            JLabel nameLabel = new JLabel(product.getProductName(), SwingConstants.CENTER);
            nameLabel.setFont(Theme.FONT_PRODUCT_BUTTON.deriveFont(Font.BOLD, 12f));

            String priceAndStockText = String.format("%.0f VND | Stock: %d", product.getPrice(), product.getStock());
            JLabel priceStockLabel = new JLabel(priceAndStockText, SwingConstants.CENTER);
            priceStockLabel.setFont(Theme.FONT_PRODUCT_BUTTON.deriveFont(Font.PLAIN, 11f));

            infoPanel.add(nameLabel);
            infoPanel.add(priceStockLabel);

            productButton.add(infoPanel, BorderLayout.SOUTH);

            productButton.setToolTipText("Click to add " + product.getProductName() + " to cart");
            if (product.getStock() <= 0) {
                productButton.setEnabled(false);
                nameLabel.setForeground(Color.GRAY);
                priceStockLabel.setText("Out of Stock");
                priceStockLabel.setForeground(Color.RED);
            }

            productButton.addActionListener(e -> {
                if (product.getStock() > 0) {
                    promptForQuantityAndAddToCart(product);
                } else {
                    JOptionPane.showMessageDialog(this, product.getProductName() + " is out of stock!", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                }
            });

            productDisplayPanel.add(productButton);
        }
        productDisplayPanel.revalidate();
        productDisplayPanel.repaint();
    }


    private void promptForQuantityAndAddToCart(ProductDTO product) {
        // ... (no changes in this method)
        JPanel panel = new JPanel(new GridLayout(0,1,5,5));
        panel.add(new JLabel("Enter quantity for " + product.getProductName() + ":"));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, product.getStock(), 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        panel.add(quantitySpinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Select Quantity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int quantity = (Integer) quantitySpinner.getValue();
                if (quantity > 0 && quantity <= product.getStock()) {
                    addItemToInvoice(product, quantity);
                } else if (quantity > product.getStock()) {
                    JOptionPane.showMessageDialog(this, "Not enough stock. Available: " + product.getStock(), "Stock Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity input.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void addItemToInvoice(ProductDTO product, int quantity) {
        // ... (no changes in this method)
        for (InvoiceItem item : currentInvoiceItems) {
            if (item.getProductID() == product.getID()) {
                if (item.getQuantity() + quantity <= product.getStock() + item.getQuantity()) {
                    item.quantity += quantity;
                    product.setStock(product.getStock() - quantity);
                    refreshInvoiceDisplay();
                    updateTotalDisplay();
                    updateProductButtonDisplay(product.getID());
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock to add more " + product.getProductName(), "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        InvoiceItem newItem = new InvoiceItem(product, quantity);
        currentInvoiceItems.add(newItem);
        product.setStock(product.getStock() - quantity);

        refreshInvoiceDisplay();
        updateTotalDisplay();
        updateProductButtonDisplay(product.getID());
    }

    private void refreshInvoiceDisplay() {
        // ... (no changes in this method)
        invoiceItemContainerPanel.removeAll();
        for (InvoiceItem item : currentInvoiceItems) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
            itemPanel.setBackground(Theme.LIGHTER_PINK);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0,0,1,0, Color.decode("#e0e0e0")),
                    new EmptyBorder(8, 8, 8, 8)
            ));


            String itemText = String.format("<html><b>%s</b><br>SL: %d - %.0f VND<br>Gía: %.0f VND</html>",
                    item.getName(), item.getQuantity(), item.getUnitPrice(), item.getSubtotal());
            JLabel itemLabel = new JLabel(itemText);
            itemLabel.setFont(Theme.FONT_INVOICE_ITEM);
            itemPanel.add(itemLabel, BorderLayout.CENTER);

            JButton removeItemButton = new JButton("X");
            Theme.configureButton(removeItemButton);
            removeItemButton.setMargin(new Insets(2,8,2,8));
            removeItemButton.setBackground(new Color(255, 102, 102));
            removeItemButton.setForeground(Color.WHITE);


            removeItemButton.addActionListener(e -> {
                ProductDTO productInItem = item.getProduct();
                productInItem.setStock(productInItem.getStock() + item.getQuantity());
                currentInvoiceItems.remove(item);
                refreshInvoiceDisplay();
                updateTotalDisplay();
                updateProductButtonDisplay(productInItem.getID());
            });
            itemPanel.add(removeItemButton, BorderLayout.EAST);
            invoiceItemContainerPanel.add(itemPanel);
        }
        invoiceItemContainerPanel.revalidate();
        invoiceItemContainerPanel.repaint();
    }

    // MODIFIED: Update total display to include discount
    private void updateTotalDisplay() {
        currentTotal = 0.0;
        for (InvoiceItem item : currentInvoiceItems) {
            currentTotal += item.getSubtotal();
        }

        double finalTotal = currentTotal - pointsDiscount;
        if (finalTotal < 0) {
            finalTotal = 0; // Total can't be negative
        }

        String totalText = String.format(
                "<html><div style='text-align: right;'>"
                        + "Tạm tính: %.0f VND<br>"
                        + "Giảm giá: -%.0f VND<br>"
                        + "<b style='font-size:14px;'>Tổng cộng: %.0f VND</b>"
                        + "</div></html>",
                currentTotal, pointsDiscount, finalTotal
        );
        totalLabel.setText(totalText);
    }


    private void updateProductButtonDisplay(int productID) {
        // ... (no changes in this method)
        ProductDTO productToUpdate = null;
        for (ProductDTO p : masterProductList) {
            if (p.getID() == productID) {
                productToUpdate = p;
                break;
            }
        }
        if (productToUpdate == null) return;

        for (Component comp : productDisplayPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                Integer id = (Integer) button.getClientProperty("productID");

                if (id != null && id == productToUpdate.getID()) {
                    JPanel infoPanel = null;
                    Component southComponent = ((BorderLayout) button.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
                    if (southComponent instanceof JPanel) {
                        infoPanel = (JPanel) southComponent;
                    }

                    if (infoPanel != null && infoPanel.getComponentCount() == 2) {
                        JLabel nameLabel = (JLabel) infoPanel.getComponent(0);
                        JLabel priceStockLabel = (JLabel) infoPanel.getComponent(1);

                        priceStockLabel.setText(String.format("%.0f VND | Kho: %d", productToUpdate.getPrice(), productToUpdate.getStock()));
                        if (productToUpdate.getStock() <= 0) {
                            button.setEnabled(false);
                            nameLabel.setForeground(Color.GRAY);
                            priceStockLabel.setText("Hết hàng");
                            priceStockLabel.setForeground(Color.RED);
                        } else {
                            button.setEnabled(true);
                            nameLabel.setForeground(Color.BLACK);
                            priceStockLabel.setForeground(Color.BLACK);
                        }
                    }
                    break;
                }
            }
        }
    }


    private void processPayment(String paymentMethod) {
        if (currentInvoiceItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống.", "Giỏ hàng trống", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double finalTotal = Math.max(0, currentTotal - pointsDiscount);

        // 2. Create InvoiceDTO
        InvoiceDTO newInvoice = new InvoiceDTO();
        if (currentCustomer != null) {
            newInvoice.setCustomerID(currentCustomer.getID());
        } else {
            newInvoice.setCustomerID(0); // Default/guest customer ID
        }
        newInvoice.setEmployeeID(101); // Sample employee ID
        newInvoice.setDateCreated(LocalDateTime.now());

        float totalPrice = 0;
        int totalQtyDrink = 0;
        int totalQtyFood = 0;
        float priceDrink = 0;
        float priceFood = 0;

        for (InvoiceItem item : currentInvoiceItems) {
            totalPrice += item.getSubtotal();
            if ("Trà Sữa".equalsIgnoreCase(item.getProductType()) || "Trà Trái Cây".equalsIgnoreCase(item.getProductType()) ||
                    "Nước Ép".equalsIgnoreCase(item.getProductType()) ) {
                totalQtyDrink += item.getQuantity();
                priceDrink += item.getSubtotal();
            } else if ("Combo".equalsIgnoreCase(item.getProductType())) {
                totalQtyFood += item.getQuantity();
                priceFood += (item.getSubtotal()/2);
                totalQtyDrink += item.getQuantity();
                priceDrink += (item.getSubtotal()/2);
            }else {
                totalQtyFood += item.getQuantity();
                priceFood += item.getSubtotal();
            }
        }
        newInvoice.setTotalPrice((float) finalTotal); // Use the final total after discount
        newInvoice.setTotalQuantityDrink(totalQtyDrink);
        newInvoice.setTotalQuantityFood(totalQtyFood);
        newInvoice.setTotalPriceDrink(priceDrink);
        newInvoice.setTotalPriceFood(priceFood);
        List<InvoiceDetailDTO> listInvoiceDetailDTO = new ArrayList<>();

        apiInvoice.postToApi(newInvoice, invoice -> {
            for (InvoiceItem item : currentInvoiceItems) {
                InvoiceDetailDTO dto = new InvoiceDetailDTO();
                dto.setInvoiceID(invoice.getID());
                dto.setProductName(item.getName());
                dto.setProductID(item.getProductID());
                dto.setPrice(item.getSubtotal());
                dto.setQuantity(item.getQuantity());
                dto.setProductType(item.getProductType());
                listInvoiceDetailDTO.add(dto);
            }
            apiInvoiceDetail.postAllToApi(listInvoiceDetailDTO, list -> System.out.println("Lưu chi tiết hóa đơn thành công"));

            // ADDED: Update customer points after successful payment
            if (currentCustomer != null) {
                int pointsRedeemed = (int) (pointsDiscount / 1000);
                int pointsEarned = (int) (finalTotal / 10000);
                currentCustomer.setPoints(currentCustomer.getPoints() - pointsRedeemed + pointsEarned);

                apiCustomer.postToApi(currentCustomer, updatedCustomer -> {
                    System.out.println("Cập nhật điểm khách hàng thành công.");
                });
            }
        });

        // 3. Post-payment processing
        JOptionPane.showMessageDialog(this,
                "Thanh toán " + String.format("%.0f", finalTotal) + " VND bằng " + paymentMethod + " thành công!\n" +
                        "Mã hóa đơn: " + newInvoice.getID(),
                "Thanh toán thành công",
                JOptionPane.INFORMATION_MESSAGE);

        currentInvoiceItems.clear();
        resetCustomerState(); // MODIFIED
        refreshInvoiceDisplay();
        updateTotalDisplay();
        String currentCategory = categoryList.getSelectedValue();
        filterProductsByCategory(currentCategory != null ? currentCategory : "All Items");
    }
}