package org.example.cnjava_milkteastore.frontend.ui.management;

import org.example.cnjava_milkteastore.frontend.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainManagementView extends JFrame {

    private JPanel contentPanel;

    public MainManagementView() {
        setTitle("Management Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // full màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // 1. Ẩn thanh tiêu đề (để không resize, đóng, di chuyển)
        setUndecorated(false);

        // 2. Phóng to tối đa thay vì dùng setFullScreenWindow
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // 3. Không cho phép resize
//        setResizable(false);

        setAlwaysOnTop(true);

        // 6. Nếu bị mất focus, tự mang cửa sổ về lại
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                // Trở lại trên cùng
                toFront();
                requestFocus();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());

        // --- Menu bên trái ---
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Theme.DARK_PINK);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(300, getHeight()));

        JLabel lblMenu = new JLabel("📢 MENU", SwingConstants.CENTER);
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMenu.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        menuPanel.add(lblMenu);

        String[] menuItems = {
                "Employee Management", "Customer Management", "Product Management",
                "Invoice Management", "Report Management","Account Management"
        };

        for (String item : menuItems) {
            JButton button = new JButton(item);
            Theme.configureButton(button);
            button.setFont(new Font("SansSerif", Font.BOLD, 18));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(250, 50));
            button.addActionListener(e -> showPanel(item));
            menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            menuPanel.add(button);
        }

        // Nút exit
        JButton btnLogout = new JButton("📕 Exit");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(211, 47, 47));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(250, 50));
        btnLogout.addActionListener(e -> dispose());

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(btnLogout);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        add(menuPanel, BorderLayout.WEST);

        // --- Content bên phải ---
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Theme.BG_PINK);
        contentPanel.add(createWelcomePanel(), "welcome");
        // Thêm vào dòng sau sau khi tạo contentPanel
        contentPanel.add(new InvoiceManagement(), "Invoice Management");
        contentPanel.add(new ProductManagement(), "Product Management");
        contentPanel.add(new EmployeeManagement(), "Employee Management");
        contentPanel.add(new AccountManagement(), "Account Management");
        contentPanel.add(new CustomerManagement(), "Customer Management");
        contentPanel.add(new ReportManagement(), "Report Management");


        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_PINK);

        JLabel label = new JLabel("Chào mừng bạn đến trang quản lý", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 32));
        label.setForeground(Theme.DARK_PINK);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDummyPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.LIGHTER_PINK);

        JLabel label = new JLabel("📄 This is " + name, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 28));
        label.setForeground(Theme.DARK_PINK);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainManagementView().setVisible(true);
        });
    }
}
