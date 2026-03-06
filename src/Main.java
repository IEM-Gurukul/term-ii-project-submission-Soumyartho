import service.LibraryService;
import ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        LibraryService library = new LibraryService();
        library.loadData();

        SwingUtilities.invokeLater(() -> new LoginFrame(library));

    }

}
