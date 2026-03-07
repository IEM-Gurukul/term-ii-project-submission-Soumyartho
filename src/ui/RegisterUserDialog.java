package ui;

import model.StudentMember;
import model.FacultyMember;
import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class RegisterUserDialog extends JDialog {

    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> typeCombo;

    private LibraryService library;

    // Colors
    private static final Color BG_DARK = new Color(30, 30, 46);
    private static final Color BG_SURFACE = new Color(36, 39, 58);
    private static final Color TEXT_PRIMARY = new Color(205, 214, 244);
    private static final Color TEXT_SECONDARY = new Color(166, 173, 200);
    private static final Color ACCENT_BLUE = new Color(137, 180, 250);

    public RegisterUserDialog(JFrame parent, LibraryService library) {

        super(parent, "Register New User", true);
        this.library = library;

        setSize(400, 250);
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

        // User ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel idLabel = new JLabel("User ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(TEXT_SECONDARY);
        add(idLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        idField = createStyledField();
        add(idField, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_SECONDARY);
        add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = createStyledField();
        add(nameField, gbc);

        // Type
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        JLabel typeLabel = new JLabel("User Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLabel.setForeground(TEXT_SECONDARY);
        add(typeLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        typeCombo = new JComboBox<>(new String[]{"Student", "Faculty"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeCombo.setBackground(BG_SURFACE);
        typeCombo.setForeground(TEXT_PRIMARY);
        add(typeCombo, gbc);

        // Register button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 12, 8, 12);

        JButton registerBtn = new JButton("Register User");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setForeground(new Color(30, 30, 46));
        registerBtn.setBackground(ACCENT_BLUE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setPreferredSize(new Dimension(0, 38));
        registerBtn.addActionListener(e -> registerUser());
        add(registerBtn, gbc);
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

    private void registerUser() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (library.getUser(id) != null) {
            JOptionPane.showMessageDialog(this, "User ID already exists.", "Duplicate", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Student".equals(type)) {
            library.registerUser(new StudentMember(id, name));
        } else {
            library.registerUser(new FacultyMember(id, name));
        }

        JOptionPane.showMessageDialog(this, name + " registered as " + type + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
