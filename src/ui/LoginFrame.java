package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private LibraryService library;

    public LoginFrame(LibraryService library) {

        this.library = library;

        setTitle("Smart Library Management System");
        setSize(520, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 46));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Icon label
        JLabel iconLabel = new JLabel("\u2588\u2588 LIBRARY");
        iconLabel.setFont(new Font("Consolas", Font.BOLD, 28));
        iconLabel.setForeground(new Color(137, 180, 250));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Smart Library System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Manage books, members, and transactions");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(166, 173, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Version info
        JLabel versionLabel = new JLabel("v1.0  |  Java Swing Desktop Application");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(88, 91, 112));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Enter button — styled
        JButton enterButton = new JButton("Enter System  \u25B6");
        enterButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        enterButton.setForeground(Color.WHITE);
        enterButton.setBackground(new Color(137, 180, 250));
        enterButton.setFocusPainted(false);
        enterButton.setBorderPainted(false);
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.setMaximumSize(new Dimension(250, 45));
        enterButton.setPreferredSize(new Dimension(250, 45));

        // Hover effect
        enterButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                enterButton.setBackground(new Color(116, 160, 230));
            }
            public void mouseExited(MouseEvent e) {
                enterButton.setBackground(new Color(137, 180, 250));
            }
        });

        // Button action — opens DashboardFrame
        enterButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(library);
        });

        // Layout
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(enterButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(versionLabel);
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
        setVisible(true);
    }

}
