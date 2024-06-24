package com.nach.core.util.file;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ZipCreator {

    public static void addToZipFile(String basePath, File file, ZipArchiveOutputStream zos) throws IOException {
        String entryName = basePath.isEmpty() ? file.getName() : basePath + "/" + file.getName();

        if (file.isDirectory()) {
            ZipArchiveEntry zipEntry = new ZipArchiveEntry(entryName + "/");
            zos.putArchiveEntry(zipEntry);
            zos.closeArchiveEntry();

            // Recurse into the directory
            for (File childFile : file.listFiles()) {
                addToZipFile(entryName, childFile, zos);
            }
        } else {
            // Create a zip entry for a file
            ZipArchiveEntry zipEntry = new ZipArchiveEntry(entryName);
            zos.putArchiveEntry(zipEntry);

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            fis.close();
            zos.closeArchiveEntry();
        }
    }

    public static void createZip(File directoryToZip, File zipOutput) throws IOException {
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new FileOutputStream(zipOutput))) {
            for (File file : directoryToZip.listFiles()) {
                addToZipFile("", file, zos);
            }
        }
    }

    public static void main(String[] args) {
        String dirPath = "path/to/your/directory"; // Replace with the actual path
        File directoryToZip = new File(dirPath);
        File zipOutput = new File(directoryToZip.getParent(), directoryToZip.getName() + ".zip");

        try {
            createZip(directoryToZip, zipOutput);
            System.out.println("Zip file created at " + zipOutput.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
