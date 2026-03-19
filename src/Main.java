import service.LibraryService;
import util.DataSeeder;
import ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        LibraryService library = new LibraryService();
        library.loadData();

        // Seed sample data on first launch (when library is empty)
        if (library.getAllBooks().isEmpty()) {
            DataSeeder.seed(library);
        }

        // Background thread: auto-saves library data every 60 seconds
        // Demonstrates practical use of Threading in a GUI application
        Thread autoSaveThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // 60 seconds
                    library.saveData();
                    System.out.println("[AutoSave] Data saved in background.");
                } catch (InterruptedException e) {
                    System.out.println("[AutoSave] Thread interrupted, stopping.");
                    break;
                }
            }
        });
        autoSaveThread.setDaemon(true); // exits with the JVM, doesn't block shutdown
        autoSaveThread.setName("AutoSave-Thread");
        autoSaveThread.start();

        System.out.println("[AutoSave] Background save thread started (every 60s).");

        // Launch GUI on Swing's Event Dispatch Thread (thread safety)
        SwingUtilities.invokeLater(() -> new LoginFrame(library));

    }

}
