package org.example.cnjava_milkteastore.frontend.ui;

import org.example.cnjava_milkteastore.frontend.session.Session;
import org.example.cnjava_milkteastore.frontend.theme.RoundedButton;
import org.example.cnjava_milkteastore.frontend.theme.Theme;
import org.example.cnjava_milkteastore.frontend.ui.login.LoginUI;
import org.example.cnjava_milkteastore.frontend.ui.management.MainManagementView;
import org.example.cnjava_milkteastore.frontend.ui.possystem.POSSystemView;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainUI extends JFrame {

    private JLabel lblTime;

    public MainUI() {
        initUI();
        setupComponents();
    }

    private void initUI() {
        setTitle("MilkTea Store - Menu");
        setSize(1000, 700);
        setUndecorated(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 245, 250));
        setLayout(new BorderLayout());
    }

    private void setupComponents() {
        // --- HEADER ---
        lblTime = createTimeLabel();
        JLabel lblTitle = createTitleLabel("Cửa hàng Trà Sữa Phúc Long");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(250, 245, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblTime, BorderLayout.EAST);

        // --- BUTTONS ---
        JButton btnSale = new RoundedButton("Bán hàng", Theme.FONT_TOTAL, Color.WHITE, Theme.DARK_PINK, Theme.DARK_PINK, Color.WHITE);
        JButton btnManage = new RoundedButton("Quản lý", Theme.FONT_TOTAL, Color.WHITE, Theme.DARK_PINK, Theme.DARK_PINK, Color.WHITE);

        btnSale.setPreferredSize(new Dimension(250, 60));
        btnManage.setPreferredSize(new Dimension(250, 60));

        btnSale.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new POSSystemView().setVisible(true));
        });

        btnManage.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new MainManagementView().setVisible(true));
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
        btnPanel.setOpaque(false);
        btnPanel.add(btnSale);
        btnPanel.add(btnManage);

        // --- CENTER WRAPPER ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        centerPanel.add(btnPanel);

        // --- FOOTER: Đăng xuất ---
        JButton btnLogout = new RoundedButton("Đăng xuất", Theme.FONT_TOTAL, Color.WHITE, Color.GRAY, Color.GRAY.darker(), Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(150, 40));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Session session = new Session();
                session.deleteSessionFile();
                dispose(); // đóng MainUI
                SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
            }
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        footerPanel.setOpaque(false);
        footerPanel.add(btnLogout);

        // --- ADD TO FRAME ---
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // --- TIMER ---
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();

        Session session = new Session();
        if (!session.isAdminFromAccountInfo()) {
            btnManage.setEnabled(false);
        }
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_TOTAL.deriveFont(Font.BOLD, 26f));
        label.setForeground(Theme.DARK_PINK);
        return label;
    }

    private JLabel createTimeLabel() {
        JLabel label = new JLabel();
        label.setFont(Theme.FONT_TEXT.deriveFont(Font.PLAIN, 16f));
        label.setForeground(Theme.DARK_PINK.darker());
        return label;
    }

    private void updateTime() {
        Locale locale = new Locale("vi", "VN");
        String pattern = "EEEE, dd MMMM yyyy  |  HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        lblTime.setText(sdf.format(new Date()));
    }

    public static void main(String[] args) {
        Session session = new Session();
        if (!session.isAccountInfoFileEmpty()) {
            SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
        } else {
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
        }
    }
}
