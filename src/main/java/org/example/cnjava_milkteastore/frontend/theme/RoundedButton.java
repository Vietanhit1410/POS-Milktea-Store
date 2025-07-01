package org.example.cnjava_milkteastore.frontend.theme;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    public RoundedButton(String text, Font font, Color foreground, Color background, Color border, Color hover) {
        super(text);
        setFont(font);
        setForeground(foreground);
        setBackground(background);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        setContentAreaFilled(false);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Bo góc
        super.paintComponent(g);
        g2.dispose();
    }
}
