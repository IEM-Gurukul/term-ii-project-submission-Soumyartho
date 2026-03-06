package ui;

import service.LibraryService;
import model.Book;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {

    private LibraryService library;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // Consistent color palette (matches LoginFrame)
    private static final Color BG_DARK = new Color(30, 30, 46);
    private static final Color BG_SURFACE = new Color(36, 39, 58);
    private static final Color BG_OVERLAY = new Color(49, 50, 68);
    private static final Color TEXT_PRIMARY = new Color(205, 214, 244);
    private static final Color TEXT_SECONDARY = new Color(166, 173, 200);
    private static final Color ACCENT_BLUE = new Color(137, 180, 250);
    private static final Color ACCENT_GREEN = new Color(166, 227, 161);
    private static final Color ACCENT_PEACH = new Color(250, 179, 135);
    private static final Color ACCENT_RED = new Color(243, 139, 168);
    private static final Color ACCENT_MAUVE = new Color(203, 166, 247);

    public DashboardFrame(LibraryService library) {

        this.library = library;

        setTitle("Library Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        setLayout(new BorderLayout(0, 0));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.WEST);
        add(createStatusBar(), BorderLayout.SOUTH);

        refreshBookTable();

        setVisible(true);
    }

    // --- Header ---

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("\u2588\u2588 Library Dashboard");
        title.setFont(new Font("Consolas", Font.BOLD, 20));
        title.setForeground(ACCENT_BLUE);

        JLabel info = new JLabel("Manage your library  |  v1.0");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(TEXT_SECONDARY);

        header.add(title, BorderLayout.WEST);
        header.add(info, BorderLayout.EAST);

        return header;
    }

    // --- Side Button Panel ---

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
        panel.setPreferredSize(new Dimension(180, 0));

        JLabel menuLabel = new JLabel("ACTIONS");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menuLabel.setForeground(TEXT_SECONDARY);
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(menuLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        panel.add(createSideButton("Add Book", ACCENT_GREEN, e -> onAddBook()));
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(createSideButton("Register User", ACCENT_BLUE, e -> onRegisterUser()));
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(createSideButton("Issue Book", ACCENT_PEACH, e -> onIssueBook()));
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(createSideButton("Return Book", ACCENT_MAUVE, e -> onReturnBook()));
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(createSideButton("Search Books", TEXT_PRIMARY, e -> onSearchBooks()));

        panel.add(Box.createVerticalGlue());

        panel.add(createSideButton("Refresh", ACCENT_BLUE, e -> refreshBookTable()));
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(createSideButton("Save & Exit", ACCENT_RED, e -> onSaveAndExit()));

        return panel;
    }

    private JButton createSideButton(String text, Color accentColor, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(accentColor);
        btn.setBackground(BG_OVERLAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 36));
        btn.setPreferredSize(new Dimension(160, 36));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(69, 71, 90));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BG_OVERLAY);
            }
        });

        btn.addActionListener(action);
        return btn;
    }

    // --- Book Table ---

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tableTitle = new JLabel("  Book Catalog");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(TEXT_PRIMARY);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 8, 0));

        tableModel = new DefaultTableModel(
            new String[]{"Book ID", "Title", "Author", "Category", "Available", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookTable.setRowHeight(28);
        bookTable.setBackground(BG_SURFACE);
        bookTable.setForeground(TEXT_PRIMARY);
        bookTable.setGridColor(BG_OVERLAY);
        bookTable.setSelectionBackground(new Color(69, 71, 90));
        bookTable.setSelectionForeground(ACCENT_BLUE);
        bookTable.setShowHorizontalLines(true);
        bookTable.setShowVerticalLines(false);
        bookTable.setIntercellSpacing(new Dimension(0, 1));

        // Header styling
        JTableHeader header = bookTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(BG_OVERLAY);
        header.setForeground(ACCENT_BLUE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_BLUE));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_SURFACE);

        panel.add(tableTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- Status Bar ---

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(BG_SURFACE);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        JLabel status = new JLabel("System ready  |  Data loaded from disk");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        status.setForeground(TEXT_SECONDARY);

        bar.add(status);
        return bar;
    }

    // --- Data Refresh ---

    public void refreshBookTable() {
        tableModel.setRowCount(0);

        for (Book book : library.getAllBooks()) {
            String status = book.isAvailable() ? "Available" : "All Issued";
            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableCopies() + "/" + book.getTotalCopies(),
                status
            });
        }
    }

    // --- Button Actions (placeholder — will connect dialogs in Stage 8) ---

    private void onAddBook() {
        JOptionPane.showMessageDialog(this, "Add Book dialog coming in next stage.", "Add Book", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onRegisterUser() {
        JOptionPane.showMessageDialog(this, "Register User dialog coming in next stage.", "Register User", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onIssueBook() {
        JOptionPane.showMessageDialog(this, "Issue Book dialog coming in next stage.", "Issue Book", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onReturnBook() {
        JOptionPane.showMessageDialog(this, "Return Book dialog coming in next stage.", "Return Book", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onSearchBooks() {
        String keyword = JOptionPane.showInputDialog(this, "Enter title to search:", "Search Books", JOptionPane.QUESTION_MESSAGE);
        if (keyword != null && !keyword.trim().isEmpty()) {
            tableModel.setRowCount(0);
            for (Book book : library.getAllBooks()) {
                if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getCategory().toLowerCase().contains(keyword.toLowerCase())) {

                    String status = book.isAvailable() ? "Available" : "All Issued";
                    tableModel.addRow(new Object[]{
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.getAvailableCopies() + "/" + book.getTotalCopies(),
                        status
                    });
                }
            }
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No books found matching: " + keyword, "Search", JOptionPane.INFORMATION_MESSAGE);
                refreshBookTable();
            }
        }
    }

    private void onSaveAndExit() {
        library.saveData();
        JOptionPane.showMessageDialog(this, "Data saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

}
