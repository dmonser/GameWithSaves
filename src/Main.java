import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static StringBuilder log = new StringBuilder();
    public static final String PARANT_DIR = "/home/rnosov/JavaGame/";

    public static void touch (String path, String fileName) throws IOException {
        File file = new File(path, fileName);
        try {
            if (file.createNewFile()) {
                log.append("File \"" + fileName + "\" created in dir " + path + ".\n");
                //System.out.println("File \"" + fileName + "\" created in dir " + path);
            } else {
                log.append("Error creating the file \"" + fileName + "\" in dir " + path + "!\n");
            }
        } catch (IOException ex) {
            log.append(ex.getMessage() + "\n");
        }
    }

    public static void makeDir (String path){
        File dir = new File(path);
        if (dir.mkdir()) {
            log.append("Directory " + path + " is create.\n");
        } else {
            log.append("Error creating directory: " + path + "\n");
        }
    }

    public static void main(String[] args) throws IOException {
        makeDir(PARANT_DIR + "src"); makeDir(PARANT_DIR + "res");
        makeDir(PARANT_DIR + "savegames"); makeDir(PARANT_DIR + "temp"); //1.

        makeDir(PARANT_DIR + "src/main"); makeDir(PARANT_DIR + "src/test"); //2.

        touch(PARANT_DIR + "src/main", "Main.java");
        touch(PARANT_DIR + "src/main", "Utils.java"); //3.

        makeDir(PARANT_DIR + "res/drawables"); makeDir(PARANT_DIR + "res/vectors");
        makeDir(PARANT_DIR + "res/icons"); //4.

        touch(PARANT_DIR + "temp", "temp.txt"); //5

        String outPut = log.toString();
        byte[] buffer = outPut.getBytes();

        try (FileOutputStream out = new FileOutputStream(PARANT_DIR + "temp/temp.txt");
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            bos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}