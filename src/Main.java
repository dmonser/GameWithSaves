import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static StringBuilder log = new StringBuilder();
    public static ArrayList<String> savesList = new ArrayList<>();
    public static final String PARENT_DIR = "/home/user/JavaGame/";
    public static final String PATH_TO_SAVE = PARENT_DIR + "savegames/";


    public static void touch(String path, String fileName) throws IOException {
        File file = new File(path, fileName);
        try {
            if (file.createNewFile()) {
                log.append("File \"").append(fileName).append("\" created in dir ").append(path).append(".\n");
            }
        } catch (IOException ex) {
            log.append(ex.getMessage());
        }
    }

    public static void makeDir(String path) {
        File dir = new File(path);
        if (dir.mkdir()) {
            log.append("Directory ").append(path).append(" is create.\n");
        } else {
            log.append("Error creating directory: ").append(path).append("\n");
        }
    }

    public static void saveGames(String path, GameProgress save) throws IOException { // TODO: 28.11.2022
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(save);
            log.append("Saving was successful in file: ").append(path).append("\n");
        } catch (IOException ex) {
            log.append(ex.getMessage()).append("\n");
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
            log.append("The archive was created successfully in file: ").append(path).append("\n");
            zos.close();
        } catch (Exception ex) {
            log.append(ex.getMessage()).append("\n");
        }
    }

    public static void openZip(String pathToArchive, String pathToUnzip) {

        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(pathToArchive))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(PATH_TO_SAVE + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
            log.append("The archive ").append(pathToArchive).append(" has been unpacked correct.\n");
        } catch (Exception ex) {
            log.append(ex.getMessage()).append("\n");
        }
    }

    public static GameProgress openProgress(String path) throws IOException, ClassNotFoundException {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            log.append(ex.getMessage()).append("\n");
        }
        log.append("Save is correct deserialization from file: ").append(path).append("\n");
        return gameProgress;
    }

    public static void writeLogInFile() {
        String outPut = log.toString();
        byte[] buffer = outPut.getBytes();

        try (FileOutputStream out = new FileOutputStream(PARENT_DIR + "temp/temp.txt");
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            bos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        makeDir(PARENT_DIR + "src");
        makeDir(PARENT_DIR + "res");
        makeDir(PARENT_DIR + "savegames");
        makeDir(PARENT_DIR + "temp"); //1.

        makeDir(PARENT_DIR + "src/main");
        makeDir(PARENT_DIR + "src/test"); //2.

        touch(PARENT_DIR + "src/main", "Main.java");
        touch(PARENT_DIR + "src/main", "Utils.java"); //3.

        makeDir(PARENT_DIR + "res/drawables");
        makeDir(PARENT_DIR + "res/vectors");
        makeDir(PARENT_DIR + "res/icons"); //4.

        touch(PARENT_DIR + "temp", "temp.txt"); //5

        GameProgress save1 = new GameProgress(98, 2, 4, 115);
        GameProgress save2 = new GameProgress(112, 4, 8, 196);
        GameProgress save3 = new GameProgress(150, 8, 15, 368);

        saveGames(PATH_TO_SAVE + "save1.dat", save1);
        saveGames(PATH_TO_SAVE + "save2.dat", save2);
        saveGames(PATH_TO_SAVE + "save3.dat", save3);

        zipFiles(PATH_TO_SAVE + "saves.zip", savesList);

        openZip(PATH_TO_SAVE + "saves.zip", PATH_TO_SAVE);

        GameProgress gameProgress = openProgress(PATH_TO_SAVE + "save2.dat");
        System.out.println(gameProgress);

        writeLogInFile();
    }
}