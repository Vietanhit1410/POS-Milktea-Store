package org.example.cnjava_milkteastore.frontend.ui.login;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.backend.account.dto.AccountDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.session.Session;
import org.example.cnjava_milkteastore.frontend.theme.Theme;
import org.example.cnjava_milkteastore.frontend.ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class LoginUI extends JFrame {
    private GetAPI<AccountDTO> accountAPI = new GetAPI<>(new TypeReference<>() {}, AccountDTO.class, new TypeReference<>() {});
    private List<AccountDTO> listAccount;
    private AccountDTO loggedInAccount; // Lưu tài khoản đã đăng nhập
    private final Session currentSession = new Session();

    public LoginUI() {
        accountAPI.fetchFromApi(accountDTO -> {
            this.listAccount = accountDTO;
        });

        setTitle("Đăng nhập - Quán Trà Sữa POV");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full màn hình

        // Load ảnh nền
        ImageIcon bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/background_loginUI.jpg")));
        Image img = bgIcon.getImage();
        Image scaledImg = img.getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(scaledImg);

        // Panel nền có hình ảnh
        JLabel backgroundLabel = new JLabel(bgIcon);
        backgroundLabel.setLayout(new GridBagLayout()); // Canh giữa khung login

        // Panel login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255, 220)); // Trắng trong suốt
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        loginPanel.setMaximumSize(new Dimension(450, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Cửa hàng trà sữa Phúc Long", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Theme.DARK_PINK);
        loginPanel.add(title, gbc);

        // Tài khoản
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Tài khoản:");
        usernameLabel.setFont(Theme.FONT_TEXT);
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 35));
        loginPanel.add(usernameField, gbc);

        // Mật khẩu
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(Theme.FONT_TEXT);
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 35));
        loginPanel.add(passwordField, gbc);

        // Nút đăng nhập
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginBtn = new JButton("Đăng nhập");
        Theme.configureButton(loginBtn);
        loginBtn.setPreferredSize(new Dimension(200, 40));
        loginPanel.add(loginBtn, gbc);

        // Nút đăng xuất
        gbc.gridy++;
        JButton logoutBtn = new JButton("Đăng xuất");
        Theme.configureHeaderButton(logoutBtn);
        logoutBtn.setPreferredSize(new Dimension(200, 40));
        loginPanel.add(logoutBtn, gbc);

        // Nút thoát
        gbc.gridy++;
        JButton exitBtn = new JButton("Thoát");
        Theme.configureHeaderButton(exitBtn);
        exitBtn.setPreferredSize(new Dimension(200, 40));
        loginPanel.add(exitBtn, gbc);

        // Đặt panel login vào background
        backgroundLabel.add(loginPanel);

        setContentPane(backgroundLabel);

        // Hành động nút đăng nhập
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            // Kiểm tra tài khoản và mật khẩu
            if (listAccount == null || listAccount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không thể tải danh sách tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loginSuccess = false;
            loggedInAccount = null;

            for (AccountDTO account : listAccount) {
                if (account.getUserName().equals(user) && account.getPassword().equals(pass)) {
                    loginSuccess = true;
                    loggedInAccount = account;
                    break;
                }
            }

            if (loginSuccess) {
                // Kiểm tra quyền truy cập
                    // Lưu phiên vào file session.txt
                    saveSession(loggedInAccount);
                    // Lưu thông tin tài khoản vào file account_info.txt
                    try (FileWriter writer = new FileWriter("D:\\CNJAVA_MilkTeaStore\\src\\main\\java\\org\\example\\cnjava_milkteastore\\frontend\\session\\session.txt")) {
                        writer.write("Thông tin tài khoản đăng nhập:\n");
                        writer.write("Tài khoản: " + loggedInAccount.getUserName() + "\n");
                        writer.write("Mật khẩu: " + loggedInAccount.getPassword() + "\n");
                        writer.write("Vai trò: " + loggedInAccount.getRole() + "\n");
                        writer.write("Thời gian đăng nhập: " + LocalDateTime.now() + "\n");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
                    dispose();
                    SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
                } else {
                    JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
        });

        // Hành động nút đăng xuất
        logoutBtn.addActionListener(e -> {
            currentSession.deleteSessionFile();
            currentSession.clearAccountInfoFile();
            loggedInAccount = null;
            usernameField.setText("");
            passwordField.setText("");
            JOptionPane.showMessageDialog(this, "Đăng xuất thành công");
        });

        // Hành động nút thoát
        exitBtn.addActionListener(e -> System.exit(0));
    }


    private void saveSession(AccountDTO account) {
        try (FileWriter writer = new FileWriter("D:\\CNJAVA_MilkTeaStore\\src\\main\\java\\org\\example\\cnjava_milkteastore\\frontend\\session\\session.txt")) {
            writer.write(account.getUserName() + "\n");
            writer.write(LocalDateTime.now().toString() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }






}