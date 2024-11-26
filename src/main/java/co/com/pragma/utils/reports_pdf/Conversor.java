package co.com.pragma.utils.reports_pdf;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Conversor {
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(Conversor.class));

    public Conversor() {
    }

    public static void zipFolder(String sourceFolder, String outputFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        try {
            addFolderToZip("", sourceFolder, zos);
        } finally {
            zos.close();
            fos.close();
        }
    }

    private static void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            FileInputStream in = new FileInputStream(srcFile);
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));

            int len;
            while((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }

            in.close();
        }

    }

    private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        String[] var4 = (String[])Objects.requireNonNull(folder.list());
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String fileName = var4[var6];
            if (path.equals("")) {
                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
            } else {
                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
            }
        }

    }

    public static String codificarBase64(String filePath) throws RuntimeException {
        String base64String = null;

        try {
            base64String = fileToBase64(filePath);
        } catch (IOException var3) {
            LOGGER.info(var3.getMessage());
        }

        return base64String;
    }

    public static String fileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = new byte[(int)file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(fileContent);
        fileInputStream.close();
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String convertirfileJsonAStringUTF8(String path) throws IOException {
        FileInputStream flieinputStream = new FileInputStream(path);
        return IOUtils.toString(flieinputStream, StandardCharsets.UTF_8);
    }

    public static String convertirStringABase64(String strAConvertir) {
        byte[] encodedBytes = Base64.getEncoder().encode(strAConvertir.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}

