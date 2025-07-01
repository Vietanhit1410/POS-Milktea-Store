package org.example.cnjava_milkteastore.frontend.theme;

import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color DARK_PINK = new Color(233, 30, 99);
    public static final Color BG_PINK = new Color(252, 228, 236);
    public static final Color LIGHTER_PINK = new Color(255, 242, 247); // For invoice items
    public static final Font FONT_TEXT = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_TOTAL = new Font("Arial", Font.BOLD, 18);
    public static final Font FONT_PRODUCT_BUTTON = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_INVOICE_ITEM = new Font("Arial", Font.PLAIN, 13);


    public static void configureButton(JButton button) {
        button.setFont(FONT_TEXT);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE); // Nền trắng cho button
        button.setForeground(DARK_PINK); // Chữ màu hồng đậm
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_PINK, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    public static void configureHeaderButton(JButton button) {
        button.setFont(FONT_TEXT);
        button.setFocusPainted(false);
        button.setBackground(new Color(213, 10, 79)); // Màu nền đậm hơn cho nút header
        button.setForeground(Color.WHITE); // Chữ trắng
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
    public static void configureTextField(JTextField textField) {
        textField.setFont(Theme.FONT_TEXT);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setBorder(BorderFactory.createLineBorder(DARK_PINK));
    }

}