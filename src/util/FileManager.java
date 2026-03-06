package util;

import java.io.*;

public class FileManager {

    public static void saveObject(Object data, String filename) {

        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(filename))) {

            out.writeObject(data);

        } catch (IOException e) {
            System.out.println("Error saving file: " + filename);
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public static Object loadObject(String filename) {

        File file = new File(filename);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream in =
                new ObjectInputStream(new FileInputStream(file))) {

            return in.readObject();

        } catch (Exception e) {
            System.out.println("Error loading file: " + filename);
            return null;
        }

    }

}
