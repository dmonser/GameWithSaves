import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static StringBuilder log = new StringBuilder();
    public static ArrayList<String> savesList = new ArrayList<>();
    public static final String PARANT_DIR = "/home/rnosov/JavaGame/";
    public static final String PATH_TO_SAVE = PARANT_DIR + "savegames/";


    public static void touch(String path, String fileName) throws IOException {
        File file = new File(path, fileName);
        try {
            if (file.createNewFile()) {
                log.append("File \"" + fileName + "\" created in dir " + path + ".\n");
            } else {
                log.append("Error creating the file \"" + fileName + "\" in dir " + path + "!\n");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void makeDir(String path) {
        File dir = new File(path);
        if (dir.mkdir()) {
            log.append("Directory " + path + " is create.\n");
        } else {
            log.append("Error creating directory: " + path + "\n");
        }
    }

    public static void saveGames(String path, GameProgress save) throws IOException { // TODO: 28.11.2022
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(save);
            log.append("Saving was successful in file: " + path + "\n");
        } catch (IOException ex) {
            log.append(ex.getMessage() + "\n");
        }
        savesList.add(path);
    }

    public static void zipFiles(String path, ArrayList<String> list) { // TODO: 28.11.2022
        int counter = list.size();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < counter; i++) {
                File save = new File(list.get(i));
                ZipEntry zipEntry = new ZipEntry(save.getName()); //save.getName()???
                zos.putNextEntry(zipEntry);
                zos.write(Files.readAllBytes(save.toPath()));
                save.delete();
            }
            log.append("The archive was created successfully in file: " + path + "\n");
            zos.close();
        }
        catch (Exception ex) {
            log.append(ex.getMessage());
        }
    }

    public static void writeLogInFile() {
        String outPut = log.toString();
        byte[] buffer = outPut.getBytes();

        try (FileOutputStream out = new FileOutputStream(PARANT_DIR + "temp/temp.txt");
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            bos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        makeDir(PARANT_DIR + "src");
        makeDir(PARANT_DIR + "res");
        makeDir(PARANT_DIR + "savegames");
        makeDir(PARANT_DIR + "temp"); //1.

        makeDir(PARANT_DIR + "src/main");
        makeDir(PARANT_DIR + "src/test"); //2.

        touch(PARANT_DIR + "src/main", "Main.java");
        touch(PARANT_DIR + "src/main", "Utils.java"); //3.

        makeDir(PARANT_DIR + "res/drawables");
        makeDir(PARANT_DIR + "res/vectors");
        makeDir(PARANT_DIR + "res/icons"); //4.

        touch(PARANT_DIR + "temp", "temp.txt"); //5

        GameProgress save1 = new GameProgress(98, 2, 4, 115);
        GameProgress save2 = new GameProgress(112, 4, 8, 196);
        GameProgress save3 = new GameProgress(150, 8, 15, 368);

        saveGames(PATH_TO_SAVE + "save1.dat", save1);
        saveGames(PATH_TO_SAVE + "save2.dat", save2);
        saveGames(PATH_TO_SAVE + "save3.dat", save3);

        zipFiles(PATH_TO_SAVE + "saves.zip", savesList);

        writeLogInFile();
    }
}