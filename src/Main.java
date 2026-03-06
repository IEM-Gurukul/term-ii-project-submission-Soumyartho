import service.LibraryService;
import ui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        // Load library data
        LibraryService library = new LibraryService();
        library.loadData();

        // Launch GUI on the Event Dispatch Thread (Swing best practice)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });

    }

}
