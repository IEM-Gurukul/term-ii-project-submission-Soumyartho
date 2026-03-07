package ui;

import model.Book;
import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class AddBookDialog extends JDialog {

    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField categoryField;
    private JTextField copiesField;

    private LibraryService library;

    // Colors (match dashboard theme)
    private static final Color BG_DARK = new Color(30, 30, 46);
    private static final Color BG_SURFACE = new Color(36, 39, 58);
    private static final Color TEXT_PRIMARY = new Color(205, 214, 244);
    private static final Color TEXT_SECONDARY = new Color(166, 173, 200);
    private static final Color ACCENT_GREEN = new Color(166, 227, 161);

    public AddBookDialog(JFrame parent, LibraryService library) {

        super(parent, "Add New Book", true);
        this.library = library;

        setSize(400, 340);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        buildForm();

        setVisible(true);
    }

    private void buildForm() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Book ID:", "Title:", "Author:", "Category:", "Total Copies:"};
        JTextField[] fields = new JTextField[5];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(TEXT_SECONDARY);
            add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            fields[i] = new JTextField(18);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fields[i].setBackground(BG_SURFACE);
            fields[i].setForeground(TEXT_PRIMARY);
            fields[i].setCaretColor(TEXT_PRIMARY);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(69, 71, 90)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            add(fields[i], gbc);
        }

        idField = fields[0];
        titleField = fields[1];
        authorField = fields[2];
        categoryField = fields[3];
        copiesField = fields[4];

        // Add button
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 12, 8, 12);

        JButton addBtn = new JButton("Add Book");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(new Color(30, 30, 46));
        addBtn.setBackground(ACCENT_GREEN);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(0, 38));
        addBtn.addActionListener(e -> addBook());
        add(addBtn, gbc);
    }

    private void addBook() {
        try {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            String copiesText = copiesField.getText().trim();

            if (id.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty() || copiesText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int copies = Integer.parseInt(copiesText);
            if (copies <= 0) {
                JOptionPane.showMessageDialog(this, "Copies must be a positive number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Book book = new Book(id, title, author, category, copies);
            library.addBook(book);

            JOptionPane.showMessageDialog(this, "Book '" + title + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Copies must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
