package ui;

import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class ReturnBookDialog extends JDialog {

    private JTextField bookIdField;
    private JTextField userIdField;

    private LibraryService library;

    // Colors
    private static final Color BG_DARK = new Color(30, 30, 46);
    private static final Color BG_SURFACE = new Color(36, 39, 58);
    private static final Color TEXT_PRIMARY = new Color(205, 214, 244);
    private static final Color TEXT_SECONDARY = new Color(166, 173, 200);
    private static final Color ACCENT_MAUVE = new Color(203, 166, 247);

    public ReturnBookDialog(JFrame parent, LibraryService library) {

        super(parent, "Return Book", true);
        this.library = library;

        setSize(400, 220);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        buildForm();

        setVisible(true);
    }

    private void buildForm() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Book ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel bookLabel = new JLabel("Book ID:");
        bookLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookLabel.setForeground(TEXT_SECONDARY);
        add(bookLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        bookIdField = createStyledField();
        add(bookIdField, gbc);

        // User ID
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel userLabel = new JLabel("User ID:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(TEXT_SECONDARY);
        add(userLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        userIdField = createStyledField();
        add(userIdField, gbc);

        // Return button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 12, 8, 12);

        JButton returnBtn = new JButton("Return Book");
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnBtn.setForeground(new Color(30, 30, 46));
        returnBtn.setBackground(ACCENT_MAUVE);
        returnBtn.setFocusPainted(false);
        returnBtn.setBorderPainted(false);
        returnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnBtn.setPreferredSize(new Dimension(0, 38));
        returnBtn.addActionListener(e -> returnBook());
        add(returnBtn, gbc);
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField(18);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(BG_SURFACE);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(69, 71, 90)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private void returnBook() {
        String bookId = bookIdField.getText().trim();
        String userId = userIdField.getText().trim();

        if (bookId.isEmpty() || userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = library.returnBookGUI(bookId, userId);
        boolean success = result.startsWith("SUCCESS");
        String message = result.substring(result.indexOf(":") + 1);

        JOptionPane.showMessageDialog(this, message, "Return Book",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        if (success) {
            dispose();
        }
    }

}
