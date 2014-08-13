package szregplat.util;

import szregplat.omc.Ws320RuntimeException;

import java.io.*;

/**
 * Created by Administrator on 14-4-12.
 */
public class FileUtils {
    private FileUtils ins;
    private String directory;

    private FileUtils(String dir) {
        if (dir == null)
            dir = "." + File.separator;

        if (!dir.endsWith(File.separator))
            dir += File.separator;

        directory = dir;
    }

    public static FileUtils getInstance() {
        return getInstance("." + File.separator);
    }

    public static FileUtils getInstance(String dir) {
        return new FileUtils(dir);
    }


    public static String getFileContent(File file) throws IOException {
        return getFileContent(file, "UTF-8");
    }


    /* instance methods */
    public String getFileContent(String fileName) throws IOException {
        if (fileName.startsWith("/"))
            fileName = fileName.substring(1);

        return getFileContent(new File(directory + fileName), "UTF-8");
    }

    public void saveAs(String fileName, String content) {
        try {
            File file = new File(directory + fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) parentFile.mkdirs();

            FileOutputStream fos = new FileOutputStream(directory + fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf8");
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new Ws320RuntimeException(e);
        }
    }


    /* static methods */
    public static String load(String fileName) {
        InputStream is = FileUtils.class.getResourceAsStream(fileName);
        if (is == null)
            is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null)
            throw new Ws320RuntimeException("file not found[" + fileName + "]");

        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b;
        try {
            while ((b = bis.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {
            throw new Ws320RuntimeException(e);
        }
        try {
            bis.close();
        } catch (IOException e) {
            throw new Ws320RuntimeException(e);
        }

        return baos.toString();
    }

    public static String getFileContent(File file, String encoding) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b;
        while ((b = bis.read()) != -1) {
            baos.write(b);
        }

        bis.close();
        return baos.toString(encoding);
    }

    public static boolean ancestorExists(File file) {
        do {
            if (file.exists()) return true;
            else file = file.getParentFile();
        } while (file != null);

        return false;
    }

    public static boolean ancestorExists(String path) {
        return ancestorExists(new File(path));
    }

    public static ByteArrayOutputStream getBytes(String file) throws IOException {
        new File(file);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b;
        while ((b = bis.read()) != -1) {
            baos.write(b);
        }

        bis.close();
        return baos;
    }

    public static void move(File from, File to) throws IOException {
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);

        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        int b = -1;
        while ((b = bis.read()) != -1) {
            bos.write(b);
        }

        bis.close();
        bos.close();

        from.delete();
    }
}
